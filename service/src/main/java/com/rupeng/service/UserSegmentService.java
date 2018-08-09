package com.rupeng.service;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserSegment;
import com.rupeng.util.CommonUtils;

@Service
public class UserSegmentService extends ManyToManyBaseService<UserSegment, User, Segment> {
/**
 * 得到指定用户最后一次学习记录
 * @param userId
 * @return
 */
	public Long selectLatestSegmentId(Long userId) {
		UserSegment userSegment = new UserSegment();
		userSegment.setUserId(userId);
		PageInfo<UserSegment> pageInfo = page(1, 1, userSegment, "createTime desc");
		if(CommonUtils.isEmpty(pageInfo.getList())){
			return null;
		}
		return pageInfo.getList().get(0).getSegmentId();
	}
	/**
	 * 根据用户id获得最后一次观看记录的UserSegment对象
	 * @param userId
	 * @return
	 */
	public UserSegment selectOneLatestUserSegment(Long userId) {
		UserSegment userSegment = new UserSegment();
		userSegment.setUserId(userId);
		PageInfo<UserSegment> pageInfo = page(1, 1, userSegment, "createTime desc");
		if(CommonUtils.isEmpty(pageInfo.getList())){
			return null;
		}
		return pageInfo.getList().get(0);
	}

}
