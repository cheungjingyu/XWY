package com.rupeng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rupeng.mapper.QuestionMapper;
import com.rupeng.pojo.Question;
import com.rupeng.pojo.QuestionAnswer;

@Service
public class QuestionService extends BaseService<Question> {

	@Autowired
	private QuestionAnswerService questionAnswerService;
	
	@Autowired
    private QuestionMapper questionMapper;
	
	public void adopt(Question question, QuestionAnswer questionAnswer) {
		// TODO Auto-generated method stub
		update(question);
		questionAnswerService.update(questionAnswer);
	}

	public PageInfo<Question> pageOfMyAnswered(Integer pageNum, Integer pageSize, Long userId) {
		PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.selectMyAnswered(userId);
        return new PageInfo<Question>(list);
	}
	//查询自己所在班级的所有的学生提问的还没有解决的问题
	public List<Question> selectUnresolvedQuestionByTeacherId(Long teacherId) {
		// TODO Auto-generated method stub
		return questionMapper.selectUnresolvedQuestionByTeacherId(teacherId);
	}
	 //查询参与的（提问的和回复的）还未解决的问题
	public List<Question> selectUnresolvedQuestionByStudentId(Long studentId) {
		// TODO Auto-generated method stub
		return questionMapper.selectUnresolvedQuestionByStudentId(studentId);
	}

}
