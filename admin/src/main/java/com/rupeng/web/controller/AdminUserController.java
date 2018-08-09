package com.rupeng.web.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.AdminUser;
import com.rupeng.pojo.Role;
import com.rupeng.service.AdminUserRoleService;
import com.rupeng.service.AdminUserService;
import com.rupeng.service.RoleService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.ImageCodeUtils;

@Controller
@RequestMapping("/adminUser")
public class AdminUserController {
	
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private AdminUserRoleService adminUserRoleService;
	/**
	 * 获取管理员用户列表
	 * @return
	 */
	@RequestMapping("/list.do")
	public ModelAndView list(){
		List<AdminUser> adminUserList = adminUserService.selectList();
		return new ModelAndView("adminUser/list", "adminUserList", adminUserList);
	}
	/**
	 * 获取管理员添加页面
	 * @return
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(){
		List<Role> roleList = roleService.selectList();
		return new ModelAndView("adminUser/add","roleList",roleList);
	}
	/**
	 * 添加管理员提交
	 * @param account
	 * @param password
	 * @param roleIds 拥有的权限
	 * @return
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(String account,String password,Long[] roleIds){
		//非空判断
		if(CommonUtils.isEmpty(account) || CommonUtils.isEmpty(password)){
			return AjaxResult.errorInstance("用户名或者密码不能为空，请重新添加");
		}
		//账号的唯一性判断
		AdminUser adminUser = new AdminUser();
		adminUser.setAccount(account);
		if(adminUserService.isExisted(adminUser)){
			return AjaxResult.errorInstance("该用户名已存在，请重新添加！");
		}
		adminUser.setPassword(password);
		adminUserService.insert(adminUser,roleIds);
		return AjaxResult.successInstance("添加成功");
	}
	/**
	 * 根据id删除一个管理员用户
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		adminUserService.delete(id);
		adminUserRoleService.deleteByFirstId(id);
		return AjaxResult.successInstance("删除成功");
		
	}
	/**
	 * 重置密码
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/resetPassword",method=RequestMethod.GET)
	public ModelAndView resetPassword(Long id){
		AdminUser adminUser=adminUserService.selectOne(id);
		return new ModelAndView("adminUser/resetPassword", "adminUser", adminUser);
	}
	/**
	 * 重置密码提交
	 * @param id
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/resetPassword",method=RequestMethod.POST)
	public @ResponseBody AjaxResult resetPasswordSubmit(Long id,String password){
		//非空判断
		if(CommonUtils.isEmpty(password)){
			return AjaxResult.errorInstance("密码不能为空，请重新输入");
		}
		if(password.length()>20 || password.length()<6){
			return AjaxResult.errorInstance("新密码长度，应在[6-20]位之间");
	
		
			
		}
		//执行重置操作
		AdminUser adminUser= adminUserService.selectOne(id);
		adminUser.setPassword(CommonUtils.calculateMD5(adminUser.getPasswordSalt()+password));
		adminUserService.update(adminUser);
		return AjaxResult.successInstance("重置密码成功");
	}
	/**
	 * 启用。禁用账号
	 * @param id
	 * @return
	 */
	@RequestMapping("/toggleDisable.do")
	public @ResponseBody AjaxResult toggleDisable(Long id){
		AdminUser adminUser = adminUserService.selectOne(id);
		adminUser.setIsDisabled(!adminUser.getIsDisabled());
		adminUserService.update(adminUser);
		return AjaxResult.successInstance(adminUser.getIsDisabled()?"禁用成功":"启用成功");
	}
	/**
	 * 获取后台管理员登录页面
	 * @return
	 */
	@RequestMapping(value="/login.do",method=RequestMethod.GET)
	public ModelAndView login(){
		return new ModelAndView("adminUser/login");
	}
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public ModelAndView loginSubmit(String account,String password,String imageCode,HttpServletRequest request){
		//非空判断
		if(CommonUtils.isEmpty(account) || CommonUtils.isEmpty(password)){
			return new ModelAndView("adminUser/login","message","账号或者密码不能为空，请重新输入");
		}
		//验证验证码
		if(!ImageCodeUtils.checkImageCode(request.getSession(), imageCode)){
			return new ModelAndView("adminUser/login","message","验证码不正确，请重新输入");
		}
		
		AdminUser adminUser=new AdminUser();
		
		//进行数据库查询
		adminUser=adminUserService.checkLogin(account,password);
		if(adminUser==null){
			return new ModelAndView("adminUser/login","message","账号或者密码不正确，请重新输入");
		}
		//登录成功，存入session
		request.getSession().setAttribute("adminUser",adminUser);
		return new ModelAndView("redirect:/");
	}
	/**
	 * 退出并且销毁全部session
	 * @param request
	 * @return
	 */
	@RequestMapping("/logOut.do")
	public ModelAndView logOut(HttpServletRequest request){
		request.getSession().invalidate();
		return new ModelAndView("redirect:/adminUser/login.do");
	}
	/**
	 * 获取修改密码页面
	 * @return
	 */
	@RequestMapping(value="/updatePassword.do",method=RequestMethod.GET)
	public ModelAndView updatePassword(){
		return new ModelAndView("adminUser/updatePassword");
	}
	/**
	 * 修改密码页面提交
	 * @param password
	 * @param newpassword
	 * @param renewpassword
	 * @return
	 */
	@RequestMapping(value="/updatePassword.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updatePasswordSubmit(HttpServletRequest request,String password,String newpassword,String renewpassword){
		//非空判断
		if(CommonUtils.isEmpty(password) || CommonUtils.isEmpty(newpassword) || CommonUtils.isEmpty(renewpassword)){
			return AjaxResult.errorInstance("密码不能为空");
		}
		if(!newpassword.equals(renewpassword)){
			return AjaxResult.errorInstance("两次密码不一致");
		}
		if(newpassword.length()>20 || newpassword.length()<6){
			return AjaxResult.errorInstance("新密码长度，应在[6-20]位之间");
		}
		//执行更新操作
		AdminUser adminUser=(AdminUser) request.getSession().getAttribute("adminUser");
		
		if(!adminUser.getPassword().equals(CommonUtils.calculateMD5(adminUser.getPasswordSalt()+password))){
			return AjaxResult.errorInstance("旧密码不正确");
		}
		
		adminUser.setPassword(CommonUtils.calculateMD5(adminUser.getPasswordSalt()+newpassword));
		adminUserService.update(adminUser);
		return AjaxResult.successInstance("修改成功");
	}
	
}
