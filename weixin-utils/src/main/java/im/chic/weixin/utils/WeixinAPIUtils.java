package im.chic.weixin.utils;

import im.chic.weixin.utils.api.WeixinAPI;
import im.chic.weixin.utils.api.WeixinDeviceAPI;
import im.chic.weixin.utils.api.WeixinSnsAPI;
import im.chic.weixin.utils.api.WeixinTemplateAPI;
import im.chic.weixin.utils.api.WeixinTemplateMessageAPI;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RestAdapter;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import retrofit.mime.TypedString;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author huahang
 */
public class WeixinAPIUtils {
    static public WeixinAPI getWeixinAPI(final Class clazz) {
        final Logger logger = LoggerFactory.getLogger(clazz);
        return new RestAdapter.Builder()
                .setEndpoint("https://api.weixin.qq.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        logger.info(message);
                    }
                })
                .build().create(WeixinAPI.class);
    }

    static public WeixinSnsAPI getWeixinSNSAPI(final Class clazz) {
        final Logger logger = LoggerFactory.getLogger(clazz);
        return new RestAdapter.Builder()
                .setEndpoint("https://api.weixin.qq.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        logger.info(message);
                    }
                })
                .build().create(WeixinSnsAPI.class);
    }

    static public WeixinDeviceAPI getWeixinDeviceAPI(final Class clazz) {
        final Logger logger = LoggerFactory.getLogger(clazz);
        return new RestAdapter.Builder()
                .setEndpoint("https://api.weixin.qq.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        logger.info(message);
                    }
                })
                .build().create(WeixinDeviceAPI.class);
    }

    static public WeixinTemplateAPI getWeixinTemplateAPI(final Class clazz) {
        final Logger logger = LoggerFactory.getLogger(clazz);
        return new RestAdapter.Builder()
                .setEndpoint("https://api.weixin.qq.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        logger.info(message);
                    }
                })
                .build().create(WeixinTemplateAPI.class);
    }

    static public WeixinTemplateMessageAPI getWeixinTemplateMessageAPI(final Class clazz) {
        final Logger logger = LoggerFactory.getLogger(clazz);
        return new RestAdapter.Builder()
                .setEndpoint("https://api.weixin.qq.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        logger.info(message);
                    }
                })
                .setConverter(new Converter() {
                    @Override
                    public Object fromBody(TypedInput body, Type type) throws ConversionException {
                        try {
                            return new JSONObject(IOUtils.toString(body.in(), Charsets.UTF_8));
                        } catch (Throwable t) {
                            logger.error("Hit an error!", t);
                            throw new ConversionException(t);
                        }
                    }

                    @Override
                    public TypedOutput toBody(Object object) {
                        TypedOutput typedOutput = new TypedString(object.toString());
                        return typedOutput;
                    }
                })
                .build().create(WeixinTemplateMessageAPI.class);
    }
}
