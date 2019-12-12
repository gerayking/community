package life.gerayking.community.community.mapper;

import life.gerayking.community.community.dto.QuestionDTO;
import life.gerayking.community.community.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag,creator_id) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag},#{creatorId})")
    void create(Question question);
    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);
    @Select("select count(1) from question")
    Integer count();
    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "userId") String userId, @Param(value = "offset") Integer offset,@Param(value = "size") Integer size);
    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param("userId")String userId);
    @Select(("select * from question where id=#{id}"))
    Question getById(@Param("id")Integer id);

    @Update("Update question set title = #{title} ,description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id=#{id}")
    void update(Question question);
}
