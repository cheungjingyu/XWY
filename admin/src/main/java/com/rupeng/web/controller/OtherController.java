package com.rupeng.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.AdminUser;
import com.rupeng.util.ImageCodeUtils;

@Controller
public class OtherController {
	/**
	 * 获取主页面，进行是否登录用户判断
	 * @param request
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView index(HttpServletRequest request){
		AdminUser adminUser=(AdminUser) request.getSession().getAttribute("adminUser");
		if(adminUser==null){
			return new ModelAndView("redirect:/adminUser/login.do");
		}
		return new ModelAndView("index");
	}
	@RequestMapping("/welcome.do")
	public ModelAndView welcome(){
		return new ModelAndView("welcome");
	}
	@RequestMapping("/imageCode.do")
	public void imageCode(HttpServletRequest request,HttpServletResponse response){
		ImageCodeUtils.sendImageCode(request.getSession(), response);
	}
}
