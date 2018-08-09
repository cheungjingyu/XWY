package com.rupeng.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.pojo.AdminUser;
import com.rupeng.util.CommonUtils;
@Service
public class AdminUserService extends BaseService<AdminUser> {
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private AdminUserRoleService adminUserRoleService;
/**
 * 在中间表添加该用户拥有的角色
 * 需要事物的支持
 * @param adminUser
 * @param roleIds
 */
	public void insert(AdminUser adminUser, Long[] roleIds) {
		// TODO Auto-generated method stub
		//执行添加操作
				//密码盐
				String passwordSalt=UUID.randomUUID().toString();
				String password=CommonUtils.calculateMD5(passwordSalt+adminUser.getPassword());
				adminUser.setPassword(password);
				adminUser.setPasswordSalt(passwordSalt);
				adminUserService.insert(adminUser);
				AdminUser param=adminUserService.selectOne(adminUser);
				adminUserRoleService.updateFirst(param.getId(), roleIds);
	}
	/**
	 *  进行登录判断
	 * 数据库有返回一个对象，没有返回null
	 * @param account
	 * @param password
	 * @return
	 */
		public AdminUser checkLogin(String account,String password) {
			// TODO Auto-generated method stub
			AdminUser adminUser=new AdminUser();
			adminUser.setAccount(account);
			adminUser=adminUserService.selectOne(adminUser);
			if(adminUser!=null){
				//进行密码的判断
				if(adminUser.getPassword().equalsIgnoreCase(CommonUtils.calculateMD5(adminUser.getPasswordSalt()+password))){
					return adminUser;
				}
			}
			return null;
		}
}
