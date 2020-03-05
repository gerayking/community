package life.gerayking.community.community.controller;

import life.gerayking.community.community.dto.PaginationDTO;
import life.gerayking.community.community.dto.QuestionDTO;
import life.gerayking.community.community.dto.QuoteDTO;
import life.gerayking.community.community.mapper.QuestionMapper;
import life.gerayking.community.community.mapper.UserMapper;
import life.gerayking.community.community.service.QuestionService;
import life.gerayking.community.community.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

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
    private QuoteService quoteService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name ="page",defaultValue = "1")Integer page,
                        @RequestParam(name ="size",defaultValue = "5")Integer size,
                        @RequestParam(name ="search",required = false )String search){
        PaginationDTO<QuestionDTO> paginationDTO = questionService.list(search,page,size);
        QuoteDTO quoteDTO = quoteService.get();
        model.addAttribute("paginations",paginationDTO);
        model.addAttribute("search",search);
        model.addAttribute("quote",quoteDTO);
        return "index";
    }
}
