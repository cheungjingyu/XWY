package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.AdminUserRole;
import com.rupeng.pojo.Role;
import com.rupeng.service.AdminUserRoleService;
import com.rupeng.service.RoleService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/adminUserRole")
public class AdminUserRoleController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private AdminUserRoleService adminUserRoleService;
	/**
	 * 获取给用户分配角色页面
	 * @param adminUserId
	 * @return
	 */
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView update(Long adminUserId){
		List<Role> roleList=roleService.selectList();
		AdminUserRole adminUserRole=new AdminUserRole();
		adminUserRole.setAdminUserId(adminUserId);
		List<AdminUserRole> adminUserRoleList = adminUserRoleService.selectList(adminUserRole);
		ModelAndView modelAndView = new ModelAndView("adminUserRole/update");
		modelAndView.addObject("adminUserId",adminUserId);
		modelAndView.addObject("roleList",roleList);
		modelAndView.addObject("adminUserRoleList",adminUserRoleList);
		return modelAndView;
	}
	/**
	 * 分配角色页面提交
	 * @param adminUserId
	 * @param roleIds
	 * @return
	 */
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Long adminUserId,Long[] roleIds){
		adminUserRoleService.updateFirst(adminUserId, roleIds);
		return AjaxResult.successInstance("分配角色成功");
	}
}
