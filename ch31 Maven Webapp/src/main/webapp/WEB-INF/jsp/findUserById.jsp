<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
 
<title>测试页面</title>
 
</head>
 
<body>
 
 
	<form
		action="${pageContext.request.contextPath }/user/findUserById"
		method="post">
		<input type="text" name="id" /> <input type="submit" value="测试整合性" />
	</form>
 
</body>
</html>
