<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/header.jsp" %>
<title>学习卡排序</title>
</head>
<body>
<script type="text/javascript">

    function saveOrders(form){
        $("tr",$(form)).each(function(index,trDom){
            if(index>0){//忽略表头行
                $("[name=seqNums]",$(trDom)).val(index);    
            }
        });
        ajaxSubmitForm(form);
    }
    function  selectSubject(selectDom){
    	location.href="<%=ctxPath%>/cardSubject/order.do?subjectId="+selectDom.value+"&time="+new Date();
    }
</script>
    <div class="pd-20">
        <form action="<%=ctxPath%>/cardSubject/order.do" onsubmit="saveOrders(this)">
            <select name="subjectId" onchange="selectSubject(this)" style="font-size: 20px;margin-bottom: 10px;">
                   <c:forEach items="${subjectList}" var="subject">
                      <option value="${subject.id}" 
                      <c:if test="${subject.id==subjectId}">selected="selected"</c:if>
                      >${subject.name}</option>
                    </c:forEach>
                    
            </select>
            <table class="table table-border table-bordered table-bg table-hover">
                <thead>
                    <tr>
                        <th>原序号</th>
                        <th>学习卡名称</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${cardSubjectList}" var="cardSubject">
                   <c:forEach items="${cardList}" var="card">
                   	<c:if test="${cardSubject.cardId==card.id}">
                    <tr id="tr${cardSubject.id}">
                        <input type="hidden" name="cardSubjectIds" value="${cardSubject.id}" />
                        <input type="hidden" name="seqNums" />
                        <td>
                            <a class="btn size-MINI" onclick="$('#tr${cardSubject.id}').insertBefore($('#tr${cardSubject.id}').prev());"><i class="Hui-iconfont"></i></a>
                            <span style="margin:0 10px;">${cardSubject.seqNum}</span>
                            <a class="btn size-MINI" onclick="$('#tr${cardSubject.id}').insertAfter($('#tr${cardSubject.id}').next());"><i class="Hui-iconfont"></i></a>
                        </td>
                        <td>${card.name}</td>
                    </tr>
                    </c:if>
                    </c:forEach>
                </c:forEach>
                </tbody>
            </table>
            <br>
            <input class="btn btn-primary radius" type="submit" value="保存顺序" />
        </form>
    </div>

</body>
</html>