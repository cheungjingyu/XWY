package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Chapter;
import com.rupeng.service.ChapterService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
/**
 * 学习卡篇章的操作
 * @author Bright
 *
 */
@Controller
@RequestMapping("/chapter")
public class ChapterController {

	@Autowired
	private ChapterService chapterService;
	/**
	 * 根据学习卡id，获得该学习卡的所有的篇章
	 * @param cardId
	 * @return
	 */
	@RequestMapping("/list.do")
	public ModelAndView list(Long cardId){
		Chapter chapter=new Chapter();
		chapter.setCardId(cardId);
		List<Chapter> chapterList = chapterService.selectList(chapter,"seqNum asc");
		ModelAndView modelAndView = new ModelAndView("chapter/list");
		modelAndView.addObject("chapterList",chapterList);
		modelAndView.addObject("cardId",cardId);
		return modelAndView;
	}
	/**
	 * 获得添加篇章页面
	 * @param cardId
	 * @return
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(Long cardId){
		return new ModelAndView("chapter/add","cardId",cardId);
	}
	/**
	 * 添加篇章提交
	 * @param cardId
	 * @param name
	 * @param seqNum
	 * @param description
	 * @return
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(Long cardId,String name,Integer seqNum,String description){
		//非空判断
		if(CommonUtils.isEmpty(name) || CommonUtils.isEmpty(description) || seqNum==null){
			return AjaxResult.errorInstance("篇章名称或者描述或者篇章序号不能为空");
		}
		//name的唯一性判断
		Chapter chapter = new Chapter();
		chapter.setCardId(cardId);
		chapter.setName(name);
		if(chapterService.isExisted(chapter)){
			return AjaxResult.errorInstance("这个篇章名称已经存在，请重新添加");
		}
		//执行添加操作
		chapter.setDescription(description);
		chapter.setSeqNum(seqNum);
		chapterService.insert(chapter);
		return AjaxResult.successInstance("添加成功");
	}
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView update(Long id){
		return new ModelAndView("chapter/update","chapter",chapterService.selectOne(id));
	}
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Long id,String name,Integer seqNum,String description){
		//非空判断
		if(CommonUtils.isEmpty(name) || CommonUtils.isEmpty(description) || seqNum==null){
			return AjaxResult.errorInstance("篇章名称或者描述或者篇章序号不能为空");
		}
		Chapter chapter = new Chapter();
		chapter.setName(name);
		chapter=chapterService.selectOne(chapter);
		if(chapter!=null && chapter.getId()!=id){
			return AjaxResult.errorInstance("这个篇章名称已存在");
		}
		//执行更新操作
		chapter.setDescription(description);
		chapter.setName(name);
		chapter.setSeqNum(seqNum);
		chapterService.update(chapter);
		return AjaxResult.successInstance("修改成功");
	}
	/**
	 * 根据id删除一个篇章
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		chapterService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
	
}
