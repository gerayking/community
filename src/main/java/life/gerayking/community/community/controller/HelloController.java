package life.gerayking.community.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
Creat by coderinker on 2019/12/2
* */
@Controller
public class HelloController {
    @GetMapping("/")
    public String index(){return "index";}

}
