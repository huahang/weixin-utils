package im.chic.weixin.utils.api;

import com.google.gson.annotations.SerializedName;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * @author huahang
 */
public interface WeixinTemplateAPI {
    public static class SetIndustryRequest {
        @SerializedName("industry_id1")
        public String industryID1 = "";
        @SerializedName("industry_id2")
        public String industryID2 = "";
    }

    public static class SetIndustryResponse {
        @SerializedName("errcode")
        public int errorCode = 0;
        @SerializedName("errmsg")
        public String errorMessage = "ok";
    }

    @POST("/cgi-bin/template/api_set_industry")
    public SetIndustryResponse setIndustry(
            @Query("access_token") String accessToken,
            @Body SetIndustryRequest request
    );

    public static class AddTemplateRequest {
        @SerializedName("template_id_short")
        public String templateIDShort = "";
    }

    public static class AddTemplateResponse {
        @SerializedName("errcode")
        public int errorCode = 0;
        @SerializedName("errmsg")
        public String errorMessage = "ok";
    }

    @POST("/cgi-bin/template/api_add_template")
    public AddTemplateResponse addTemplate(
            @Query("access_token") String accessToken,
            @Body AddTemplateRequest request
    );
}
