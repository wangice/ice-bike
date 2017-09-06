<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/common/huihead.jsp"%>
<title></title>
</head>
<body>
	<section class="page-404 minWP text-c">
	<p class="error-title">
		<i class="Hui-iconfont va-m" style="font-size: 80px">&#xe656;</i><span
			class="va-m"> 404</span>
	</p>
	<p class="error-description">不好意思，您访问的页面不存在~</p>
	<p class="error-info">
		您可以：
		<!-- <a href="javascript:;" onclick="history.go(-1)" class="c-primary">&lt; 返回上一页</a><span class="ml-20">|</span> -->
		<a <%-- href="${ctx}/index" --%> onclick="toIndex()"
			class="c-primary ml-20">去首页 &gt;</a>
	</p>
	</section>
</body>
</html>