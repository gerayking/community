package life.gerayking.community.community.controller;

import life.gerayking.community.community.dto.PaginationDTO;
import life.gerayking.community.community.mapper.UserMapper;
import life.gerayking.community.community.model.User;
import life.gerayking.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    private String action;
    private Model model;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserMapper userMapper;
    @GetMapping(value = "/profile/{action}")
    public String profile(@PathVariable String action,
                          HttpServletRequest request,
                          @RequestParam(name ="page",defaultValue = "1")Integer page,
                          @RequestParam(name ="size",defaultValue = "2")Integer size,
                          Model model)
    {
        User user=null;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null)
            for(Cookie cookie:cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findBytoken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        if(user==null)
        {
            return "redirect:/";
        }
        if("questions".equals(action))
        {
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","问题");
            PaginationDTO paginationDTO = questionService.list(user.getAccountId(),page,size);
            model.addAttribute("paginations",paginationDTO);
        }
        else if("replies".equals(action))
        {
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        return "profile";
    }
}
