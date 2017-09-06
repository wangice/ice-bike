<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/common/huihead.jsp"%>
<title>591vr app管理后台</title>
</head>
<body>
	<section class="page-404 minWP text-c">
	<p class="error-title">
		<i class="Hui-iconfont va-m" style="font-size: 80px">&#xe656;</i><span
			class="va-m"> 500</span>
	</p>
	<p class="error-description">不好意思，本次访问出现异常，请联系管理员！</p>
	<p class="error-info">
		您可以：<a href="javascript:;" onclick="history.go(-1)" class="c-primary">&lt;
			返回上一步</a> <span class="ml-20">|</span><a href="javascript:;"
			onclick="showError()" class="c-primary">&nbsp;&nbsp;&nbsp;&nbsp;查看详情</a>
	</p>
	</section>
	<div class="text-l pd-20" id="errorMs" style="display:none">
	<%Exception ex = (Exception)request.getAttribute("ex");
		ex.printStackTrace(new PrintWriter(out)); %>
	</div>
	<script type="text/javascript"
		src="${ctxResources}/hui/lib/jquery/1.9.1/jquery.min.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
			 var str = $('#errorMs').html();   
			    var nstr = str.replace(/\n\r/gi,"<br/>"); 
			    nstr = str.replace(/\r/gi,"<br/>"); 
			    nstr = str.replace(/\n/gi,"<br/>"); 
			    $('errorMs').html(nstr); 
			    });
		function showError() {
			var errorMs = $('#errorMs');
			if (errorMs.is(':hidden')) {
				errorMs.show();
			} else if (errorMs.is(':visible')) {
				errorMs.hide();
			}
		}
	</script>
</body>
</html>