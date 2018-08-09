package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Classes;
import com.rupeng.pojo.Subject;
import com.rupeng.service.ClassesService;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/classes")
public class ClassesController {

	@Autowired
	private ClassesService classesService;
	@Autowired
	private SubjectService subjectService;
	@RequestMapping("/list.do")
	public ModelAndView list(){  
		List<Classes> classesList = classesService.selectList();
		List<Subject> subjectList = subjectService.selectList();
		ModelAndView modelAndView = new ModelAndView("classes/list");
		modelAndView.addObject("classesList",classesList);
		modelAndView.addObject("subjectList",subjectList);
		return modelAndView;
	}
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(){
		return new ModelAndView("classes/add", "subjectList", subjectService.selectList());
	}
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(String name,Long subjectId){
		//非空判断
		if(CommonUtils.isEmpty(name)){
			return AjaxResult.errorInstance("班级名称不能为空");
		}
		//name进行唯一性判断
		Classes classes = new Classes();
		classes.setName(name);
		if(classesService.isExisted(classes)){
			return AjaxResult.errorInstance("此班级名称已存在");
		}
		//执行添加操作
		classes.setSubjectId(subjectId);
		classesService.insert(classes);
		return AjaxResult.successInstance("添加成功");
	}
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		classesService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView update(Long id){
		List<Subject> subjectList = subjectService.selectList();
		Classes classes = classesService.selectOne(id);
		ModelAndView modelAndView = new ModelAndView("classes/update");
		modelAndView.addObject("subjectList",subjectList);
		modelAndView.addObject("classes",classes);
		return modelAndView;
	}
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Long id,String name,Long subjectId){
		//非空判断
		if(CommonUtils.isEmpty(name)){
			return AjaxResult.errorInstance("班级名称不能为空");
		}
		Classes classes = new Classes();
		classes.setName(name);
		classes=classesService.selectOne(classes);
		if(classes!=null && classes.getId()!=id){
			return AjaxResult.errorInstance("班级名称已存在");
		}
		//执行更新操作
		classes.setSubjectId(subjectId);
		classesService.update(classes);
		return AjaxResult.successInstance("修改成功");
	}
}
