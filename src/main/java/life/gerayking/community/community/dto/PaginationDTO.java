package life.gerayking.community.community.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages;
    private Integer endPage;
    public void setPagination(Integer totalcount, Integer page, Integer size){
        pages = new LinkedList<>();
        Integer totalPage = 0;
        totalPage=totalcount%size==0?totalcount/size:totalcount/size+1;
        //当前展示的页面
        for(int i=Math.max(page-3,1);i<=Math.min(page+3,totalPage);i++)
        {
            pages.add(Integer.valueOf(i));
        }
        //是否展示前一页
        showPrevious= page != 1;
        //是否占式下一页
        showNext = page!=totalPage;
        //是否占式第一页
        showFirstPage= !pages.contains(1);
        //是否展示最后一页
        showEndPage = !pages.contains(totalPage);
        endPage=totalPage;
        this.page=page;
    }
}
