package lift.xql.community.provider;

import com.alibaba.fastjson.JSON;
import lift.xql.community.dto.AccessTokenDTO;
import lift.xql.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToker(AccessTokenDTO accessTokenDTO){
       MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token?client_id=Iv1.08230ffdebc53b50&client_secret=14e517b3a9a688fdecd530f76cb395d9d3d2ca4b" +
                        "&code="+accessTokenDTO.getCode()+"&redirect_uri=http://localhost:8080/callback&state=1")
//                .url("https://github.com/login/oauth/access_token?client_id="+accessTokenDTO.getClinet_id()+"&client_secret="+accessTokenDTO.getClinet_secret()
//                        +"&code="+accessTokenDTO.getCode()+"&redirect_uri="+accessTokenDTO.getRediect_url()+"&state="+accessTokenDTO.getState())
                .post(body)
                .build();


        /*Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token?client_id=e(你的id)&client_secret=(你的sercret)&code="+accessTokenDTO.getCode()+"&redirect_uri=http://localhost:8887/callback&state=1")
                .post(body)
                .build();*/
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            System.out.println(string);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+ accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
