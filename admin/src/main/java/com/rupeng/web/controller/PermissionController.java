package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Permission;
import com.rupeng.service.PermissionService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
/**
 * 权限的操作，增删改查
 * @author Bright
 *
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {
	@Autowired
	private PermissionService permissionService;
	/**
	 * 查看权限列表
	 * @return
	 */
	@RequestMapping("/list.do")
	public ModelAndView list(){
		List<Permission> permissionList = permissionService.selectList();
		ModelAndView modelAndView=new ModelAndView("permission/list");
		modelAndView.addObject("permissionList",permissionList);
		return modelAndView;
	}
	/**
	 * get方式获取添加权限页面
	 * @return
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(){
		return new ModelAndView("permission/add");
	}
	/**
	 * 添加权限页面提交
	 * @return
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(String path,String description){
		//判断非空
		if(CommonUtils.isEmpty(path) || CommonUtils.isEmpty(description) ){
			return AjaxResult.errorInstance("请求路径或者描述不能为空，请重新输入");
		}
		//用path（请求路径）判断数据库中是否存在
		Permission permission=new Permission();
		permission.setPath(path);
		if(permissionService.isExisted(permission)){
			return AjaxResult.errorInstance("该请求路径已存在，请重新输入");
		}
		//执行添加操作
		permission.setPath(path);
		permission.setDescription(description);
		permissionService.insert(permission);
		return AjaxResult.successInstance("添加权限成功");
	}
	/**
	 * 根据id修改一个权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView update(Long id){
		Permission permission=permissionService.selectOne(id);
		return new ModelAndView("permission/update", "permission", permission);
	}
	/**
	 * 修改一个权限提交
	 * @param id
	 * @param path
	 * @param description
	 * @return
	 */
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Long id,String path,String description){
		//判断非空
		if(CommonUtils.isEmpty(path) || CommonUtils.isEmpty(description)){
			return AjaxResult.errorInstance("请求路径或者描述不能为空，请重新输入");
		}
		//用path（请求路径）判断数据库中是否存在
		Permission permission=new Permission();
		permission.setPath(path);
		permission=permissionService.selectOne(permission);
		if(permission!=null&& permission.getId()!=id) {
			return AjaxResult.errorInstance("该请求路径已存在，请重新输入");
		}
		//执行更新操作
		permission.setPath(path);
		permission.setDescription(description);
		permissionService.update(permission);
		return AjaxResult.successInstance("修改权限成功");
	}
	/**
	 * 根据id删除一个权限
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		permissionService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
	
}
