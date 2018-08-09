package com.rupeng.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.User;
import com.rupeng.service.SettingService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.EmailUtils;
import com.rupeng.util.ImageCodeUtils;
import com.rupeng.util.JedisUtils;
import com.rupeng.util.JsonUtils;
import com.rupeng.util.SMSUtils;

@Controller
public class OtherController {
	@Autowired
	private SettingService settingService;

	@RequestMapping("/")
	public ModelAndView index(){
		return new ModelAndView("index");
	}
	@RequestMapping("/imageCode.do")
	public void imageCode(HttpServletRequest request,HttpServletResponse response){
		ImageCodeUtils.sendImageCode(request.getSession(), response);
	}
	@RequestMapping("/smsCode.do")
	public @ResponseBody AjaxResult smsCode(String phone,HttpServletRequest request){
		if(CommonUtils.isEmpty(phone))
		{
			return AjaxResult.errorInstance("手机号不能为空");
		}
		if(!CommonUtils.isPhone(phone)){
			return AjaxResult.errorInstance("请输入正确的手机号");
		}
		String url = settingService.getValueByName("sms_url");
		String username = settingService.getValueByName("sms_username");
		String appKey = settingService.getValueByName("sms_app_key");
		String template = settingService.getValueByName("sms_code_template");
		SMSUtils.sendSMSCode(request.getSession(), url, username, appKey, template, phone);
		return AjaxResult.successInstance("发送成功");
	}
	@RequestMapping("/emailCode.do")
	public @ResponseBody AjaxResult emailCode(String email,HttpServletRequest request){
		if(CommonUtils.isEmpty(email))
		{
			return AjaxResult.errorInstance("邮箱不能为空");
		}
		if(!CommonUtils.isEmail(email)){
			return AjaxResult.errorInstance("请输入正确的邮箱");
		}
		String smtpServer = settingService.getValueByName("email_smtp_host");
		String username = settingService.getValueByName("email_username");
		String password = settingService.getValueByName("email_password");
		String from = settingService.getValueByName("email_from");
		EmailUtils.sendEmailCode(request.getSession(), smtpServer, username, password, from, email);
		return AjaxResult.successInstance("发送成功");
	}
	@RequestMapping("/notification.do")
	public @ResponseBody AjaxResult notification(HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("user");
		if(user==null){
			return AjaxResult.errorInstance("您还没有登录");
		}
		//获得当前登录用户所有的通知消息，并且把通知消息从redis里面删除掉
		Set<String> datas=JedisUtils.smembersAndDel("notification_"+user.getId());
		if(CommonUtils.isEmpty(datas)){
			return AjaxResult.successInstance(null);
		}
		//把json格式转换成bean对象
		List<Object> list = new ArrayList<Object>();
		for(String string:datas){
			Object obj = JsonUtils.toBean(string, Object.class);
			list.add(obj);
		}
		return AjaxResult.successInstance(list);
	}
}
