package com.rupeng.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.UserCard;
import com.rupeng.util.CommonUtils;

@Service
public class CardService extends BaseService<Card> {
	@Autowired
	private CardService cardService;
	@Autowired
	private CardSubjectService cardSubjectService;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private SegmentService segmentService;
/**
 * 添加学习卡的时候，在中间表中也添加关联关系
 * @param card
 * @param subjectIds
 */
	public void insert(Card card, Long[] subjectIds) {
		// TODO Auto-generated method stub
		cardService.insert(card);
		Card param = new Card();
		param.setName(card.getName());
		param = cardService.selectOne(param);
		cardSubjectService.updateFirst(param.getId(), subjectIds);
	}
	/**
	 * 修改学习卡的时候，同步修改中间表
	 * @param card
	 * @param subjectIds
	 */
	public void update(Card card, Long[] subjectIds) {
	// TODO Auto-generated method stub
	    cardService.update(card);
	    cardSubjectService.updateFirst(card.getId(), subjectIds);
	}
	//指定学习卡所拥有的所有的篇章，以及篇章所拥有的段落
	public Map<Chapter, List<Segment>> selectAllCourse(Long id) {
		// TODO Auto-generated method stub
		
		Chapter chapter = new Chapter();
		chapter.setCardId(id);
		List<Chapter> chapterList = chapterService.selectList(chapter, "seqNum asc");
		if(CommonUtils.isEmpty(chapterList)){
			return null;
		}
		Map<Chapter,List<Segment>> listMap= new LinkedHashMap<Chapter,List<Segment>>();
		for (Chapter chapter2 : chapterList) {
			Segment segment = new Segment();
			segment.setChapterId(chapter2.getId());
			List<Segment> segmentList = segmentService.selectList(segment, "seqNum asc");
			listMap.put(chapter2, segmentList);
		}
		return listMap;
	}

}
