package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.Chapter;
import com.rupeng.service.ChapterService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/chapter")
public class ChapterController {

	@Autowired
	private ChapterService chapterService;
	
	/**
	 * 根据cardID  获得所有的篇章
	 * @param cradId
	 * @return
	 */
	@RequestMapping("/list.do")
	public @ResponseBody AjaxResult list(Long cardId){
		if(cardId==null){
			throw new RuntimeException("学习卡id不能为空");
		}
		Chapter chapter = new Chapter();
		chapter.setCardId(cardId);
		List<Chapter> chapterList= chapterService.selectList(chapter, "seqNum asc");
		return AjaxResult.successInstance(chapterList);
	}
}
