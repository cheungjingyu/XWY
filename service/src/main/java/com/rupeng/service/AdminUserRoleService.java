package com.rupeng.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.mapper.AdminUserRoleMapper;
import com.rupeng.pojo.AdminUser;
import com.rupeng.pojo.AdminUserRole;
import com.rupeng.pojo.Role;
@Service
public class AdminUserRoleService extends ManyToManyBaseService<AdminUserRole, AdminUser, Role> {
	@Autowired
	private AdminUserRoleMapper adminUserRoleMapper;
/**
 * 检查该用户是否拥有该权限
 * @param id
 * @param servletPath
 * @return
 */
	public boolean checkPermission(Map map) {
		// TODO Auto-generated method stub
		int count = adminUserRoleMapper.checkPermission(map);
		return count > 0;
	}

}
