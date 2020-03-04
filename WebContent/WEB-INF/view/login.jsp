<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<link rel="shortcut icon" href="icon.ico"/>
			<link rel="stylesheet" href="css/login.css" />
			<script src="http://www.jqhtml.com/jquery/jquery-1.10.2.js"></script>
			<script src="js/jquery-3.3.1.js"></script>			
			<script>
				$(function(){
					//登录
					$("#submitBtn").click(function(){
						if($("#radio-2").attr("checked") && "${systemInfo.forbidStudent}" == 1){
							$.messager.alert("消息提醒", "学生暂不能登录系统！", "warning");
							return;
						}
						if($("#radio-3").attr("checked") && "${systemInfo.forbidTeacher}" == 1){
							$.messager.alert("消息提醒", "教师暂不能登录系统！", "warning");
							return;
						}
						
						var data = $("#form").serialize();
						$.ajax({
							type: "post",
							url: "LoginServlet?method=Login",
							data: data, 
							dataType: "text", //返回数据类型
							success: function(msg){
								if("loginError" == msg){
									$.messager.alert("消息提醒", "用户名或密码错误!", "warning");
									$("#vcodeImg").click();//切换验证码
									$("input[name='vcode']").val("");//清空验证码输入框
								} else if("admin" == msg){
									window.location.href = "SystemServlet?method=toAdminView";
								} else if("student" == msg){
									window.location.href = "SystemServlet?method=toStudentView";
								} else if("teacher" == msg){
									window.location.href = "SystemServlet?method=toTeacherView";
								}
							}
						});
					});
				})
			</script>
			<title>登录</title>
	</head>
	<body>
			<div class="container">
					<h2>学生成绩管理系统</h2>
				<div class="loginWraper">
					<div class="loginBox">
						<form id="form" method="post">
							<div class="user">
								<label class="icon_1"><img src="pic/account.ico"/></label>
								<input id="" name="account" type="text" placeholder="账户" class="input_2">
							</div>
							<div class="user">
								<label class="icon_1"><img src="pic/pwd.ico"/></label>
								<input id="" name="password" type="password" placeholder="密码" class="input_2">
							</div>

							<div class="box1">
								<div class="radio_box">
									<input type="radio" id="radio-2" name="type" checked value="2" class="r1" />
									<label for="radio-1">学生</label>
								</div>
								<div class="radio_box">
									<input type="radio" id="radio-3" name="type" value="3" class="r1" />
									<label for="radio-2">老师</label>
								</div>
								<div class="radio_box">
									<input type="radio" id="radio-1" name="type" value="1" class="r1" />
									<label for="radio-3">管理员</label>
								</div>
							</div>
							<div class="row">
								<div class="formControls col-8 col-offset-3">
									<input id="submitBtn" type="button" class="btn" value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;"/>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
	</body>
</html>