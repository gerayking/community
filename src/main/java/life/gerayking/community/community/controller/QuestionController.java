package life.gerayking.community.community.controller;

import life.gerayking.community.community.dto.QuestionDTO;
import life.gerayking.community.community.mapper.QuestionExtMapper;
import life.gerayking.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    /**
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "/question/{id}")
    public String question(@PathVariable(value = "id")Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        //增加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }

}
