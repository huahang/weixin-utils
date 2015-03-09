package im.chic.weixin.utils;

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

    @Test
    @Ignore
    public void testAddDevice() throws InterruptedException {
        RestAdapter adapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://api.weixin.qq.com").build();
        WeixinAPI weixinAPI = adapter.create(WeixinAPI.class);
        WeixinAPI.AuthorizeDeviceRequest addDeviceRequest = new WeixinAPI.AuthorizeDeviceRequest();
        WeixinAPI.AuthorizeDeviceRequest.Device device = new WeixinAPI.AuthorizeDeviceRequest.Device();
        device.id = "dev-1234";
        device.mac = "00:00:00:00:00:00".replace(":", "");
        addDeviceRequest.device_num = 1;
        addDeviceRequest.device_list.add(device);
        weixinAPI.authorizeDevice(
                "m0dmh-oRdpS_Moq0EWf8DHPfRLQZaBGA3K9KMN-tn9-5i9tbrhMWwskouxA5RAFC-Y6vQJ1tOL9ZAo4TCvP7ruefNSyfi3_H7fO3dugxkDk",
                addDeviceRequest,
                new Callback<WeixinAPI.AuthorizeDeviceResponse>() {
                    @Override
                    public void success(WeixinAPI.AuthorizeDeviceResponse addDeviceResponse, Response response) {
                        return;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        return;
                    }
                }
        );
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }

    @Test
    @Ignore
    public void testCreateQRCode() throws InterruptedException {
        RestAdapter adapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://api.weixin.qq.com").build();
        WeixinAPI weixinAPI = adapter.create(WeixinAPI.class);
        WeixinAPI.CreateQRCodeRequest createQRCodeRequest = new WeixinAPI.CreateQRCodeRequest();
        createQRCodeRequest.device_id_list.add("dev-1234");
        createQRCodeRequest.device_num = 1;
        weixinAPI.createQRCode(
                "m0dmh-oRdpS_Moq0EWf8DHPfRLQZaBGA3K9KMN-tn9-5i9tbrhMWwskouxA5RAFC-Y6vQJ1tOL9ZAo4TCvP7ruefNSyfi3_H7fO3dugxkDk",
                createQRCodeRequest,
                new Callback<WeixinAPI.CreateQRCodeResponse>() {
                    @Override
                    public void success(WeixinAPI.CreateQRCodeResponse createQRCodeResponse, Response response) {
                        return;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        return;
                    }
                }
        );
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }
}
