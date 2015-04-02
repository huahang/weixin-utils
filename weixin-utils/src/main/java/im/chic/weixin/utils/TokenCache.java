package im.chic.weixin.utils;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import im.chic.weixin.utils.api.WeixinAPI;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicValue;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RestAdapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author huahang
 */
public class TokenCache extends LeaderSelectorListenerAdapter implements Closeable {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    private static class CachedTokens {
        private static class Item {
            @SerializedName("access_token")
            public String accessToken = "";
            @SerializedName("js_ticket")
            public String jsTicket = "";
        }
        @SerializedName("update_time")
        public long updateTime = 0;
        @SerializedName("map")
        public HashMap<String, Item> map = new HashMap<String, Item>();
    }

    private static final TokenCache instance = new TokenCache();

    private static String getZkLeaderPath() {
        return WeixinConfig.getZKPath() + "/leader";
    }

    private static String getZkValuePath() {
        return WeixinConfig.getZKPath() + "/value";
    }

    public static boolean isLeader() throws Exception {
        instance.initialize();
        return (null != instance.leaderSelector) && instance.leaderSelector.hasLeadership();
    }

    public static String getAccessToken(String appID) {
        try {
            instance.initialize();
            CachedTokens cachedTokens = instance.getCachedTokens();
            return cachedTokens.map.get(appID).accessToken;
        } catch (Throwable t) {
            logger.error("Hit an error!", t);
        }
        return null;
    }

    public static String getJsTicket(String appID) {
        try {
            instance.initialize();
            CachedTokens cachedTokens = instance.getCachedTokens();
            return cachedTokens.map.get(appID).jsTicket;
        } catch (Throwable t) {
            logger.error("Hit an error!", t);
        }
        return null;
    }

    private void initialize() throws Exception {
        if (!initialized) {
            logger.info("lock");
            synchronized (this) {
                logger.info("lock acquired");
                if (!initialized) {
                    String zkServers = WeixinConfig.getZKServerList();
                    logger.info("ZK servers: {}", zkServers);
                    client = CuratorFrameworkFactory.newClient(zkServers, new ExponentialBackoffRetry(1000, 10));
                    client.start();
                    client.blockUntilConnected();
                    logger.info("ZK connected");
                    if (WeixinConfig.isLeaderMode()) {
                        leaderSelector = new LeaderSelector(client, getZkLeaderPath(), this);
                        leaderSelector.autoRequeue();
                        leaderSelector.start();
                    }
                    value = new DistributedAtomicValue(client, getZkValuePath(), new ExponentialBackoffRetry(1000, 10));
                    initialized = true;
                }
            }
        }
    }

    private CuratorFramework client = null;
    private LeaderSelector leaderSelector = null;
    private DistributedAtomicValue value = null;

    private boolean initialized = false;

    public void close() {
        IOUtils.closeQuietly(leaderSelector);
        IOUtils.closeQuietly(client);
    }

    public void takeLeadership(CuratorFramework curatorFramework) {
        logger.info("Leadership taken");
        try {
            boolean needRefresh = false;
            try{
                CachedTokens cachedTokens = getCachedTokens();
                long updateTime = (null == cachedTokens) ? 0 : cachedTokens.updateTime;
                long duration = System.currentTimeMillis() - updateTime;
                if (Math.abs(duration) > TimeUnit.MINUTES.toMillis(30)) {
                    needRefresh = true;
                }
            } catch (Throwable t) {
                logger.error("Hit an error", t);
                needRefresh = true;
            }
            if (needRefresh) {
                logger.info("Will refresh tokens");
                Collection<String> apps = WeixinConfig.getApps();
                logger.debug("Apps: {}", apps);
                CachedTokens cachedTokens = getCachedTokens();
                if (null == cachedTokens) {
                    cachedTokens = new CachedTokens();
                }
                for (String app : apps) {
                    RestAdapter adapter = (new RestAdapter.Builder()).setEndpoint("https://api.weixin.qq.com").build();
                    WeixinAPI weixinAPI = adapter.create(WeixinAPI.class);
                    WeixinConfig.Item item = WeixinConfig.get(app);
                    CachedTokens.Item cacheTokenItem = new CachedTokens.Item();
                    for (int i = 0; i < 10; ++i) {
                        WeixinAPI.Token token = weixinAPI.getToken("client_credential", item.appID, item.appSecret);
                        if (StringUtils.isNoneBlank(token.access_token)) {
                            cacheTokenItem.accessToken = token.access_token;
                            break;
                        }
                    }
                    for (int i = 0; i < 10; ++i) {
                        WeixinAPI.Ticket ticket = weixinAPI.getTicket(cacheTokenItem.accessToken, "jsapi");
                        if (StringUtils.isNoneBlank(ticket.ticket)) {
                            cacheTokenItem.jsTicket = ticket.ticket;
                            break;
                        }

                    }
                    cachedTokens.map.put(app, cacheTokenItem);
                }
                cachedTokens.updateTime = System.currentTimeMillis();
                setCachedTokens(cachedTokens);
                logger.info("Tokens refreshed");
            } else {
                Thread.sleep(TimeUnit.SECONDS.toMillis(60));
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Throwable t) {
            logger.error("Unknown error", t);
        } finally {
            logger.info("Relinquishing leadership.");
        }
    }

    public CachedTokens getCachedTokens() {
        try {
            String stringValue = new String(value.get().postValue(), Charsets.UTF_8);
            CachedTokens cachedTokens = (new Gson()).fromJson(stringValue, CachedTokens.class);
            return cachedTokens;
        } catch (Throwable t) {
            logger.error("Hit an error!", t);
        }
        return null;
    }

    public void setCachedTokens(CachedTokens cachedTokens) {
        try {
            Gson gson = new Gson();
            String valueString = (new JSONObject(gson.toJson(cachedTokens))).toString(2);
            logger.debug("Tokens to be cached: {}", valueString);
            value.forceSet(valueString.getBytes(Charsets.UTF_8));
        } catch (Throwable t) {
            logger.error("Unknown error", t);
        }
    }
}
