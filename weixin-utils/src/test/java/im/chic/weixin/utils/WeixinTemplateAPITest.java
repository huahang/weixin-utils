package im.chic.weixin.utils;

import im.chic.weixin.utils.api.WeixinTemplateAPI;
import im.chic.weixin.utils.api.WeixinTemplateMessageAPI;
import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.json.JSONObject;

/**
 * @author huahang
 */
@RunWith(JUnit4.class)
@Ignore
public class WeixinTemplateAPITest {
    @Test
    public void testSetIndustry() {
        WeixinTemplateAPI weixinTemplateAPI = WeixinAPIUtils.getWeixinTemplateAPI(WeixinTemplateAPITest.class);
        WeixinTemplateAPI.SetIndustryRequest setIndustryRequest = new WeixinTemplateAPI.SetIndustryRequest();
        setIndustryRequest.industryID1 = "6";
        setIndustryRequest.industryID1 = "7";
        WeixinTemplateAPI.SetIndustryResponse response = weixinTemplateAPI.setIndustry(
                "",
                setIndustryRequest
        );
        return;
    }
    @Test
    public void testSendMessage() throws JSONException {
        WeixinTemplateMessageAPI weixinTemplateMessageAPI = WeixinAPIUtils.getWeixinTemplateMessageAPI(WeixinTemplateAPITest.class);
        JSONObject request = new JSONObject();
        request.put("touser", "");
        request.put("template_id", "");
        JSONObject requestData = new JSONObject();
        JSONObject deviceNameData = new JSONObject();
        deviceNameData.put("value", "小米手机");
        deviceNameData.put("color", "#173177");
        requestData.put("deviceName", deviceNameData);
        request.put("data", requestData);
        JSONObject response = weixinTemplateMessageAPI.send(
                "",
                request
        );
        return;
    }
}
