package life.gerayking.community.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDTO {
    //标签分类
    private String categoryName;
    //标签分类中的内容
    private List<String> tags;
}
