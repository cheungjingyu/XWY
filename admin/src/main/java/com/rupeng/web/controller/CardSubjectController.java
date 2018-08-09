package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping("/cardSubject")
public class CardSubjectController {

	@Autowired
	private CardService cardService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private CardSubjectService cardSubjectService;
	
	/**
	 * 获取学习卡排序页面
	 * @param subjectId
	 * @return
	 */
	@RequestMapping(value="/order.do",method=RequestMethod.GET)
	public ModelAndView orderPage(Long subjectId){
		List<Subject> subjectList = subjectService.selectList();
		if(subjectId==null && !CommonUtils.isEmpty(subjectList)){
			subjectId=subjectList.get(0).getId();
		}
		List<Card> cardList=null;
		if(subjectId!=null){
			cardList=cardSubjectService.selectFirstListBySecondId(subjectId);
		}
		List<CardSubject> cardSubjectList=null;
		
		if(subjectId!=null){
			CardSubject param = new CardSubject();
			param.setSubjectId(subjectId);
			cardSubjectList=cardSubjectService.selectList(param,"seqNum asc");
		}
		ModelAndView modelAndView = new ModelAndView("cardSubject/order");
		modelAndView.addObject("cardSubjectList", cardSubjectList);
		modelAndView.addObject("cardList", cardList);
		modelAndView.addObject("subjectList", subjectList);
		modelAndView.addObject("subjectId", subjectId);
		return modelAndView;
	}
	/**
	 * 修改学习卡顺序提交
	 * @param cardSubjectIds
	 * @param seqNums
	 * @return
	 */
	@RequestMapping(value="/order.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult orderSubmit(Long[] cardSubjectIds,Integer[] seqNums){
		cardSubjectService.update(cardSubjectIds,seqNums);
		return AjaxResult.successInstance("保存成功");
	}
	
}
