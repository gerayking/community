package life.gerayking.community.community.controller;

import life.gerayking.community.community.dto.qqUserDTO;
import life.gerayking.community.community.provider.qqProvider;
import life.gerayking.community.community.dto.AccesstokenDTO;
import life.gerayking.community.community.dto.GithubUser;
import life.gerayking.community.community.mapper.UserMapper;
import life.gerayking.community.community.model.User;
import life.gerayking.community.community.provider.GithubProvider;
import life.gerayking.community.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Value("${github.client_id}")
    private String clientId;
    @Value("${github.client_secret}")
    private  String clientSecret;
    @Value("${github.redirect_uri}")
    private  String redirectUrl;
    @Value("${qq.client_id}")
    private String qqClientId;
    @Value("${qq.client_secret}")
    private String qqClientSecret;
    @Value("${qq.redirct_url}")
    private String qqRedirectUrl;
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setCode(code);
        accesstokenDTO.setClient_id(clientId);
        accesstokenDTO.setClient_secret(clientSecret);
        accesstokenDTO.setRedirect_uri(redirectUrl);
        accesstokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accesstokenDTO);
        System.out.println(accessToken);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser !=null)
        {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));
            request.getSession().setAttribute("user", githubUser);
            return "redirect:/";
            // 登录成功，写cookie与session
        }
        else {
            log.error("callback get gihub error {}",githubUser);
            //登录失败，重新登录
            return "redirect:/";
        }
    }
    @GetMapping("/qqcallback")
    public String qqcallback(@RequestParam(name="code") String code,
                             @RequestParam(name="state") String state,
                             HttpServletRequest request,
                             HttpServletResponse response){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setCode(code);
        accesstokenDTO.setClient_id(qqClientId);
        accesstokenDTO.setClient_secret(qqClientSecret);
        accesstokenDTO.setRedirect_uri(qqRedirectUrl);
        accesstokenDTO.setState(state);
        String accessToken = qqProvider.getAccessToken(accesstokenDTO);
        System.out.println(accessToken);
        String openId = qqProvider.getOpenId(accessToken);
        qqUserDTO qqUserDTO = qqProvider.getUser(openId,accesstokenDTO.getClient_id(),accessToken);
        System.out.println(qqUserDTO.toString());
        if(qqUserDTO !=null)
        {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(qqUserDTO.getNickname());
            user.setAccountId(openId);
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(qqUserDTO.getFigureurl());
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));
            request.getSession().setAttribute("user", user);
            return "redirect:/";
            // 登录成功，写cookie与session
        }
        return "redirect:/";

    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response)
    {
        //删除cookies操作
        //先删除给前端的user对象
        request.getSession().removeAttribute("user");
        //给现有的cookies注入一个新的值null
        Cookie cookie = new Cookie("token",null);
        //设置立即删除
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
