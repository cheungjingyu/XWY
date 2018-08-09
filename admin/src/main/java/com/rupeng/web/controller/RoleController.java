package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Permission;
import com.rupeng.pojo.Role;
import com.rupeng.service.PermissionService;
import com.rupeng.service.RolePermissionService;
import com.rupeng.service.RoleService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

/**
 * 角色操作
 * @author Bright
 *
 */
@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RolePermissionService rolePermissionService;
	/**
	 * 获取角色列表
	 * @return
	 */
	@RequestMapping("/list.do")
	public ModelAndView list(){
		List<Role> roleList = roleService.selectList();
		return new ModelAndView("role/list","roleList",roleList);
	}
	/**
	 * 获取添加页面
	 * @return
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(){
		List<Permission> permissionList = permissionService.selectList();
		return new ModelAndView("role/add","permissionList",permissionList);
	}
	/**
	 * 添加角色提交
	 * @param name
	 * @param description
	 * @param permissionIds
	 * @return
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(String name,String description,Long[] permissionIds){
		if(CommonUtils.isEmpty(name) || CommonUtils.isEmpty(description)){
			return AjaxResult.errorInstance("角色名或者描述不能为空，请重新输入");
		}
		//根据name进行唯一性检查
		Role role=new Role();
		role.setName(name);
		if(roleService.isExisted(role)){
			return AjaxResult.errorInstance("该角色名已存在，请重新输入");
		}
		//执行添加操作，不仅在role表中添加，也要在中间表添加 关联关系
		//需要事物的支持，在同一个service中进行操作
		role.setDescription(description);
		roleService.insert(role,permissionIds);
		return AjaxResult.successInstance("添加成功");
	}
	/**
	 * 根据id修改角色
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView update(Long id){
		//查到角色信息
		Role role = roleService.selectOne(id);
		/*//查到该角色拥有的权限
		rolePermissionService.selectSecondListByFirstId(id);
		//查到所有的权限
		List<Permission> permissionList = permissionService.selectList();*/
		ModelAndView modelAndView=new ModelAndView("role/update");
		modelAndView.addObject("role",role);
		//modelAndView.addObject("permissionList",permissionList);
		return modelAndView;
	}
	/**
	 * 修改角色提交
	 * @param id
	 * @param name
	 * @param description
	 * @return
	 */
	 @RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult update(Long id,String name,String description){
		//非空判断
		if(CommonUtils.isEmpty(name) || CommonUtils.isEmpty(description)){
			return AjaxResult.errorInstance("角色名或者描述不能为空，请重新输入");
		}
		//根据name（角色名）判断角色的唯一性
		Role role=new Role();
		role.setName(name);
		role=roleService.selectOne(role);
		if(role!=null && role.getId()!=id){
			return AjaxResult.errorInstance("该角色名已存在，请重新输入");
		}
		//执行更新操作
		role.setDescription(description);
		roleService.update(role);
		//roleService.insert(role, permissionIds);
		return AjaxResult.successInstance("修改成功");
	}
	/**
	 * 根据id进行删除一个角色
	 * @param id
	 * @return
	 */
	 @RequestMapping(value="/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		roleService.delete(id);
		//删除中间表的关联数据
		rolePermissionService.deleteByFirstId(id);
		return AjaxResult.successInstance("删除成功");
	}
	
	
}
