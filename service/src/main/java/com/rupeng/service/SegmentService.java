package com.rupeng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Segment;
import com.rupeng.util.CommonUtils;

@Service
public class SegmentService extends BaseService<Segment> {
	
	@Autowired
	private ChapterService chapterService;
/**
 * 得到下一节课
 * @param currentId
 * @return
 */
	public Segment selectNextSegment(Long currentId) {
		 //获取当前课程
        Segment currentSegment = selectOne(currentId);

        //获取chapter的下一节课
        Segment segment = new Segment();
        segment.setChapterId(currentSegment.getChapterId());
        //当前chapter的所有segment，降序排列
        List<Segment> segmentList = selectList(segment, "seqNum desc");

        Segment nextSegment = null;
        for (Segment segment2 : segmentList) {
            if (segment2.getId() == currentId) {
                break;
            } else {
                nextSegment = segment2;
            }
        }
        if (nextSegment != null) {
            return nextSegment;
        }
        //如果nextSegment为null说明currentSegment是本chapter最后一节课
        //那么继续寻找本学习卡的下一个chapter
        Chapter nextChapter = null;
        Long currentChapterId = currentSegment.getChapterId();

        //有的chapter可能会没有segment，所以使用循环，直到找到一个segment，或者到此学习卡末尾
        while ((nextChapter = chapterService.selectNextChapter(currentChapterId)) != null) {
            currentChapterId = nextChapter.getId();
            //查询nextChapter的第一个segment
            Segment segmentParams = new Segment();
            segmentParams.setChapterId(nextChapter.getId());
            PageInfo<Segment> pageInfo = page(1, 1, segmentParams, "seqNum asc");

            if (!CommonUtils.isEmpty(pageInfo.getList())) {
                return pageInfo.getList().get(0);
            }
        }
        return null;
	}

}
