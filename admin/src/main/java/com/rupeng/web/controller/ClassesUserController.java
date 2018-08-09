package com.rupeng.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.ClassesUser;
import com.rupeng.pojo.User;
import com.rupeng.service.ClassesUserService;
import com.rupeng.service.UserService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/classesUser")
public class ClassesUserController {

	@Autowired
	private ClassesUserService classesUserService;
	@Autowired
	private UserService userService;
	@RequestMapping("/update.do")
	public ModelAndView update(Long classesId){
		List<User> userList = classesUserService.selectSecondListByFirstId(classesId);
		ModelAndView modelAndView = new ModelAndView("classesUser/update");
		modelAndView.addObject("userList", userList);
		modelAndView.addObject("classesId", classesId);
		return modelAndView;
	}
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(Integer curr,String param,Long classesId){
		if(curr==null){
			curr=1;
		}
		if(param!=null){
			param="%"+param+"%";
		}
		
		Map<String,Object> params = new  HashMap<String, Object>();
		params.put("param", param);
		
		PageInfo<User> pageInfo = userService.search(curr, 10, params);
		ModelAndView modelAndView = new ModelAndView("classesUser/add"); 
		modelAndView.addObject("pageInfo", pageInfo);
		modelAndView.addObject("classesId", classesId);
		return modelAndView;
	}
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(Long classesId,Long userId){
		User user = new User();
		user=userService.selectOne(userId);
		//是老师
		if(user.getIsTeacher()){
			ClassesUser classesUser = new ClassesUser();
			classesUser.setClassesId(classesId);
			classesUser.setUserId(userId);
			if(classesUserService.isExisted(classesUser)){
				return AjaxResult.errorInstance("此老师已经在此班级里面了");
			}
			//是学生
		}else{
			ClassesUser classesUser = new ClassesUser();
			classesUser.setUserId(userId);
			if(classesUserService.isExisted(classesUser)){
				return AjaxResult.errorInstance("此学生已经在某个班级里面了");
			}
		}
		//执行添加操作
		ClassesUser classesUser = new ClassesUser();
		classesUser.setClassesId(classesId);
		classesUser.setUserId(userId);
		classesUserService.insert(classesUser);
		return AjaxResult.successInstance("添加成功");
	}
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long userId){
		classesUserService.deleteBySecondId(userId);
		return AjaxResult.successInstance("删除成功");
	}
	
}
