package life.gerayking.community.community.controller;

import life.gerayking.community.community.mapper.UserMapper;
import life.gerayking.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/*
Creat by coderinker on 2019/12/2
* */
@Controller
public class HelloController{
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies)
        {
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                User user = userMapper.findBytoken(token);
                if(user!=null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }

        return "index";}

}
