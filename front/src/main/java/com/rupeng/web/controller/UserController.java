package com.rupeng.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.ClassesUser;
import com.rupeng.pojo.Question;
import com.rupeng.pojo.User;
import com.rupeng.service.ClassesUserService;
import com.rupeng.service.QuestionService;
import com.rupeng.service.UserCardService;
import com.rupeng.service.UserService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.EmailUtils;
import com.rupeng.util.ImageCodeUtils;
import com.rupeng.util.SMSUtils;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ClassesUserService  classesUserService;
	@Autowired
	private UserCardService userCardService;
	@Autowired
	private QuestionService questionService;
	
	
	@RequestMapping(value="/register.do",method=RequestMethod.GET)
	public ModelAndView register(){
		return new ModelAndView("user/register");
	}
	@RequestMapping(value="/register.do",method=RequestMethod.POST)
	public ModelAndView registerSubmit(String email,String password,String emailCode,HttpServletRequest request){
		if(CommonUtils.isEmpty(email) || CommonUtils.isEmpty(password) || CommonUtils.isEmpty(emailCode)){
			return new  ModelAndView("user/register","message","邮箱或者密码或者验证码不能为空");
		}
		if(!CommonUtils.isEmail(email)){
			return new  ModelAndView("user/register","message","请输入正确的邮箱格式");
		}
		if(!CommonUtils.isLengthEnough(password, 6)){
			return new  ModelAndView("user/register","message","密码最少六位");
		}
		int result = EmailUtils.checkEmailCode(request.getSession(), email, emailCode);
		if(result==EmailUtils.CHECK_RESULT_FLASE){
			return new  ModelAndView("user/register","message","验证码错误，请重新输入！");
		}else if(result==EmailUtils.CHECK_RESULT_INVALID){
			return new  ModelAndView("user/register","message","验证码已过期，请重新发送验证码");
		}
		//邮箱的唯一性判断
		User user = new User();
		user.setEmail(email);
		if(userService.isExisted(user)){
			return new  ModelAndView("user/register","message","此邮箱已被注册");
		}
		
		user.setCreateTime(new Date());
		String salt = UUID.randomUUID().toString();
		user.setPasswordSalt(salt);
		user.setPassword(CommonUtils.calculateMD5(salt+password));
		userService.insert(user);
		return new ModelAndView("user/registerSuccess");
	}
	@RequestMapping(value="/login.do",method=RequestMethod.GET)
	public ModelAndView login(){
		return new ModelAndView("user/login");
	}
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public ModelAndView loginSubmit(String loginName,String password,String imageCode,HttpServletRequest request,HttpServletResponse response){
		//非空判断
		if(CommonUtils.isEmpty(imageCode) || CommonUtils.isEmpty(password) || CommonUtils.isEmpty(loginName)){
			return new ModelAndView("user/login","message","登录名或者密码或者验证码不能为空");
		}
		
		if(!ImageCodeUtils.checkImageCode(request.getSession(), imageCode)){
			return new ModelAndView("user/login","message","验证码不正确");
		}
		if(!CommonUtils.isEmail(loginName) && !CommonUtils.isPhone(loginName)){
			return new ModelAndView("user/login","message","邮箱或者手机号格式不正确");
		}
		User user = userService.login(loginName,password);
		if(user==null){
			return new ModelAndView("user/login","message","登录名或者密码错误");
		}
		//登录成功
		request.getSession().setAttribute("user", user);

        //添加cookie，方便自动登录
        int maxAge = 60 * 60 * 24 * 14;//14天

        Cookie loginNameCookie = new Cookie("loginName", loginName);
        loginNameCookie.setMaxAge(maxAge);
        loginNameCookie.setPath("/");
        response.addCookie(loginNameCookie);

        Cookie passwordCookie = new Cookie("password", user.getPassword());
        passwordCookie.setMaxAge(maxAge);
        passwordCookie.setPath("/");
        response.addCookie(passwordCookie);

        //重定向到首页
		return new ModelAndView("redirect:/");
	}
	@RequestMapping("/logOut.do")
	public ModelAndView logOut(HttpServletRequest request,HttpServletResponse response){
		request.getSession().invalidate();
		  //删除cookie，取消自动登录
        int maxAge = 0;//立即删除

        Cookie loginNameCookie = new Cookie("loginName", "");
        loginNameCookie.setMaxAge(maxAge);
        loginNameCookie.setPath("/");
        response.addCookie(loginNameCookie);

        Cookie passwordCookie = new Cookie("password", "");
        passwordCookie.setMaxAge(maxAge);
        passwordCookie.setPath("/");
        response.addCookie(passwordCookie);
		
		return new ModelAndView("redirect:/");
	}
	@RequestMapping("/userInfo.do")
	public ModelAndView userInfo(){
		return new ModelAndView("user/userInfo");
	}
	/**
	 * 修改个人信息
	 * @param name
	 * @param school
	 * @param isMale
	 * @param request
	 * @return
	 */
	@RequestMapping("/update.do")
	public @ResponseBody AjaxResult update(String name,String school,Boolean isMale,HttpServletRequest request){
		if(CommonUtils.isEmpty(name) || CommonUtils.isEmpty(school) || isMale==null){
			return AjaxResult.errorInstance("姓名，学校或者性别不能为空");
		}
		User oldUser = (User) request.getSession().getAttribute("user");
		oldUser.setIsMale(isMale);
		oldUser.setName(name);
		oldUser.setSchool(school);
		userService.update(oldUser);
		////更新session中的user
		request.getSession().setAttribute("user", oldUser);
		return AjaxResult.successInstance("修改成功");
	}
	@RequestMapping("/bindPhone.do")
	public @ResponseBody AjaxResult bindPhone(String phone,String smsCode,HttpServletRequest request){
		//非空判断
		if(CommonUtils.isEmpty(phone) || CommonUtils.isEmpty(smsCode) ){
			return AjaxResult.errorInstance("手机号或者验证码不能为空");
		}
		if(!CommonUtils.isPhone(phone)){
			return AjaxResult.errorInstance("请输入正确的手机号格式");
		}
		//检查验证码
		int result = SMSUtils.checkSMSCode(request.getSession(), phone, smsCode);
		if(result==SMSUtils.CHECK_RESULT_FLASE){
			return AjaxResult.errorInstance("验证码错误请重新输入");
		}else if(result==SMSUtils.CHECK_RESULT_INVALID){
			return AjaxResult.errorInstance("验证码已过期，请点击按钮重新发送");
		}
		//执行绑定操作
		User oldUser = (User)request.getSession().getAttribute("user");
		oldUser.setPhone(phone);
		userService.update(oldUser);
	//更新session中的user
			request.getSession().setAttribute("user", oldUser);
			return AjaxResult.successInstance("绑定手机号成功");
	}
	/**
	 * 修改密码
	 * @return
	 */
	@RequestMapping(value="/passwordUpdate.do",method=RequestMethod.GET)
	public ModelAndView passwordUpdate(){
		return new ModelAndView("user/passwordUpdate");
	}
	/**
	 * 修改密码提交
	 * @param password
	 * @param newpassword
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/passwordUpdate.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult passwordUpdateSubmit(String password,String newpassword,HttpServletRequest request){
		if(CommonUtils.isEmail(password) || CommonUtils.isEmail(newpassword)){
			return AjaxResult.errorInstance("旧密码或者新密码不能为空");
		}
		if(!CommonUtils.isLengthEnough(newpassword, 6)){
			return AjaxResult.errorInstance("新密码至少六位");
		}
		//检查原密码是否正确
        //从session中取出user，使用其id查询数据库，可避免session中的user信息不是最新的情况，当然如果可以保证session中的user信息总是和数据库中的一致，也可以使用session中的user
		User oldUser = (User)request.getSession().getAttribute("user");
		oldUser=userService.selectOne(oldUser.getId());
		if(!(oldUser.getPassword()).equalsIgnoreCase(CommonUtils.calculateMD5(oldUser.getPasswordSalt()+password))){
			return AjaxResult.errorInstance("旧密码不正确");
		}
 		oldUser.setPassword(CommonUtils.calculateMD5(oldUser.getPasswordSalt()+newpassword));
			userService.update(oldUser);
			//更新session中的user
			request.getSession().setAttribute("user", oldUser);
			return AjaxResult.successInstance("修改密码成功");
	}
	/**
	 * 忘记密码页面
	 * @return
	 */
	@RequestMapping(value="/passwordRetrieve.do",method=RequestMethod.GET)
	public ModelAndView passwordRetrieve(){
		return new ModelAndView("user/passwordRetrieve");
	}
	/**
	 * 忘记密码页面提交
	 * @param email
	 * @param password
	 * @param emailCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/passwordRetrieve.do",method=RequestMethod.POST)
	public ModelAndView passwordRetrieveSubmit(String email,String password,String emailCode,HttpServletRequest request){
		if(CommonUtils.isEmpty(email) || CommonUtils.isEmpty(password) || CommonUtils.isEmpty(emailCode)){
			return new ModelAndView("user/passwordRetrieve","message","邮箱，新密码，验证码不能能为空");
		}
		if(!CommonUtils.isEmail(email)){
			return new ModelAndView("user/passwordRetrieve","message","邮箱格式不正确");
		}
		if(!CommonUtils.isLengthEnough(password, 6)){
			return new ModelAndView("user/passwordRetrieve","message","密码至少六位");
		}
		//校验验证码
		int result = EmailUtils.checkEmailCode(request.getSession(), email, emailCode);
		if(result==EmailUtils.CHECK_RESULT_FLASE){
			return new ModelAndView("user/passwordRetrieve","message","验证码不正确，请重新输入");
		}else if(result==EmailUtils.CHECK_RESULT_INVALID){
			return new ModelAndView("user/passwordRetrieve","message","验证码已过期，请点击按钮重新发送");
		}
		
		User user = new User();
		user.setEmail(email);
		user=userService.selectOne(user);
		if(user==null){
			return new ModelAndView("user/passwordRetrieve","message","此用户名不存在");
		}
		user.setPassword(CommonUtils.calculateMD5(user.getPasswordSalt()+password));
		userService.update(user);
		return new ModelAndView("user/passwordRetrieveSuccess");
	}
	/**
	 * 学生学习中心
	 * @param request
	 * @return
	 */
	@RequestMapping("/studentHome.do")
	public ModelAndView studentHome(HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("user");
		ClassesUser classesUser = new ClassesUser();
		classesUser.setUserId(user.getId());
		if(!classesUserService.isExisted(classesUser)){
			return new ModelAndView("message", "message", "您还不属于任何班级");
		}
		ModelAndView modelAndView = new ModelAndView("user/studentHome");
		List<Card> cardList =  userCardService.selectSecondListByFirstId(user.getId());
		
		 //查询参与的（提问的和回复的）还未解决的问题
        List<Question> questionList = questionService.selectUnresolvedQuestionByStudentId(user.getId());
        //去重
        if (questionList != null) {
            Set<Question> set = new LinkedHashSet<>(questionList);
            questionList = new ArrayList<Question>(set);
        }
		
		modelAndView.addObject("cardList", cardList);
		modelAndView.addObject("questionList", questionList);
		return modelAndView;
	}
	@RequestMapping("/teacherHome.do")
	public ModelAndView teacherHome(HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("user");
		if(user.getIsTeacher()==null || user.getIsTeacher()==false){
			return new ModelAndView("message","message","您不是老师，无权访问");
		}
		//查询自己所在班级的所有的学生提问的还没有解决的问题
        List<Question> questionList = questionService.selectUnresolvedQuestionByTeacherId(user.getId());
		
		
		ModelAndView modelAndView = new ModelAndView("user/teacherHome");
		modelAndView.addObject("questionList", questionList);
		return modelAndView;
	}
}
