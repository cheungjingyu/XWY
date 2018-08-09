package com.rupeng.web.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 学习卡管理
 * @author Bright
 *
 */
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Subject;
import com.rupeng.service.CardService;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
@Controller
@RequestMapping("/card")
public class CardController {
	@Autowired
	private CardService cardService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private CardSubjectService cardSubjectService;
	/**
	 * 获得学习卡列表
	 * @return
	 */
	@RequestMapping("/list.do")
	public ModelAndView list(){
		return new ModelAndView("card/list","cardList",cardService.selectList());
	}
	/**
	 * 获取一个添加学习卡页面，并且获取所有的学科
	 * @return
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(){
		return new ModelAndView("card/add","subjectList",subjectService.selectList());
	}
	/**
	 * 添加一个学习卡提交处理
	 * @param name
	 * @param description
	 * @param courseware
	 * @param subjectIds
	 * @return
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(String name,String description,String courseware,Long[] subjectIds){
		//非空判断
		if(CommonUtils.isEmpty(name) || CommonUtils.isEmpty(description) || CommonUtils.isEmpty(courseware)){
			return AjaxResult.errorInstance("学习卡名字或者学习卡描述或者学习卡课件下载地址不能为空");
		}
		//根据name进行唯一性判断
		Card card=new Card();
		card.setName(name);
		if(cardService.isExisted(card)){
			return AjaxResult.errorInstance("该学习卡的名字已经存在，请重新输入");
		}
		//执行添加操作,需要事物的支持
		card.setCourseware(courseware);
		card.setDescription(description);
		card.setCreateTime(new Date());
		 cardService.insert(card,subjectIds);
		return AjaxResult.successInstance("添加成功");
	}
	/**
	 * 根据id，获得一个学习卡对象，由于修改，并且获取所有的学科
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView update(Long id){
		ModelAndView modelAndView = new ModelAndView("card/update");
		Card card =cardService.selectOne(id);
        List<Subject>	subjectList = subjectService.selectList();
        CardSubject cardSubject = new CardSubject();
        cardSubject.setCardId(id);
        List<CardSubject> cardSubjectList = cardSubjectService.selectList(cardSubject);
        
        modelAndView.addObject("card", card);
        modelAndView.addObject("subjectList", subjectList);
        modelAndView.addObject("cardSubjectList", cardSubjectList);
		return modelAndView;
	}
	/**
	 * 修改提交
	 * @param id
	 * @param name
	 * @param description
	 * @param courseware
	 * @param subjectIds
	 * @return
	 */
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Long id,String name,String description,String courseware,Long[] subjectIds){
		//非空判断
		if(CommonUtils.isEmpty(name) || CommonUtils.isEmpty(description) || CommonUtils.isEmpty(courseware)){
		}
		//根据name进行数据库唯一性判断
		Card card = new Card();
		card.setName(name);
		card=cardService.selectOne(card);
		if(card!=null && card.getId()!=id){
			return AjaxResult.errorInstance("该学习卡名称已经存在，请重新输入");
		}
		//执行修改操作
		card.setId(id);
		card.setCourseware(courseware);
		card.setName(name);
		card.setDescription(description);
		cardService.update(card,subjectIds);
		return AjaxResult.successInstance("修改成功");
	}
	/**
	 * 根据id删除一个card，同时也删除中间表的关联关系
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		cardService.delete(id);
		cardSubjectService.deleteByFirstId(id);
		return AjaxResult.successInstance("删除成功");
	}
	
	
	
}
