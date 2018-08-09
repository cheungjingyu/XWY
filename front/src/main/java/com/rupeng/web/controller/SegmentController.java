package com.rupeng.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;
import com.rupeng.pojo.UserSegment;
import com.rupeng.service.CardService;
import com.rupeng.service.ChapterService;
import com.rupeng.service.SegmentService;
import com.rupeng.service.UserCardService;
import com.rupeng.service.UserSegmentService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/segment")
public class SegmentController {

	@Autowired
	private SegmentService segmentService;
	
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private CardService cardService;
	
	@Autowired
	private UserCardService userCardService;
	
	@Autowired
	private UserSegmentService userSegmentService;
	
	@RequestMapping("/detail.do")
	public ModelAndView detail(Long id,HttpServletRequest request){
		Segment segment = segmentService.selectOne(id);
		Chapter chapter = chapterService.selectOne(segment.getChapterId());
		Card card= cardService.selectOne(chapter.getCardId());
		//判断当前用户是否拥有这个学习卡，过没过期
		User user = (User)request.getSession().getAttribute("user");
		UserCard userCard = new UserCard();
		userCard.setCardId(card.getId());
		userCard.setUserId(user.getId());
		userCard=userCardService.selectOne(userCard);
		if(userCard==null || userCard.getEndTime().getTime()<System.currentTimeMillis()){
			return new ModelAndView("message","message","您还没有此张学习卡，或者这张学习已过期");
		}
		
		ModelAndView modelAndView = new ModelAndView("segment/detail");
		modelAndView.addObject("segment", segment);
		modelAndView.addObject("chapter", chapter);
		modelAndView.addObject("card", card);
		
		//规定用户没打开一次段落，就学习一次
		UserSegment userSegment = new UserSegment();
		userSegment.setCreateTime(new Date());
		userSegment.setSegmentId(id);
		userSegment.setUserId(user.getId());
		userSegmentService.insert(userSegment);
		return modelAndView;
	}
	@RequestMapping("/segmentNext.do")
	public ModelAndView segmentNext(Long currentId,HttpServletRequest request){
		//获取下一节课
		Segment segment = segmentService.selectNextSegment(currentId); 
		if(segment==null){
			return new ModelAndView("message","message","没有下一节课了");
		}
		//接着执行detail的逻辑
		return detail(segment.getId(), request);
		
	}
	
	/**
	 * 根据篇章id  获得该篇章下的所有的段落
	 * @param chapterId
	 * @return
	 */
	@RequestMapping("/list.do")
	public @ResponseBody AjaxResult list(Long chapterId){
		if(chapterId==null){
			throw new RuntimeException("篇章id不能为空");
		}
		Segment segment = new Segment();
		segment.setChapterId(chapterId);
		List<Segment> segmentList= segmentService.selectList(segment, "seqNum asc");
		return AjaxResult.successInstance(segmentList);
	}
}
