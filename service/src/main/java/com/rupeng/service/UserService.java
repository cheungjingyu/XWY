package com.rupeng.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rupeng.mapper.UserMapper;
import com.rupeng.pojo.User;
import com.rupeng.util.CommonUtils;

@Service
public class UserService extends BaseService<User> {

	@Autowired
	private UserService userService;
	@Autowired
	private UserMapper mapper;
	public PageInfo<User> search(Integer pageNum,Integer pageSize, Map<String, Object> params) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<User> userList = mapper.search(params);
		return new PageInfo<User>(userList);
	}
	//检查登录
	public User login(String loginName, String password) {
		// TODO Auto-generated method stub
		User user = new User();
		if(CommonUtils.isEmail(loginName)){
			user.setEmail(loginName);
		}
		if(CommonUtils.isPhone(loginName)){
			user.setPhone(loginName);
		}
		user=userService.selectOne(user);
		//检查有户名
		if(user==null){
			return null;
		}
		//检查密码
		if(!user.getPassword().equalsIgnoreCase(CommonUtils.calculateMD5(user.getPasswordSalt()+password))){
			return null;
		}
		return user;
	}

}
