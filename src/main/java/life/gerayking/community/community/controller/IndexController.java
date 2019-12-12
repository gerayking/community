package life.gerayking.community.community.controller;

import life.gerayking.community.community.dto.PaginationDTO;
import life.gerayking.community.community.dto.QuestionDTO;
import life.gerayking.community.community.mapper.QuestionMapper;
import life.gerayking.community.community.mapper.UserMapper;
import life.gerayking.community.community.model.Question;
import life.gerayking.community.community.model.User;
import life.gerayking.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
Creat by coderinker on 2019/12/2
* */
@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionMapper questionMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name ="page",defaultValue = "1")Integer page,
                        @RequestParam(name ="size",defaultValue = "5")Integer size){

        PaginationDTO paginationDTO = questionService.list(page,size);
        List<QuestionDTO> questions = paginationDTO.getQuestions();
        for(int i=0;i<questions.size();i++)
        {
            System.out.println(questions.get(i).toString());
        }
        model.addAttribute("paginations",paginationDTO);
        return "index";
    }

}
