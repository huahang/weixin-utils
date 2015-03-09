package im.chic.weixin.utils;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicValue;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.json.JSONException;
import org.json.JSONObject;
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
            return new JSONObject(stringValue).optString("access_token", null);
        } catch (Throwable t) {
            logger.error("Hit an error!", t);
        }
        return null;
    }

    public static String getJsTicket() {
        try {
            instance.initialize();
            String stringValue = new String(instance.value.get().postValue(), Charsets.UTF_8);
            return new JSONObject(stringValue).optString("js_ticket", null);
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
            String stringValue = new String(value.get().postValue(), Charsets.UTF_8);
            try {
                JSONObject jsonValue = new JSONObject(stringValue);
                String accessToken = jsonValue.optString("access_token", "");
                String jsTicket = jsonValue.optString("js_ticket", "");
                long updateTime = jsonValue.optLong("update_time", 0);
                if (StringUtils.isBlank(accessToken) ||
                        StringUtils.isBlank(jsTicket) ||
                        (System.currentTimeMillis() - updateTime) > TimeUnit.MINUTES.toMillis(30)) {
                    logger.info("Will refresh tokens");
                    RestAdapter adapter = (new RestAdapter.Builder()).setEndpoint("https://api.weixin.qq.com").build();
                    WeixinAPI weixinAPI = adapter.create(WeixinAPI.class);
                    WeixinAPI.Token token = weixinAPI.getToken("client_credential", WeixinConfig.getAppID(), WeixinConfig.getAppSecret());
                    WeixinAPI.Ticket ticket = weixinAPI.getTicket(token.access_token, "jsapi");
                    jsonValue.put("access_token", token.access_token);
                    jsonValue.put("js_ticket", ticket.ticket);
                    jsonValue.put("update_time", System.currentTimeMillis());
                    value.forceSet(jsonValue.toString().getBytes(Charsets.UTF_8));
                    logger.info("Tokens refreshed");
                } else {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(60));
                }
            } catch (JSONException e) {
                logger.error("Value not JSON: {}", stringValue);
                value.forceSet("{}".getBytes());
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
