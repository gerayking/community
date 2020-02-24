package life.gerayking.community.community.service;

import life.gerayking.community.community.enums.CommentTypeEnum;
import life.gerayking.community.community.exception.CustomizeErrorCode;
import life.gerayking.community.community.exception.CustomizeException;
import life.gerayking.community.community.mapper.CommentMapper;
import life.gerayking.community.community.mapper.QuestionExtMapper;
import life.gerayking.community.community.mapper.QuestionMapper;
import life.gerayking.community.community.model.Comment;
import life.gerayking.community.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    public void insert(Comment comment) {
        if(comment.getParentId()==null || comment.getParentId()==0){
              throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType()==null|| !CommentTypeEnum.isExit(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            Comment dbComent = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComent == null){
                //若数据库中查不到，抛出评论不存在异常
                throw  new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //回复评论
        }
        else {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            // 回复问题
        }
    }
}
