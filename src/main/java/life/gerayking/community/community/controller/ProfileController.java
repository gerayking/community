package life.gerayking.community.community.controller;

import life.gerayking.community.community.dto.NotificationDTO;
import life.gerayking.community.community.dto.PaginationDTO;
import life.gerayking.community.community.dto.QuestionDTO;
import life.gerayking.community.community.mapper.UserMapper;
import life.gerayking.community.community.model.Notification;
import life.gerayking.community.community.model.Question;
import life.gerayking.community.community.model.User;
import life.gerayking.community.community.service.NotificationService;
import life.gerayking.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    private String action;
    private Model model;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserMapper userMapper;
    @GetMapping(value = "/profile/{action}")
    public String profile(@PathVariable String action,
                          HttpServletRequest request,
                          @RequestParam(name ="page",defaultValue = "1")Integer page,
                          @RequestParam(name ="size",defaultValue = "5")Integer size,
                          Model model)
    {
        User user =(User)request.getSession().getAttribute("user");
        Long unreadCount = notificationService.unreadCount(user.getId());
        model.addAttribute("unreadCount",unreadCount);
        if(user==null)
        {
            return "redirect:/";
        }
        if("questions".equals(action))
        {
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","问题");
            PaginationDTO paginationDTO = questionService.list(user.getId(),page,size);
            model.addAttribute("pagination",paginationDTO);
        }
        else if("replies".equals(action))
        {
           PaginationDTO paginationDTO = notificationService.list(user.getId(),page,size);
           model.addAttribute("section","replies");
           model.addAttribute("sectionName","最新回复");
           model.addAttribute("pagination",paginationDTO);
        }

        return "profile";
    }
}
