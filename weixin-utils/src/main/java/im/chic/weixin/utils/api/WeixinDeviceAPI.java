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
public interface WeixinDeviceAPI {
    public static class AuthorizeDeviceRequest {
        public static class Device {
            public String id = "";
            public String mac = "";
            public String connect_protocol = "4";
            public String auth_key = "";
            public int close_strategy = 1;
            public int conn_strategy = 1;
            public int crypt_method = 0;
            public int auth_ver = 0;
            public int manu_mac_pos = -1;
            public int ser_mac_pos = -2;
        }
        public int device_num = 0;
        public ArrayList<Device> device_list = new ArrayList<Device>();
        public int op_type = 0;
    }

    public static class AuthorizeDeviceResponse {
        public static class Item {
            public static class BaseInfo {
                public String device_type = "";
                public String device_id = "";
            }
            public int errcode = 0;
            public String errmsg = "ok";
            public BaseInfo base_info = new BaseInfo();
        }
        public int errcode = 0;
        public String errmsg = "ok";
        public ArrayList<Item> resp = new ArrayList<Item>();
    }

    @POST("/device/authorize_device")
    public void authorizeDevice(
            @Query("access_token") String accessToken,
            @Body AuthorizeDeviceRequest request,
            Callback<AuthorizeDeviceResponse> callback
    );

    public static class CreateQRCodeRequest {
        public int device_num = 0;
        public ArrayList<String> device_id_list = new ArrayList<String>();
    }

    public static class CreateQRCodeResponse {
        public static class Item {
            public String device_id = "";
            public String ticket = "";
        }
        public int errcode = 0;
        public String errmsg = "ok";
        public int device_num = 0;
        public ArrayList<Item> code_list = new ArrayList<Item>();
    }

    @POST("/device/create_qrcode")
    public void createQRCode(
            @Query("access_token") String accessToken,
            @Body CreateQRCodeRequest request,
            Callback<CreateQRCodeResponse> callback
    );

    public static class GetBindDeviceResponse {
        public static class RespMsg {
            public int ret_code = 0;
            public String error_info = "ok";
        }
        public static class Device {
            public String device_type = "";
            public String device_id = "";
        }
        public RespMsg resp_msg = new RespMsg();
        public String openid = "";
        public ArrayList<Device> device_list = new ArrayList<Device>();
    }

    @GET("/device/get_bind_device")
    public GetBindDeviceResponse getBindDevice(
            @Query("access_token") String accessToken,
            @Query("openid") String openId
    );

    public static class BindDeviceRequest {
        public String ticket = "";
        public String device_id = "";
        public String openid = "";
    }

    public static class BindDeviceResponse {
        public static class BaseResp {
            public int errcode = 0;
            public String errmsg = "ok";
        }
        public BaseResp base_resp = new BaseResp();
        public int errcode = 0;
        public String errmsg = "ok";
    }

    @POST("/device/bind")
    public BindDeviceResponse bind(
            @Query("access_token") String accessToken,
            @Body BindDeviceRequest request
    );

    public static class UnbindDeviceRequest {
        public String ticket = "";
        public String device_id = "";
        public String openid = "";
    }

    public static class UnbindDeviceResponse {
        public static class BaseResp {
            public int errcode = 0;
            public String errmsg = "ok";
        }
        public BaseResp base_resp = new BaseResp();
        public int errcode = 0;
        public String errmsg = "ok";
    }

    @POST("/device/unbind")
    public UnbindDeviceResponse unbind(
            @Query("access_token") String accessToken,
            @Body UnbindDeviceRequest request
    );

    public static class CompelBindDeviceRequest {
        public String device_id = "";
        public String openid = "";
    }

    public static class CompelBindDeviceResponse {
        public static class BaseResp {
            public int errcode = 0;
            public String errmsg = "ok";
        }
        public BaseResp base_resp = new BaseResp();
        public int errcode = 0;
        public String errmsg = "ok";
    }

    @POST("/device/compel_bind")
    public CompelBindDeviceResponse compelBind(
            @Query("access_token") String accessToken,
            @Body CompelBindDeviceRequest request
    );

    public static class CompelUnbindDeviceRequest {
        public String device_id = "";
        public String openid = "";
    }

    public static class CompelUnbindDeviceResponse {
        public static class BaseResp {
            public int errcode = 0;
            public String errmsg = "ok";
        }
        public BaseResp base_resp = new BaseResp();
        public int errcode = 0;
        public String errmsg = "ok";
    }

    @POST("/device/compel_unbind")
    public CompelUnbindDeviceResponse compelUnbind(
            @Query("access_token") String accessToken,
            @Body CompelUnbindDeviceRequest request
    );
}
