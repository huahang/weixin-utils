package im.chic.weixin.utils;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import retrofit.RestAdapter;

/**
 * @author huahang
 */
@RunWith(JUnit4.class)
public class WeixinAPITest {
    @Test
    @Ignore
    public void testGetTokenAndTicket() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://api.weixin.qq.com")
                .build();
        WeixinAPI weixinAPI = adapter.create(WeixinAPI.class);
        WeixinAPI.Token token = weixinAPI.getToken("client_credential", "", "");
        Assert.assertEquals(null, token.access_token);
        token = weixinAPI.getToken(
                "client_credential",
                "wx2f9cdd8ce42e0a5f",
                "6d2ea96d294cbaf7c2c9f0b117c98173"
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
