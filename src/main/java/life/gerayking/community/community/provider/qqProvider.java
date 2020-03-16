package life.gerayking.community.community.provider;

import com.alibaba.fastjson.JSON;
import life.gerayking.community.community.dto.AccesstokenDTO;
import life.gerayking.community.community.dto.qqUserDTO;
import life.gerayking.community.community.exception.CustomizeErrorCode;
import life.gerayking.community.community.exception.CustomizeException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class qqProvider {
    public static String getAccessToken(AccesstokenDTO accesstokenDTO) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id="
                +accesstokenDTO.getClient_id()+"&client_secret="+accesstokenDTO.getClient_secret()+"&code="
                +accesstokenDTO.getCode()+"&redirect_uri="+accesstokenDTO.getRedirect_uri();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()){
              String string = response.body().string();
            String[] split =string.split("&");
            String Token = split[0].split("=")[1];
            return Token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getOpenId(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://graph.qq.com/oauth2.0/me?access_token="+accessToken;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try(Response response = client.newCall(request).execute()){
            String string = response.body().string();
            String[] split =string.split("&");
            String clientId = split[0].split("=")[1];
            return clientId;
        }catch (Exception e){
            throw new CustomizeException(CustomizeErrorCode.GET_OPENID_FAIL);
        }
    }

    public static qqUserDTO getUser(String openId, String client_id, String accessToken) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://graph.qq.com/user/get_user_info?access_token="+accessToken+
                "&oauth_consumer_key="+client_id+
                "&openid="+openId;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()){
            String string = response.body().string();
            qqUserDTO qqUserDTO = JSON.parseObject(string,qqUserDTO.class);
            return qqUserDTO;
        }catch (Exception e){
            throw new CustomizeException(CustomizeErrorCode.CONNECT_TIME_OUT);
        }
    }
}
