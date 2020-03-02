package life.gerayking.community.community.mapper;

import life.gerayking.community.community.dto.QuestionQueryDTO;
import life.gerayking.community.community.model.Question;
import life.gerayking.community.community.model.QuestionExample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}