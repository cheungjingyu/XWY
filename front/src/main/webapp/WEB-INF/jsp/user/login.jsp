<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include  file="/WEB-INF/jsp/header.jsp"%>
    <title>登录</title>
</head>
<body>
    <%@include file="/WEB-INF/jsp/nav.jsp" %>
    
    <div id="mainDiv"  class="container" style="margin-top: 35px;">
        <style type="text/css">
            .row {
                padding: 10px;
                font-size: 16px;
            }
            .row div {
                padding-left: 5px;
            }
        </style>
        <form class="form-horizontal" action="<%=ctxPath%>/user/login.do" method="post">
            <div class="form-group">
                <label class="col-md-3 control-label">登录名</label>
                <div class="col-md-6">
                    <input name="loginName" type="text" class="form-control" placeholder="邮箱或者手机号" value="${param.loginName}"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">密码</label>
                <div class="col-md-6">
                    <input name="password" class="form-control" type="password" value="${param.password}"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">验证码 </label>
                <div class="col-md-6">
                    <div class="col-md-5" style="padding-left:0px;padding-right:0px;">
                        <input type="text" name="imageCode" id="imageCode" class="form-control" value="${param.imageCode}"/>
                    </div>
                    <div class="col-md-4 text-center" style="padding-left:0px;padding-right:0px;">
                        <img id="imgCode" src="<%=ctxPath%>/imageCode.do?q=<%=System.currentTimeMillis()%>" onclick="this.src='<%=ctxPath%>/imageCode.do?t='+new Date().getTime()" />
                    </div>
                    <div class="col-md-3" style="padding-left:0px;padding-right:0px;">
                        <a class="btn-link" onclick="$('#imgCode').attr('src','<%=ctxPath%>/imageCode.do?t='+new Date().getTime())">看不清？</a>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-offset-3 col-md-6" style="color:red">${message}</div>
            </div>
            <div class="form-group">
                <div class="col-md-offset-3 col-md-6">
                    <input class="btn btn-success" type="submit" value="登录" />
                    <a class="btn btn-link" href="<%=ctxPath%>/user/register.do">免费注册</a>
                    <a class="btn btn-link" href="<%=ctxPath%>/user/passwordRetrieve.do">忘记密码？</a>
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-offset-3 col-md-9"><span class="glyphicon glyphicon-question-sign"></span>登录、注册遇到问题？<a href="#" target="_blank">点击此处联系客服</a></div>
            </div>
            <script type="text/javascript">
            	$(function(){
            		$("form").validate({
            			rules:{
            				"loginName":{
            					required:true,
            					pattern:"^1[34578]\\d{9}$|^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
            					},
            				"password":{required:true},
            				"imageCode":{required:true}
            			},
            			messages:{
            				"loginName":{
            					required:"登录名不能为空",
            					pattern:"请输入正确的邮箱或者手机号格式"
            					},
            				"password":{required:"密码不能为空"},
            				"imageCode":{required:"验证码不能为空"}
            			}
            		});
            	});
            	
            
            </script>
        </form>
    </div>
   <%@include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>