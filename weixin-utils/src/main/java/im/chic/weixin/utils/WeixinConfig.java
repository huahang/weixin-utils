package im.chic.weixin.utils;

import com.google.common.io.Closer;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author huahang
 */
public class WeixinConfig {
    private static Logger logger = LoggerFactory.getLogger(WeixinConfig.class);

    public static class Item {
        @SerializedName("app_id")
        public String appID = "";
        @SerializedName("app_secret")
        public String appSecret = "";
        @SerializedName("weixin_id")
        public String weixinID = "";
        @SerializedName("message_encryption")
        public boolean messageEncryption = false;
        @SerializedName("message_token")
        public String messageToken = "";
        @SerializedName("message_key")
        public String messageKey = "";
        @SerializedName("comment")
        public String comment = "";
    }

    @SerializedName("config_map")
    public HashMap<String, Item> configMap = new HashMap<String, Item>();

    @SerializedName("zk_path")
    public String zkPath = "";

    @SerializedName("zk_server_list")
    public String zkServerList = "";

    public static String getZKPath() {
        return config.zkPath;
    }

    public static String getZKServerList() {
        return config.zkServerList;
    }

    public static Collection<String> getApps() {
        return config.configMap.keySet();
    }

    public static Item get(String appID) {
        if (!config.configMap.containsKey(appID)) {
            logger.error("Config for weixin app {} not found!", appID);
            return null;
        }
        return config.configMap.get(appID);
    }

    private static WeixinConfig load() throws IOException {
        Closer closer = Closer.create();
        try {
            InputStreamReader reader = new InputStreamReader(WeixinConfig.class.getClassLoader().getResourceAsStream("im.chic.weixin.utils.tokencache.json"));
            closer.register(reader);
            Gson gson = new Gson();
            return gson.fromJson(reader, WeixinConfig.class);
        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    private static final WeixinConfig config;
    static {
        try {
            config = load();
        } catch (Throwable t) {
            logger.error("Fatal error!", t);
            throw new RuntimeException(t);
        }
    }
}
