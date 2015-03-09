package im.chic.weixin.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Properties;

/**
 * @author huahang
 */
public class WeixinConfig {
    private static final String PROPERTIES_FILE = "im.chic.weixin.utils.tokencache.properties";

    private static final String APP_ID = "tokencache.app_id";
    private static final String APP_SECRET = "tokencache.app_secret";
    private static final String ZK_PATH = "tokencache.zk_path";
    private static final String ZK_SERVER_LIST = "tokencache.zk_server_list";

    private static final Object lock = new Object();

    private static String appID = null;
    private static String appSecret = null;
    private static String zkPath = null;
    private static String zkServerList = null;

    private static void loadProperties() {
        try {
            synchronized (lock) {
                Properties properties = new Properties();
                properties.load(WeixinConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
                appID = properties.getProperty(APP_ID);
                Validate.notEmpty(StringUtils.trimToNull(appID), "Empty Weixin app ID");
                appSecret = properties.getProperty(APP_SECRET);
                Validate.notEmpty(StringUtils.trimToNull(appSecret), "Empty Weixin app secret");
                zkPath = properties.getProperty(ZK_PATH);
                Validate.notEmpty(StringUtils.trimToNull(zkPath), "Empty ZK path");
                zkServerList = properties.getProperty(ZK_SERVER_LIST);
                Validate.notEmpty(StringUtils.trimToNull(zkServerList), "Empty ZK server list");
            }
        } catch (Throwable t) {
            throw new RuntimeException("Unable to load properties", t);
        }
    }

    public static String getAppID() {
        if (StringUtils.isBlank(appID)) {
            loadProperties();
        }
        return appID;
    }

    public static String getAppSecret() {
        if (StringUtils.isBlank(appSecret)) {
            loadProperties();
        }
        return appSecret;
    }

    public static String getZKPath() {
        if (StringUtils.isBlank(zkPath)) {
            loadProperties();
        }
        return zkPath;
    }

    public static String getZKServerList() {
        if (StringUtils.isBlank(zkServerList)) {
            loadProperties();
        }
        return zkServerList;
    }
}
