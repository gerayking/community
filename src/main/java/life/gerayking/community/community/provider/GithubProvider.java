package life.gerayking.community.community.provider;

import com.alibaba.fastjson.JSON;
import life.gerayking.community.community.dto.AccesstokenDTO;
import life.gerayking.community.community.dto.GithubUser;
import life.gerayking.community.community.exception.CustomizeErrorCode;
import life.gerayking.community.community.exception.CustomizeException;
import okhttp3.*;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component//把当前的类初始化到spring的上下文
public class GithubProvider {
    public String getAccessToken(AccesstokenDTO accesstokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accesstokenDTO));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute())
            {
                String string = response.body().string();
                String[] split =string.split("&");
                String Token = split[0].split("=")[1];
                return Token;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
    }
    public GithubUser getUser(String accessToken)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            if(githubUser.getName()==null) githubUser.setName(githubUser.getLogin());
            return githubUser;
        } catch (IOException e) {
            throw new CustomizeException(CustomizeErrorCode.CONNECT_TIME_OUT);
        }
    }
}
