package com.rupeng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.pojo.Role;

@Service
public class RoleService extends BaseService<Role> {
	@Autowired
	private RoleService roleService;
	@Autowired
	private RolePermissionService rolePermissionService;
/**
 * 添加角色，并且更新中间表的关联关系
 * @param role
 * @param permissionIds
 */
	public void insert(Role role, Long[] permissionIds) {
		// TODO Auto-generated method stub
		//添加角色
		roleService.insert(role);
		//根据角色名查到刚刚插入的角色的id，用于在中间表插入关联关系
		role.setName(role.getName());
		Role param=roleService.selectOne(role);
		rolePermissionService.updateFirst(param.getId(), permissionIds);
	}

}
