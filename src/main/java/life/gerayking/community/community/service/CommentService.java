package life.gerayking.community.community.service;

import life.gerayking.community.community.dto.CommentDTO;
import life.gerayking.community.community.enums.CommentTypeEnum;
import life.gerayking.community.community.enums.NotificationStatusEnum;
import life.gerayking.community.community.enums.NotificationTypeEnum;
import life.gerayking.community.community.exception.CustomizeErrorCode;
import life.gerayking.community.community.exception.CustomizeException;
import life.gerayking.community.community.mapper.*;
import life.gerayking.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentTator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExit(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            /*
             *回复评论
             */
            Comment dbComent = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComent == null) {
                //若数据库中查不到，抛出评论不存在异常
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            /*
            * 获取目前评论所在的问题
            * 为了之后能够创建回复的时候对其进行提醒
            * */
            Question question = questionMapper.selectByPrimaryKey(dbComent.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            /*
             * 增加评论数
             * */
            commentMapper.insert(comment);
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            /*
             * 创建通知
             * */


            createNotify(comment, dbComent.getCommentator(), commentTator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION);

        } else if (comment.getComment().equals("") || comment.getComment() == null) {
            throw new CustomizeException((CustomizeErrorCode.CONTENT_IS_EMPTY));
        } else {
            if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
                /**
                 *回复问题
                 */
                Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
                if (question == null) {
                    throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                }
                commentMapper.insertSelective(comment);
                question.setCommentCount(1);
                questionExtMapper.incCommentCount(question);
                // 回复问题
                createNotify(comment, question.getCreator(), commentTator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT);
            }
        }
    }

    // 评论，评论的对象的人的ID，发出评论人的名字，被评论的内容，评论的类型
    private void createNotify(Comment comment, Long reciver, String notifierName, String outerTitle, NotificationTypeEnum notificationType) {
        Notification notification = new Notification();
        if(reciver == comment.getCommentator()){
            return ;
        }
//        OuterId需要判断是一级评论还是二级评论，需要获取该问题的编号
        if(comment.getType() == CommentTypeEnum.QUESTION.getType()){
            notification.setOuterId(comment.getParentId());
        }else{
            notification.setOuterId(commentMapper.selectByPrimaryKey(comment.getParentId()).getParentId());
        }
        notification.setNotifier(comment.getCommentator());
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(reciver);
        notification.setOuterTitle(outerTitle);
        notification.setNotifierName(notifierName);
        notification.setType(notificationType.getType());
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargeId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转换为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment为CommentDTO
        List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOs;
    }
}
