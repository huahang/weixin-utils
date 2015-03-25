package im.chic.weixin.utils.api;

import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;
import org.json.JSONObject;

/**
 * @author huahang
 */
public interface WeixinTemplateMessageAPI {
    @POST("/cgi-bin/message/template/send")
    public JSONObject send(
            @Query("access_token") String accessToken,
            @Body JSONObject body
    );
}
