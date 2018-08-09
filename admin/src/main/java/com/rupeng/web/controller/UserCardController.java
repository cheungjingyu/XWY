package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Classes;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.ClassesService;
import com.rupeng.service.UserCardService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/userCard")
public class UserCardController {

	@Autowired
	private UserCardService userCardService;
	@Autowired
	private ClassesService classesService;
	@Autowired
	private CardSubjectService cardSubjectService;
	@RequestMapping("/activateFirstCard.do")
	public @ResponseBody AjaxResult activateFirstCard(Long classesId){
		//根据班级id得到学科
		Classes classes =  classesService.selectOne(classesId);
		if(classes.getSubjectId()==null){
			return AjaxResult.errorInstance("这个班级还没有分配学科");
		}
		//根据学科得到第一张学习卡
		CardSubject cardSubject = new CardSubject();
		cardSubject.setSubjectId(classes.getSubjectId());
		List<CardSubject> cardSubjectList=cardSubjectService.selectList(cardSubject, "seqNum asc");
		if(CommonUtils.isEmpty(cardSubjectList)){
			return AjaxResult.errorInstance("这个班级所属学科还没有学习卡");
		}
		Long cardId = cardSubjectList.get(0).getCardId();
		//向此班级发放第一张学习卡
		userCardService.activateFirstCard(classesId,cardId);
		return AjaxResult.successInstance("发放成功");
	}
}
