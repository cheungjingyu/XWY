package com.rupeng.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;
import com.rupeng.service.CardService;
import com.rupeng.service.ChapterService;
import com.rupeng.service.SegmentService;
import com.rupeng.service.UserCardService;
import com.rupeng.service.UserSegmentService;
import com.rupeng.util.CommonUtils;

import net.sf.jsqlparser.statement.select.LateralSubSelect;

@Controller
@RequestMapping("/card")
public class CardController {

	@Autowired
	private CardService cardService;
	
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private SegmentService segmentService;
	
	@Autowired
	private UserCardService userCardService;
	
	@Autowired
	private UserSegmentService userSegmentService;
	
	
	@RequestMapping("/detail.do")
	public ModelAndView detail(Long id,HttpServletRequest request){
		//判断当前用户有没有此学习卡
		User user = (User)request.getSession().getAttribute("user");
		UserCard userCard = new UserCard();
		userCard.setUserId(user.getId());
		userCard.setCardId(id);
		userCard=userCardService.selectOne(userCard);
		//过期判断
		if(userCard==null || userCard.getEndTime().getTime()<System.currentTimeMillis()){
			return new ModelAndView("message","message","您没有这张学习，或者学习卡已过期");
		}
		//最后一次学习记录
		Long latestSegmentId = userSegmentService.selectLatestSegmentId(user.getId());
		long remainValidDays = CommonUtils.calculateApartDays(new Date(), userCard.getEndTime());
		Card card = cardService.selectOne(id);
		//查出这个学习卡所拥有的所有的篇章，以及篇章所拥有的段落
		Map<Chapter,List<Segment>> chapterSegmentListMap = cardService.selectAllCourse(card.getId());
		ModelAndView modelAndView = new ModelAndView("card/detail");
		modelAndView.addObject("card",card);
		modelAndView.addObject("remainValidDays",remainValidDays);
		modelAndView.addObject("chapterSegmentListMap",chapterSegmentListMap);
		modelAndView.addObject("latestSegmentId",latestSegmentId);
		return modelAndView;
	}
	@RequestMapping("/lastSegment.do")
	public ModelAndView lastSegment(HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("user");
		Long segmentId = userSegmentService.selectLatestSegmentId(user.getId());
		if(segmentId==null){
			return new ModelAndView("message","message","您还没有学习课程");
		}
		Segment segment =segmentService.selectOne(segmentId);
		Chapter chapter = chapterService.selectOne(segment.getChapterId());
		Card card= cardService.selectOne(chapter.getCardId());
		return detail(card.getId(), request);
	}
}
