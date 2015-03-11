package im.chic.weixin.utils;

import im.chic.weixin.utils.api.WeixinAPI;
import im.chic.weixin.utils.api.WeixinDeviceAPI;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.concurrent.TimeUnit;

/**
 * @author huahang
 */
@RunWith(JUnit4.class)
public class WeixinAPITest {
    @Test
    @Ignore
    public void testGetTokenAndTicket() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://api.weixin.qq.com").build();
        WeixinAPI weixinAPI = adapter.create(WeixinAPI.class);
        WeixinAPI.Token token = weixinAPI.getToken("client_credential", "", "");
        Assert.assertEquals(null, token.access_token);
        token = weixinAPI.getToken(
                "client_credential",
                "",
                ""
        );
        Assert.assertNotNull(token.access_token);
        Assert.assertNotNull(token.expires_in);
        WeixinAPI.Ticket ticket = weixinAPI.getTicket("", "jsapi");
        Assert.assertEquals(null, ticket.ticket);
        ticket = weixinAPI.getTicket(token.access_token, "jsapi");
        Assert.assertNotNull(ticket.ticket);
        Assert.assertNotNull(ticket.expires_in);
        return;
    }
}
