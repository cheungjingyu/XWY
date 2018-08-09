package com.rupeng.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Classes;
import com.rupeng.pojo.ClassesUser;
import com.rupeng.pojo.Setting;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;
import com.rupeng.util.CommonUtils;

@Service
public class UserCardService extends ManyToManyBaseService<UserCard, User, Card> {
	@Autowired
	private ClassesUserService classesUserService;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private CardSubjectService cardSubjectService;
	
	
/**
 * 向指定班级发放指定学习卡
 * @param classesId
 * @param cardId
 */
	public void activateFirstCard(Long classesId, Long cardId) {
		// TODO Auto-generated method stub
		//根据班级id查到所有的学生
		List<User> userList = classesUserService.selectSecondListByFirstId(classesId);
		if(CommonUtils.isEmpty(userList)){
			return;
		}
		
		Setting setting = new Setting();
		setting.setName("card_valid_days");
		setting=settingService.selectOne(setting);
		int ValiDays=Integer.parseInt(setting.getValue());
		//向每个学生发放学习卡
		for(User user:userList){
			//判断是否老师
			if(user.getIsTeacher()){
				continue;
			}
			UserCard userCard = new UserCard();
			userCard.setCardId(cardId);
			userCard.setUserId(user.getId());
			if(isExisted(userCard)){
				continue;
			}
			userCard.setCreateTime(new Date());
			Date endTime=new Date(userCard.getCreateTime().getTime()+1000*60*60*24*ValiDays);
			userCard.setEndTime(endTime);
			insert(userCard);
			
		}
	}
	/**
	 * 申请下一张学习卡
	 * @param id
	 * @return
	 */
	public boolean applyNextCard(Long userId) {
		// TODO Auto-generated method stub
		//查询出此用户的最近一次得到的学习卡
        UserCard latestUserCard = selectLatestCreated(userId);
		
		
      //查询出此用户所属的班级 所属的学科的所有学习卡
        Classes classes = classesUserService.selectFirstOneBySecondId(userId);

        CardSubject params = new CardSubject();
        params.setSubjectId(classes.getSubjectId());

        //按照seqNum倒序排列
        List<CardSubject> cardSubjectList = cardSubjectService.selectList(params, "seqNum desc");
		
        CardSubject nextCardSubject = null;
        for (CardSubject cardSubject2 : cardSubjectList) {
            if (cardSubject2.getCardId() == latestUserCard.getCardId()) {
                break;
            } else {
                nextCardSubject = cardSubject2;
            }
        }
        if (nextCardSubject == null) {
            return false;
        }
		
		
      //学习卡默认有效天数
        int validDays = Integer.parseInt(settingService.getValueByName("card_valid_days"));
        UserCard userCard = new UserCard();
        userCard.setCardId(nextCardSubject.getCardId());
        userCard.setUserId(userId);
        userCard.setCreateTime(new Date());
        userCard.setEndTime(new Date(System.currentTimeMillis() + validDays * 1000 * 60 * 60 * 24));

        insert(userCard);

        return true;
	}
	/**
	 * 最近一次得到的学习卡
	 * @param userId
	 * @return
	 */
	private UserCard selectLatestCreated(Long userId) {
		UserCard params = new UserCard();
        params.setUserId(userId);
        PageInfo<UserCard> pageInfo = page(1, 1, params, "createTime desc");
        if (CommonUtils.isEmpty(pageInfo.getList())) {
            return null;
        }
        return pageInfo.getList().get(0);
	}

}
