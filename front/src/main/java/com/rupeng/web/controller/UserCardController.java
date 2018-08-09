package com.rupeng.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.User;
import com.rupeng.service.UserCardService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/userCard")
public class UserCardController {

	@Autowired
	private UserCardService userCardService;
	@RequestMapping("/applyNext.do")
	public @ResponseBody AjaxResult applyNext(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		boolean result = userCardService.applyNextCard(user.getId());
		
        if (result) {
           return AjaxResult.successInstance("新学习卡已经发放"); 
        } else {
        
            return AjaxResult.errorInstance("您已申请过全部的学习卡");
        }
       
	}
}
