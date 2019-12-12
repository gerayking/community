package life.gerayking.community.community.dto;

import life.gerayking.community.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creators;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private Integer creatorId;
    private User user;
}
