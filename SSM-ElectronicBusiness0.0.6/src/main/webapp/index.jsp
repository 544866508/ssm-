<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
 
<title>测试页面</title>
 
</head>
 
<body>
 
springmvc上传文件
<form name="form1" action="${pageContext.request.contextPath }/manage/product/upload.do" method="post" enctype="multipart/form-data">
	<input type="file" name="upload_file" />
	<input type="submit" value="springmvc上传文件"/>
</form>
富文本图片上传文件
<form name="form2" action="${pageContext.request.contextPath }/manage/product/rich_img_upload.do" method="post" enctype="multipart/form-data">
	<input type="file" name="upload_file" />
	<input type="submit" value="富文本图片上传文件"/>
</form>
 
</body>
</html>
