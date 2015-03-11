package im.chic.weixin.utils;

import im.chic.weixin.utils.api.WeixinDeviceAPI;
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
public class WeixinDeviceAPITest {
    @Test
    @Ignore
    public void testAddDevice() throws InterruptedException {
        RestAdapter adapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://api.weixin.qq.com").build();
        WeixinDeviceAPI weixinAPI = adapter.create(WeixinDeviceAPI.class);
        WeixinDeviceAPI.AuthorizeDeviceRequest addDeviceRequest = new WeixinDeviceAPI.AuthorizeDeviceRequest();
        WeixinDeviceAPI.AuthorizeDeviceRequest.Device device = new WeixinDeviceAPI.AuthorizeDeviceRequest.Device();
        device.id = "dev-1234";
        device.mac = "00:00:00:00:00:00".replace(":", "");
        addDeviceRequest.device_num = 1;
        addDeviceRequest.device_list.add(device);
        weixinAPI.authorizeDevice(
                "",
                addDeviceRequest,
                new Callback<WeixinDeviceAPI.AuthorizeDeviceResponse>() {
                    @Override
                    public void success(WeixinDeviceAPI.AuthorizeDeviceResponse addDeviceResponse, Response response) {
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
        WeixinDeviceAPI weixinAPI = adapter.create(WeixinDeviceAPI.class);
        WeixinDeviceAPI.CreateQRCodeRequest createQRCodeRequest = new WeixinDeviceAPI.CreateQRCodeRequest();
        createQRCodeRequest.device_id_list.add("dev-1234");
        createQRCodeRequest.device_num = 1;
        weixinAPI.createQRCode(
                "",
                createQRCodeRequest,
                new Callback<WeixinDeviceAPI.CreateQRCodeResponse>() {
                    @Override
                    public void success(WeixinDeviceAPI.CreateQRCodeResponse createQRCodeResponse, Response response) {
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
