package com.rupeng.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Classes;
import com.rupeng.pojo.Question;
import com.rupeng.pojo.QuestionAnswer;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserSegment;
import com.rupeng.service.CardService;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.ChapterService;
import com.rupeng.service.ClassesUserService;
import com.rupeng.service.QuestionAnswerService;
import com.rupeng.service.QuestionService;
import com.rupeng.service.SegmentService;
import com.rupeng.service.UserSegmentService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.JedisUtils;
import com.rupeng.util.JsonUtils;

@Controller
@RequestMapping("/question")
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UserSegmentService userSegmentService;
	
	@Autowired
	private SegmentService segmentService;
	
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private ClassesUserService classesUserService;
	
	@Autowired
	private CardService cardService;
	
	@Autowired
	private CardSubjectService cardSubjectService;
	
	@Autowired
	private QuestionAnswerService questionAnswerService;
	
	@RequestMapping(value="/ask.do",method=RequestMethod.GET)
	public ModelAndView askPage(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
        //学生所属班级、班级所属学科
        Classes classes = classesUserService.selectFirstOneBySecondId(user.getId());
        //此学科所包含的全部学习卡，按升序排序
        List<Card> cardList = cardSubjectService.selectFirstListBySecondId(classes.getSubjectId(), "seqNum asc");

        //最近一次学习的课程
        UserSegment latestUserSegment = userSegmentService.selectOneLatestUserSegment(user.getId());
        Segment latestSegment = null;
        Chapter latestChapter = null;
        List<Chapter> chapterList = null;
        List<Segment> segmentList = null;
        //如果有学习记录
        if (latestUserSegment != null) {
            latestSegment = segmentService.selectOne(latestUserSegment.getSegmentId());
            latestChapter = chapterService.selectOne(latestSegment.getChapterId());
            //当前学习卡所包含的Chapter
            Chapter chapterParams = new Chapter();
            chapterParams.setCardId(latestChapter.getCardId());
            chapterList = chapterService.selectList(chapterParams, "seqNum asc");
            //当前chapter所包含的segment
            Segment segmentParams = new Segment();
            segmentParams.setChapterId(latestChapter.getId());
            segmentList = segmentService.selectList(segmentParams, "seqNum asc");
        } else {
            Chapter chapterParams = new Chapter();
            chapterParams.setCardId(cardList.get(0).getId());
            chapterList = chapterService.selectList(chapterParams, "seqNum asc");

            //当前chapter所包含的segment
            if (!CommonUtils.isEmpty(chapterList)) {
                Segment segmentParams = new Segment();
                segmentParams.setChapterId(chapterList.get(0).getId());
                segmentList = segmentService.selectList(segmentParams, "seqNum asc");
            }
        }

        ModelAndView modelAndView = new ModelAndView("question/ask");
        modelAndView.addObject("latestSegment", latestSegment);
        modelAndView.addObject("latestChapter", latestChapter);
        modelAndView.addObject("segmentList", segmentList);
        modelAndView.addObject("chapterList", chapterList);
        modelAndView.addObject("cardList", cardList);
        return modelAndView;
	}
	
	@RequestMapping(value="/ask.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult askSubmit(Long segmentId,String errorInfo,String errorCode,String description,HttpServletRequest request){
		if(CommonUtils.isEmpty(errorInfo) && CommonUtils.isEmpty(errorCode) && CommonUtils.isEmpty(description)){
			return AjaxResult.errorInstance("报错信息,相关代码,问题描述,不能全为空");
		}
		User user = (User)request.getSession().getAttribute("user");
		Question question = new Question();
		if(segmentId!=null){
			Segment segment = segmentService.selectOne(segmentId);
			Chapter chapter = chapterService.selectOne(segment.getChapterId());
			Card card = cardService.selectOne(chapter.getCardId());
			StringBuilder builder = new StringBuilder();
			builder.append(card.getName()).append(" ").append(">> ");
			builder.append(chapter.getSeqNum()).append(" ").append(chapter.getName()).append(" >> ");
			builder.append(segment.getSeqNum()).append(" ").append(segment.getName());
			question.setCourseInfo(builder.toString());
		}
		question.setCreateTime(new Date());
		question.setDescription(description);
		question.setErrorCode(errorCode);
		question.setSegmentId(segmentId);
		question.setErrorInfo(errorInfo);
		question.setIsResolved(false);
		question.setUserId(user.getId());
		question.setUsername(user.getName());
		questionService.insert(question);
		
		//向所有老师发送通知
		List<User> tescherList = classesUserService.selectTeacherByStudentId(user.getId());
		if(!CommonUtils.isEmpty(tescherList)){
			//再把刚刚插入的查询出来，目的是获取question的id
			Question param = new Question();
			param.setUserId(user.getId());
			param=questionService.page(1, 1, param, "createTime desc").getList().get(0);
			
			Map<String,Object> notification=new HashMap<String,Object>();
			notification.put("questionId", param.getId());
			notification.put("content", "学生提问了新问题");
			for(User teacher : tescherList){
				JedisUtils.sadd("notification_"+teacher.getId(), JsonUtils.toJson(notification));
			}
		}
		return AjaxResult.successInstance("提交成功");
	}
	@RequestMapping("/list.do")
	public ModelAndView list(Integer pageNum,String condition,HttpServletRequest request){
		if(pageNum==null){
			pageNum=1;
		}
		if(CommonUtils.isEmpty(condition)){
			condition="myAsked";
		}
		User user = (User)request.getSession().getAttribute("user");
		PageInfo<Question> pageInfo = null;
		Question question = new Question();
		
		if("myAsked".equals(condition)){
			question.setUserId(user.getId());
			pageInfo= questionService.page(pageNum, 10, question, "isResolved asc,resolvedTime desc");
		}else if("allResolved".equals(condition)){
			question.setIsResolved(true);
			pageInfo= questionService.page(pageNum, 10, question, "resolvedTime desc");
		}else if("allUnresolved".equals(condition)){
			question.setIsResolved(false);
			pageInfo= questionService.page(pageNum, 10, question, "createTime desc");
		}else if("myAnswered".equals(condition)){
			question.setUserId(user.getId());
			 //我回答的问题
            pageInfo = questionService.pageOfMyAnswered(pageNum, 10, user.getId());

            //去重
            List<Question> list = pageInfo.getList();
            if (list != null) {
                Set<Question> set = new LinkedHashSet<>(list);
                pageInfo.setList(new ArrayList<Question>(set));
            }
		}
		ModelAndView modelAndView = new ModelAndView("question/list");
		modelAndView.addObject("pageInfo", pageInfo);
		modelAndView.addObject("condition", condition);
		return modelAndView;
	}
	@RequestMapping("/detail.do")
	public ModelAndView deteil(Long id){
		//答案信息
		Question question = questionService.selectOne(id);
		Classes classes = classesUserService.selectFirstOneBySecondId(question.getUserId());
	
		//得到顶层答案和直接子答案
		QuestionAnswer params = new QuestionAnswer();
		
		  params.setQuestionId(id);
	        List<QuestionAnswer> questionAnswerList = new ArrayList<>();

	        /*要做成这样的效果，需要Map<QuestionAnswer, List<QuestionAnswer>>这样的数据结构
	            一级回答1
	                    二级回答1
	                    二级回答2
	                            三级回答1
	            一级回答2
	         */
	        List<QuestionAnswer> tempList = questionAnswerService.selectList(params, "createTime desc");

	        //先找到顶层的answer
	        for (QuestionAnswer questionAnswer : tempList) {
	            if (questionAnswer.getParentId() == null) {
	                questionAnswerList.add(questionAnswer);
	            }
	        }
	        //然后找到每个answer的子级answer
	        if (tempList != null) {
	            for (QuestionAnswer questionAnswer : tempList) {
	                //再次遍历questionAnswerList，找到当前questionAnswer所有的子答案
	                List<QuestionAnswer> childList = new ArrayList<>();
	                for (QuestionAnswer child : tempList) {
	                    if (child.getParentId() == questionAnswer.getId()) {
	                        childList.add(child);
	                    }
	                }
	                questionAnswer.setChildQuestionAnswerList(childList);
	            }
	        }
		
		ModelAndView modelAndView = new ModelAndView("question/detail");
		modelAndView.addObject("question", question);
		modelAndView.addObject("classes", classes);
		modelAndView.addObject("questionAnswerList", questionAnswerList);
		return modelAndView;
	}
	@RequestMapping("/answer.do")
	public @ResponseBody AjaxResult answer(Long questionId,Long parentId,String content,HttpServletRequest request){
		if(CommonUtils.isEmpty(content)){
			return AjaxResult.errorInstance("回答内容不能为空");
		}
		User user= (User)request.getSession().getAttribute("user");
		QuestionAnswer questionAnswer = new QuestionAnswer();
		questionAnswer.setContent(content);
		questionAnswer.setCreateTime(new Date());
		questionAnswer.setParentId(parentId);
		questionAnswer.setQuestionId(questionId);
		questionAnswer.setUserId(user.getId());
		questionAnswer.setUsername(user.getName());
		
		questionAnswerService.insert(questionAnswer);
		
		//给所有参与者发送通知信息（除了回答者自己）
        //查询所有参与此问题的用户
        QuestionAnswer params = new QuestionAnswer();
        params.setQuestionId(questionAnswer.getQuestionId());

        List<QuestionAnswer> questionAnswerList = questionAnswerService.selectList(params);
        //把所有用户id放入set中去重
        Set<Long> userIds = new HashSet<Long>();
        for (QuestionAnswer questionAnswer2 : questionAnswerList) {
            userIds.add(questionAnswer2.getUserId());
        }

        //问题提问者
        Question question = questionService.selectOne(questionAnswer.getQuestionId());
        userIds.add(question.getUserId());

        Map<String, Object> notification = new HashMap<String, Object>();
        notification.put("questionId", questionAnswer.getQuestionId());

        for (Long userId : userIds) {
            if (userId == user.getId()) {
                //不通知自己
                continue;
            }
            //更人性化的通知
            if (userId == question.getUserId()) {
                notification.put("content", "您提问的问题有新回复");
            } else {
                notification.put("content", "您参与的问题有新回复");
            }
            JedisUtils.sadd("notification_" + userId, JsonUtils.toJson(notification));
        }
		
		
		
		return AjaxResult.successInstance("提交成功");
	}
	@RequestMapping("/adopt.do")
	public @ResponseBody AjaxResult adopt(Long questionAnswerId,HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		QuestionAnswer questionAnswer = questionAnswerService.selectOne(questionAnswerId);
		Question question = questionService.selectOne(questionAnswer.getQuestionId());
		
		if(!((user.getId()).equals(question.getUserId())) && (user.getIsTeacher()==null || user.getIsTeacher()==false)){
			return AjaxResult.errorInstance("您不是此问题的提问者，也不是老师，无权此操作");
		}
		
		question.setIsResolved(true);
		question.setResolvedTime(new Date());
		questionAnswer.setIsAdopted(true);
		questionService.adopt(question,questionAnswer);
		return AjaxResult.successInstance("采纳成功");
	}

}
