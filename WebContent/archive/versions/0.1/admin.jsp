<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="device-width, initial-scale=1.0, user-scalable=no">
<script type="text/javascript" src="../../../jquery/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="js/formstyle.js"></script>
<link rel="stylesheet" href="css/formstyle.css" />
<title>File Upload Demo</title>
</head>
<body>
<form class="ben-form" action="upload.jsp" enctype="multipart/form-data" method="post">
<label>上传数据表</label>
<input class="ben-file" type="file" name="upload-filename" value="" />
<input class="ben-button" type="submit" name="button-upload" value="上传" />
</form>
</body>
</html>