package life.gerayking.community.community.service;

import life.gerayking.community.community.dto.PaginationDTO;
import life.gerayking.community.community.dto.QuestionDTO;
import life.gerayking.community.community.mapper.QuestionMapper;
import life.gerayking.community.community.mapper.UserMapper;
import life.gerayking.community.community.model.Question;
import life.gerayking.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    public PaginationDTO list(Integer page, Integer size) {

        Integer totalCount = questionMapper.count();
        Integer totalPage=totalCount%size==0?totalCount/size:totalCount/size+1;
        page=Math.min(page,totalPage);
        page=Math.max(page,1);
        Integer offset = size * (page-1);
        PaginationDTO paginationDTO = new PaginationDTO();
        List<Question>questions = questionMapper.list(offset,size);
        List<QuestionDTO>questionDTOList = new ArrayList<>();
        for(Question question:questions)
        {
            User user = userMapper.findById(question.getCreatorId());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        Integer totalcount = questionMapper.count();
        paginationDTO.setPagination(totalcount,page,size);
        return paginationDTO;
    }

    public PaginationDTO list(String userId, Integer page, Integer size) {
        Integer totalCount = questionMapper.countByUserId(userId);
        Integer totalPage=totalCount%size==0?totalCount/size:totalCount/size+1;
        page=Math.min(page,totalPage);
        page=Math.max(page,1);
        Integer offset = size * (page-1);
        PaginationDTO paginationDTO = new PaginationDTO();
        List<Question>questions = questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO>questionDTOList = new ArrayList<>();
        for(Question question:questions)
        {
            User user = userMapper.findById(question.getCreatorId());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        paginationDTO.setPage(page);
        Integer totalcount = questionMapper.count();
        paginationDTO.setPagination(totalcount,page,size);
        return paginationDTO;
    }
}
