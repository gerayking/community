package life.gerayking.community.community.controller;

import life.gerayking.community.community.dto.CommentCreateDTO;
import life.gerayking.community.community.dto.CommentDTO;
import life.gerayking.community.community.dto.ResultDTO;
import life.gerayking.community.community.enums.CommentTypeEnum;
import life.gerayking.community.community.exception.CustomizeErrorCode;
import life.gerayking.community.community.mapper.CommentExtMapper;
import life.gerayking.community.community.model.Comment;
import life.gerayking.community.community.model.User;
import life.gerayking.community.community.service.CommentService;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentDTO,
                       HttpServletRequest request)//RequsetBody可序列化对象，接收json对象
    {
        User user = (User) request.getSession().getAttribute("user");
        if(user==null)
        {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentDTO==null|| StringUtils.isNullOrEmpty(commentDTO.getComment().trim())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setComment(commentDTO.getComment());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        commentService.insert(comment,user);
        return ResultDTO.okOf();
    }
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name ="id")Long id){
        List<CommentDTO> commentDTOS =commentService.listByTargeId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
