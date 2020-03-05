package life.gerayking.community.community.service;


import com.alibaba.fastjson.JSON;
import life.gerayking.community.community.dto.GithubUser;
import life.gerayking.community.community.dto.QuoteDTO;
import life.gerayking.community.community.exception.CustomizeErrorCode;
import life.gerayking.community.community.exception.CustomizeException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QuoteService {
  public QuoteDTO get(){
      OkHttpClient client = new OkHttpClient();
      Request request = new Request.Builder()
              .url("http://open.iciba.com/dsapi/")
              .build();
      try (Response response = client.newCall(request).execute()) {
          String string = response.body().string();
          QuoteDTO quoteDTO = JSON.parseObject(string, QuoteDTO.class);
          if(quoteDTO.getNote()==null){
              quoteDTO.setNote("虽然世界充满苦难，但是苦难总是可以战胜的。");
              quoteDTO.setPicture("https://edu-wps.ks3-cn-beijing.ksyun.com/image/f7825abb9deb3fc2ebbfb869f4f7be48.jpg");
              quoteDTO.setFenxiang_img("https://edu-wps.ks3-cn-beijing.ksyun.com/image/a6a75388fdc594bae470d6bb6d1bba50.png");
          }
          return quoteDTO;
      } catch (
              IOException e) {
          throw new CustomizeException(CustomizeErrorCode.CONNECT_TIME_OUT);
      }
  }
}
