package com.rupeng.web.resolver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.util.AjaxResult;
import com.rupeng.util.JsonUtils;
/**
 * 自定义错误页面
 * @author Bright
 *
 */
@Component
public class RupengHandlerExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		//判断是不是ajax请求
		
		if(request.getHeader("X-Requested-With")!=null){
			//ajax请求
			AjaxResult ajaxResult=AjaxResult.errorInstance("服务器出错了");
			try {
				response.getWriter().println(JsonUtils.toJson(ajaxResult));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView();
			//普通请求
		}else{
			return new ModelAndView("500");
		}
	}

}
