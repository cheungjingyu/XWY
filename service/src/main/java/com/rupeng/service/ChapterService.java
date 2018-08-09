package com.rupeng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rupeng.pojo.Chapter;
@Service
public class ChapterService extends BaseService<Chapter> {
/**
 * 查询下一个篇章
 * @param currentChapterId
 * @return
 */
	public Chapter selectNextChapter(Long currentChapterId) {
		 Chapter currentChapter = selectOne(currentChapterId);
	        //查询当前学习卡的所有chapter
	        Chapter params = new Chapter();
	        params.setCardId(currentChapter.getCardId());
	        List<Chapter> chapterList = selectList(params, "seqNum desc");

	        Chapter nextChapter = null;
	        for (Chapter chapter : chapterList) {
	            if (chapter.getId() == currentChapterId) {
	                break;
	            } else {
	                nextChapter = chapter;
	            }
	        }
	        return nextChapter;
	}

}
