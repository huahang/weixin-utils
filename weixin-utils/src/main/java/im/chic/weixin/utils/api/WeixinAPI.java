package im.chic.weixin.utils.api;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import java.util.ArrayList;

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

    @GET("/cgi-bin/token")
    public Token getToken(
            @Query("grant_type") String grantType,
            @Query("appid") String appID,
            @Query("secret") String secret
    );

    public static class Ticket {
        public Long errcode;
        public String errmsg;
        public String ticket;
        public Long expires_in;
    }

    @GET("/cgi-bin/ticket/getticket")
    public Ticket getTicket(
            @Query("access_token") String accessToken,
            @Query("type") String type
    );
}
