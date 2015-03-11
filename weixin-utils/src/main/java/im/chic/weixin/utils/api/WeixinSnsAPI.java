package im.chic.weixin.utils.api;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author huahang
 */
public interface WeixinSnsAPI {
    public static class GetAccessTokenResponse {
        public int errcode = 0;
        public String errmsg = "ok";
        public String access_token = "";
        public long expires_in = 0;
        public String refress_token = "";
        public String openid = "";
        public String scope = "";
    }

    @GET("/sns/oauth2/access_token")
    public GetAccessTokenResponse getAccessToken(
            @Query("appid") String appId,
            @Query("secret") String appSecret,
            @Query("code") String code,
            @Query("grant_type") String grant_type
    );

    public static class GetUserInfoResponse {
        public int errcode = 0;
        public String errmsg = "ok";
        public String openid = "";
        public String nickname = "";
        public String sex = "";
        public String province = "";
        public String city = "";
        public String country = "";
        public String headimgurl = "";
        public String unionid = "";
    }

    @GET("/sns/userinfo")
    public GetUserInfoResponse getUserInfo(
            @Query("access_token") String accessToken,
            @Query("openid") String openId,
            @Query("lang") String language
    );
}
