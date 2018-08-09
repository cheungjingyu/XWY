package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Subject;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
/**
 * 学科的增删改查
 * @author Bright
 *
 */
@Controller
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
	private SubjectService subjectService;
    @RequestMapping(value="/add.do",method=RequestMethod.GET)
    /**
     * 获得添加页面
     * @return
     */
    public ModelAndView addPage(){
    	return new ModelAndView("subject/add");
    }
    /**
     * 添加学科提交
     * 进行name的合法性判断
     * @param name
     * @return
     */
    @RequestMapping(value="/add.do",method=RequestMethod.POST)
    public @ResponseBody AjaxResult addSubmit(String name){
    	//是否is null
    	if(CommonUtils.isEmpty(name)){
    		return AjaxResult.errorInstance("学科名称不能为空");
    	}
    	Subject subject=new Subject();
    	subject.setName(name);
    	//数据库是否存在（唯一性判断）
    	if(subjectService.isExisted(subject)){
    		return AjaxResult.errorInstance("这个学科名称已存在");
    	}
    	//执行添加逻辑
    	subjectService.insert(subject);
    	return AjaxResult.successInstance("添加学科成功");
    }
    @RequestMapping("list.do")
    public ModelAndView list(){
    List<Subject> subjectList =	subjectService.selectList();
    ModelAndView modelAndView=new ModelAndView("subject/list");
    modelAndView.addObject("subjectList",subjectList);
    return modelAndView;
    }
    @RequestMapping(value="/update.do",method=RequestMethod.GET)
    public ModelAndView update(Long id){
    	Subject subject=subjectService.selectOne(id);
    	return new ModelAndView("subject/update", "subject", subject);
    }
    @RequestMapping(value="/update.do",method=RequestMethod.POST)
    public @ResponseBody AjaxResult updateSubmit(Long id,String name){
    	//是否is null
    	if(CommonUtils.isEmpty(name)){
    		return AjaxResult.errorInstance("学科名称不能为空");
    	}
    	Subject subject=new Subject();
    	subject.setName(name);
    	subject=subjectService.selectOne(subject);
    	//数据库是否存在（唯一性判断）
    	if(subject!=null && subject.getId()!=id){
    		return AjaxResult.errorInstance("学科名称已存在");
    	}
    	//执行更新操作
    	subject=subjectService.selectOne(id);
    	subject.setName(name);
    	subjectService.update(subject);
    	return AjaxResult.successInstance("修改成功");
    }
    @RequestMapping("/delete.do")
    public @ResponseBody AjaxResult delete(Long id){
    	subjectService.delete(id);
    	return AjaxResult.successInstance("删除成功");
    }
    
}
