package life.gerayking.community.community.advice;

import com.alibaba.fastjson.JSON;
import life.gerayking.community.community.dto.ResultDTO;
import life.gerayking.community.community.exception.CustomizeErrorCode;
import life.gerayking.community.community.exception.CustomizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by codedrinker on 2019/5/28.
 */
@ControllerAdvice
@Slf4j
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model, HttpServletRequest request, HttpServletResponse response) {
        //异常处理
        String contentType = request.getContentType();//获取数据类型
        if ("application/json".equals(contentType)) {
            //如果是json数据类型
            ResultDTO resultDTO;
            //判断是否为常见错误，即CustomizeException
            if (e instanceof CustomizeException) {
                //如果是常见错误，就给返回的类型赋值。
                resultDTO = ResultDTO.errorOf((CustomizeException)e);
            } else {
                //否则就返回页面错误
                log.error("handle error", e);
                resultDTO = (ResultDTO) ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        } else {
            // 错误页面跳转
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                log.error("handle error", e);
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}