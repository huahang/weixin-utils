package im.chic.weixin.utils;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import im.chic.weixin.utils.api.WeixinAPI;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicValue;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RestAdapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author huahang
 */
public class TokenCache extends LeaderSelectorListenerAdapter implements Closeable {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    private static class CachedTokens {
        public String access_token = null;
        public String js_ticket = null;
        public Long update_time = null;
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
        return instance.leaderSelector.hasLeadership();
    }

    public static String getAccessToken() {
        try {
            instance.initialize();
            String stringValue = new String(instance.value.get().postValue(), Charsets.UTF_8);
            CachedTokens cachedTokens = (new Gson()).fromJson(stringValue, CachedTokens.class);
            return cachedTokens.access_token;
        } catch (Throwable t) {
            logger.error("Hit an error!", t);
        }
        return null;
    }

    public static String getJsTicket() {
        try {
            instance.initialize();
            String stringValue = new String(instance.value.get().postValue(), Charsets.UTF_8);
            CachedTokens cachedTokens = (new Gson()).fromJson(stringValue, CachedTokens.class);
            return cachedTokens.js_ticket;
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
                    leaderSelector = new LeaderSelector(client, getZkLeaderPath(), this);
                    leaderSelector.autoRequeue();
                    leaderSelector.start();
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

    public void close() throws IOException {
        IOUtils.closeQuietly(leaderSelector);
        IOUtils.closeQuietly(client);
    }

    public void takeLeadership(CuratorFramework curatorFramework) {
        logger.info("Leadership taken");
        try {
            boolean needRefresh = false;
            try{
                String stringValue = new String(value.get().postValue(), Charsets.UTF_8);
                CachedTokens cachedTokens = (new Gson()).fromJson(stringValue, CachedTokens.class);
                String accessToken = null == cachedTokens ? null : cachedTokens.access_token;
                String jsTicket = null == cachedTokens ? null : cachedTokens.js_ticket;
                Long updateTime = null == cachedTokens ? null : cachedTokens.update_time;
                if (StringUtils.isBlank(accessToken)) {
                    needRefresh = true;
                }
                if (StringUtils.isBlank(jsTicket)) {
                    needRefresh = true;
                }
                if (null == updateTime || (System.currentTimeMillis() - updateTime) > TimeUnit.MINUTES.toMillis(30)) {
                    needRefresh = true;
                }
            } catch (Throwable t) {
                logger.error("Hit an error", t);
                needRefresh = true;
            }
            if (needRefresh) {
                logger.info("Will refresh tokens");
                RestAdapter adapter = (new RestAdapter.Builder()).setEndpoint("https://api.weixin.qq.com").build();
                WeixinAPI weixinAPI = adapter.create(WeixinAPI.class);
                WeixinAPI.Token token = weixinAPI.getToken("client_credential", WeixinConfig.getAppID(), WeixinConfig.getAppSecret());
                WeixinAPI.Ticket ticket = weixinAPI.getTicket(token.access_token, "jsapi");
                CachedTokens cachedTokens = new CachedTokens();
                cachedTokens.access_token = token.access_token;
                cachedTokens.js_ticket = ticket.ticket;
                cachedTokens.update_time = System.currentTimeMillis();
                value.forceSet((new Gson()).toJson(cachedTokens).getBytes(Charsets.UTF_8));
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
}
