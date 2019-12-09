package life.gerayking.community.community.service;

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
    public List<QuestionDTO> list() {
        List<Question>questions = questionMapper.list();
        List<QuestionDTO>questionDTOList = new ArrayList<>();
        for(Question question:questions)
        {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
