package com.rupeng.mapper;

import java.util.List;

import com.rupeng.pojo.Question;

public interface QuestionMapper extends IMapper<Question> {

	List<Question> selectMyAnswered(Long userId);
	//查询自己所在班级的所有的学生提问的还没有解决的问题
	List<Question> selectUnresolvedQuestionByTeacherId(Long teacherId);
	 //查询参与的（提问的和回复的）还未解决的问题
	List<Question> selectUnresolvedQuestionByStudentId(Long studentId);

}
