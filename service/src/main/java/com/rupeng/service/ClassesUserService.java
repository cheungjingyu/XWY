package com.rupeng.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.pojo.Classes;
import com.rupeng.pojo.ClassesUser;
import com.rupeng.pojo.User;
import com.rupeng.util.CommonUtils;

@Service
public class ClassesUserService extends ManyToManyBaseService<ClassesUser, Classes, User> {
	
//根据学生id获得本班级所有的老师
	public List<User> selectTeacherByStudentId(Long studentId) {
		//查询学生所在班级
        ClassesUser classesUser = new ClassesUser();
        classesUser.setUserId(studentId);
        classesUser = selectOne(classesUser);
        List<User> userList = selectSecondListByFirstId(classesUser.getClassesId());
		if(CommonUtils.isEmpty(userList)){
			return null;
		}
		List<User> teacherList = new ArrayList<User>();
		for (User user : userList) {
			if(user.getIsTeacher()!=null && user.getIsTeacher()){
				teacherList.add(user);
			}
		}
        return teacherList;
	}

}
