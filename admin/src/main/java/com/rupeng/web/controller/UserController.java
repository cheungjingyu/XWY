package com.rupeng.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.User;
import com.rupeng.service.UserService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	/**
	 * 输入的日期格式装换
	 * @param binder
	 */
	  @InitBinder
	    protected void initBinder(WebDataBinder binder) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        //参数true表示允许日期为空（null、""）
	        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	    }
	  /**
	   * 获得前台有户列表
	   * @param curr
	   * @param beginTime
	   * @param endTime
	   * @param param
	   * @return
	   */
	@RequestMapping("/list.do")
	public ModelAndView list(Integer curr,Date beginTime,Date endTime,String param){
		if(curr==null){
			curr=1;
		}
		if(endTime!=null){
			//比如如果要查询截止到9月22注册的用户，应该查询出来9月23号零点之前的数据
            endTime.setTime(endTime.getTime() + 1000 * 60 * 60 * 24 - 1);
		}
		if(!CommonUtils.isEmpty(param)){
			param="%"+param+"%";
		}else{
			param=null;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		params.put("param", param);
		
		PageInfo<User> pageInfoList = userService.search(curr,10,params);
		return new ModelAndView("user/list","pageInfoList",pageInfoList);
	}
	/**
	 * 根据id获取一个前台用户进行编辑
	 * @param id
	 * @return
	 */

	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView update(Long id){
		return new ModelAndView("user/update","user",userService.selectOne(id));
	}
	/**
	 * 编辑提交
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(User user){
		//非空判断
		if(CommonUtils.isEmpty(user.getName()) || CommonUtils.isEmpty(user.getEmail()) || 
				CommonUtils.isEmpty(user.getSchool()) || CommonUtils.isEmpty(user.getPhone())  ){
			return AjaxResult.errorInstance("姓名，邮箱，学校，手机号不能为空");
		}
		//判断邮箱格式
		if(!CommonUtils.isEmail(user.getEmail())){
			return AjaxResult.errorInstance("请输入正确的邮箱格式");
		}
		//判断手机号格式
		if(!CommonUtils.isPhone(user.getPhone())){
			return AjaxResult.errorInstance("请输入正确的手机号");
		}
		//执行更新操作
		User oldUser= new User();
		oldUser=userService.selectOne(user.getId());
		oldUser.setEmail(user.getEmail());
		oldUser.setIsMale(user.getIsMale());
		oldUser.setName(user.getName());
		oldUser.setSchool(user.getSchool());
		oldUser.setPhone(user.getPhone());
		userService.update(oldUser);
		return AjaxResult.successInstance("修改成功");
	}
	@RequestMapping("/setTeacher.do")
	public @ResponseBody AjaxResult setTeacher(Long id,Boolean isTeacher){
		 User oldUser = userService.selectOne(id);
	        oldUser.setIsTeacher(isTeacher);
	        userService.update(oldUser);
	        return AjaxResult.successInstance(isTeacher ? "设置老师成功！" : "取消设置老师成功！");
	}
}
