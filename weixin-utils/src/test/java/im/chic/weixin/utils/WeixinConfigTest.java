package im.chic.weixin.utils;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.TreeSet;

/**
 * @author huahang
 */
@RunWith(JUnit4.class)
public class WeixinConfigTest {
    @Test
    @Ignore
    public void test1() {
        Gson gson = new Gson();
        WeixinConfig config = new WeixinConfig();
        for (int i = 0; i < 3; ++i) {
            WeixinConfig.Item item = new WeixinConfig.Item();
            item.appID = "app" + i;
            item.appSecret = "secret" + i;
            item.weixinID = "gh_" + i;
            item.messageKey = "key" + i;
            config.configMap.put(item.appID, item);
        }
        String string = gson.toJson(config);
        Assert.assertEquals("{\"config_map\":{\"app2\":{\"app_id\":\"app2\",\"app_secret\":\"secret2\",\"weixin_id\":\"gh_2\",\"message_encryption\":false,\"message_token\":\"\",\"message_key\":\"key2\",\"comment\":\"\"},\"app1\":{\"app_id\":\"app1\",\"app_secret\":\"secret1\",\"weixin_id\":\"gh_1\",\"message_encryption\":false,\"message_token\":\"\",\"message_key\":\"key1\",\"comment\":\"\"},\"app0\":{\"app_id\":\"app0\",\"app_secret\":\"secret0\",\"weixin_id\":\"gh_0\",\"message_encryption\":false,\"message_token\":\"\",\"message_key\":\"key0\",\"comment\":\"\"}},\"zk_path\":\"\",\"zk_server_list\":\"\"}", string);
        return;
    }
    @Test
    public void test2() {
        WeixinConfig.Item item = WeixinConfig.get("app1");
        Assert.assertEquals("app1", item.appID);
        Assert.assertEquals("/path", WeixinConfig.getZKPath());
        Assert.assertNull(WeixinConfig.get("fda"));
        TreeSet<String> expected = new TreeSet<String>();
        expected.add("app1");
        expected.add("app2");
        Assert.assertEquals(expected, WeixinConfig.getApps());
        return;
    }
}
