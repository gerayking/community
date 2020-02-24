package life.gerayking.community.community.controller;

import life.gerayking.community.community.dto.CommentDTO;
import life.gerayking.community.community.dto.ResultDTO;
import life.gerayking.community.community.exception.CustomizeErrorCode;
import life.gerayking.community.community.mapper.CommentMapper;
import life.gerayking.community.community.model.Comment;
import life.gerayking.community.community.model.User;
import life.gerayking.community.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request)//RequsetBody可序列化对象，接收json对象
    {
        User user = (User) request.getSession().getAttribute("user");
        if(user==null)
        {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setComment(commentDTO.getComment());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(1);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }
}
