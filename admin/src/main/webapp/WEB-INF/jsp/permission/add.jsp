<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/header.jsp" %>
<title>添加权限</title>
</head>
<body>
<div class="pd-20">
    <form action="<%=ctxPath%>/permission/add.do" method="post" class="form form-horizontal">
        <div class="row cl">
            <label class="form-label col-2">请求路径</label>
            <div class="formControls col-5">
                <input type="text" class="input-text"  name="path" />
            </div>
            <div class="col-5"></div>
        </div>
        <div class="row cl">
            <label class="form-label col-2">描述</label>
            <div class="formControls col-5">
                <input type="text" class="input-text"  name="description" />
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
    				"path":{required:true},
    				"description":{required:true}
    					},
    			messages:{
    				"path":{required:"请求路径不能为空！"
    					},
    					"description":{required:"描述不能为空！"}
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