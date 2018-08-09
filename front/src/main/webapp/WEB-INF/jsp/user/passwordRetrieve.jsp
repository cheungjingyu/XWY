<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include  file="/WEB-INF/jsp/header.jsp"%>
    <title>找回密码</title>
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
        <form class="form-horizontal" action="<%=ctxPath%>/user/passwordRetrieve.do" method="post">
            <div class="form-group">
                <label class="col-md-3 control-label">邮箱 :</label>
                <div class="col-md-6">
                    <input id="email" name="email" type="text" class="form-control" value="${param.email}" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">新密码 :</label>
                <div class="col-md-6">
                    <input name="password" id="password" class="form-control" type="password" value="${param.password}" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">确认密码 :</label>
                <div class="col-md-6">
                    <input name="repassword" type="password" class="form-control" value="${param.repassword}" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">验证码 :</label>
                <div class="col-md-6">
                    <div class="col-md-5" style="padding-left:0px;">
                        <input type="text" name="emailCode" class="form-control" />
                    </div>
                    <div class="col-md-4 text-center">
                        <input type="button" class="btn btn-default" onclick="sendEmailCode('<%=ctxPath%>/emailCode.do',$('#email').val(),this)" value="获取邮箱验证码" />
                    </div>
                    <div class="col-md-3">${message}</div>
                </div>
            </div>
             <div class="form-group">
                <div class="col-md-offset-3 col-md-9" style="color:red"></div>
            </div>
            <div class="form-group">
                <div class="col-md-offset-3 col-md-9">
                    <input class="btn btn-success" type="submit" value="找回密码" />
                </div>
            </div>
            <script type="text/javascript">
            	$(function(){
            		$("form").validate({
            			rules:{
            				"email":{required:true,email:true},
            				"password":{required:true,minlength:6},
            				"repassword":{required:true,equalTo:"#password"},
            				"emailCode":{required:true}
            			},
            			messages:{
            				"email":{required:"邮箱不能为空",email:"邮箱格式不正确"},
            				"password":{required:"新密码不能为空",minlength:"新密码至少六位"},
            				"repassword":{required:"确认密码不能为空",equalTo:"两次密码不一致"},
            				"emailCode":{required:"验证码不能为空"}
            			}
            		});
            	});
            
            </script>
        </form>
    </div>

  <%@include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>