package im.chic.weixin.utils;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author huahang
 */
public interface WeixinAPI {
    public static class Token {
        public Long errcode;
        public String errmsg;
        public String access_token;
        public Long expires_in;
    }

    public static class Ticket {
        public Long errcode;
        public String errmsg;
        public String ticket;
        public Long expires_in;
    }

    @GET("/cgi-bin/token")
    Token getToken(
            @Query("grant_type") String grantType,
            @Query("appid") String appID,
            @Query("secret") String secret
    );

    @GET("/cgi-bin/ticket/getticket")
    Ticket getTicket(
            @Query("access_token") String accessToken,
            @Query("type") String type
    );
}
