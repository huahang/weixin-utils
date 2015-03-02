package im.chic.weixin.utils;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import retrofit.RestAdapter;

/**
 * @author huahang
 */
public class TokenCache {
    private static Object lock = new Object();
    private static String accessToken = null;
    private static String jsTicket = null;
    private static long timestamp = 0;
    private static WeixinAPI weixinAPI = null;
    private static Log log = LogFactory.getLog(TokenCache.class);

    public static String getAccessToken() {
        if (StringUtils.isBlank(accessToken)) {
            refreshTokens();
        }
        return accessToken;
    }

    public static String getJsTicket() {
        if (StringUtils.isBlank(jsTicket)) {
            refreshTokens();
        }
        return jsTicket;
    }

    private static void refreshTokens() {
        synchronized (lock) {
            if (StringUtils.isNotBlank(accessToken) &&
                    StringUtils.isNotBlank(jsTicket) &&
                    (System.currentTimeMillis() - timestamp) > 3600000) {
                return;
            }
            if (null == weixinAPI) {
                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint("https://api.weixin.qq.com")
                        .build();
                weixinAPI = adapter.create(WeixinAPI.class);
            }
            WeixinAPI.Token token = weixinAPI.getToken(
                    "client_credential",
                    "wx2f9cdd8ce42e0a5f",
                    "6d2ea96d294cbaf7c2c9f0b117c98173"
            );
            log.debug((new Gson()).toJson(token));
            if (StringUtils.isBlank(token.access_token)) {
                return;
            }
            WeixinAPI.Ticket ticket = weixinAPI.getTicket(token.access_token, "jsapi");
            if (StringUtils.isBlank(ticket.ticket)) {
                return;
            }
            accessToken = token.access_token;
            jsTicket = ticket.ticket;
            timestamp = System.currentTimeMillis();
        }
    }
}
