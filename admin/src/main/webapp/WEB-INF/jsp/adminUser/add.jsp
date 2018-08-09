<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/header.jsp" %>
<title>添加管理员账号</title>
</head>
<body>
<div class="pd-20">
    <form action="<%=ctxPath%>/adminUser/add.do" class="form form-horizontal">
    
        <div class="row cl">
            <label class="form-label col-2">管理员账号</label>
            <div class="formControls col-5">
                <input type="text" class="input-text" name="account" />
            </div>
            <div class="col-5"></div>
        </div>

        <div class="row cl">
            <label class="form-label col-2">密码</label>
            <div class="formControls col-5">
                <input type="password" class="input-text" name="password" />
            </div>
            <div class="col-5"></div>
        </div>
            
        <div class="row cl">
            <label class="form-label col-2">所属角色</label>
            <div class="formControls col-5">
            <c:forEach items="${roleList}" var="role">
                    <input id="subject${role.id}" type="checkbox" name="roleIds" value="${role.id}" /><label for="subject${role.id}" style="margin-right: 10px;">${role.name}</label>
                    </c:forEach>
            </div>
            <div class="col-5"></div>
        </div>

        <div class="row cl">
            <div class="col-9 col-offset-2">
                <input class="btn btn-primary radius" type="submit" value="添加" />
                <input class="btn btn-default radius" type="button" value="关闭" onclick="parent.location.reload()" style="margin-left: 30px;" />
            </div>
        </div>
    </form>
    <script type="text/javascript">
    	$(function(){
    		$("form").validate({
    			rules:{
    				"account":{required:true},
    				"password":{required:true,rangelength:[6,20]}
    			},
    			messages:{
    				"account":{required:"管理员账号不能为空"},
    				"password":{required:"密码不能为空",rangelength:"密码必须在6-20位之间"}
    			},
    			submitHandler:function(form){
    				ajaxSubmitForm(form,true)
    			}
    		});
    	});
    	
    
    </script>
</div>
</body>
</html>