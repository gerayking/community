package life.gerayking.community.community.mapper;

import life.gerayking.community.community.model.Comment;
import life.gerayking.community.community.model.CommentExample;
import life.gerayking.community.community.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentExtMapper {
    int incCommentCount(Comment record);
}