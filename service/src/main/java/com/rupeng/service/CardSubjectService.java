package com.rupeng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Subject;

@Service
public class CardSubjectService extends ManyToManyBaseService<CardSubject, Card, Subject> {
/**
 * 更新学习卡顺序
 * @param cardSubjectIds
 * @param seqNums
 */
	public void update(Long[] cardSubjectIds, Integer[] seqNums) {
		// TODO Auto-generated method stub
		if(cardSubjectIds!=null && seqNums!=null){
		for(int i=0;i<cardSubjectIds.length;i++){
			Long cardSubjectId=cardSubjectIds[i];
			Integer seqNum = seqNums[i];
			CardSubject cardSubject=selectOne(cardSubjectId);
			cardSubject.setSeqNum(seqNum);
			update(cardSubject);
		}
		}
	}

public List<Card> selectFirstListBySecondId(Long id, String orderBy) {
	// TODO Auto-generated method stub
	PageHelper.orderBy(orderBy);
	return selectFirstListBySecondId(id);
}

}
