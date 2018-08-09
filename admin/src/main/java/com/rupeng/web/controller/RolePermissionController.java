package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 进行对角色拥有的权限进行管理
 * @author Bright
 *
 */
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Permission;
import com.rupeng.pojo.RolePermission;
import com.rupeng.service.PermissionService;
import com.rupeng.service.RolePermissionService;
import com.rupeng.util.AjaxResult;
@Controller
@RequestMapping("/rolePermission")
public class RolePermissionController {
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RolePermissionService rolePermissionService;
	/**
	 * 根据角色roleId获取角色更新权限页面
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView updatePage(Long roleId){
		ModelAndView modelAndView=new ModelAndView("rolePermission/update");
		RolePermission rolePermission=new RolePermission();
		rolePermission.setRoleId(roleId);
		List<RolePermission> rolePermissionList = rolePermissionService.selectList(rolePermission);
		List<Permission> permissionList = permissionService.selectList();
		modelAndView.addObject("rolePermissionList",rolePermissionList);
		modelAndView.addObject("permissionList",permissionList);
		modelAndView.addObject("roleId",roleId);
		return modelAndView;
	}
	/**
	 * 跟新某角色拥有的权限
	 * @param roleId
	 * @param permissionIds
	 * @return
	 */
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Long roleId,Long[] permissionIds){
		rolePermissionService.updateFirst(roleId, permissionIds);
		return AjaxResult.successInstance("分配权限成功");
	}

}
