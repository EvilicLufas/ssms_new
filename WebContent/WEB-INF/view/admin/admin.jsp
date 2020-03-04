<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>学生成绩管理系统 管理员后台</title>
    <link rel="shortcut icon" href="icon.ico"/>
    <link rel="stylesheet" href="css/menuBox-1.1.css" />
    <script type="text/javascript" src="js/json2.js"></script>
    <script type="text/javascript" src="js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="js/checkutil.js"></script>
    <script type="text/javascript" src="js/menuBox-1.1.js"></script>
    <link rel="stylesheet" href="css/login.css" />
    


</head>
<body>

	<noscript>
		<div style=" position:absolute; z-index:100000; height:2046px;top:0px;left:0px; width:100%; background:white; text-align:center;">
		    <img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
		</div>
	</noscript>
    <div region="north" split="true" border="false" style="overflow: hidden; height: 30px;
        background: url(images/layout-browser-hd-bg.gif) #7f99be repeat-x center 50%;
        line-height: 20px;color: #fff; font-family: Verdana, 微软雅黑,黑体">
        <span style="float:right; padding-right:20px;" class="head"><span style="color:red; font-weight:bold;">${user.name}&nbsp;</span>您好&nbsp;&nbsp;&nbsp;<a href="SystemServlet?method=LoginOut" id="loginOut">安全退出</a></span>
        <span style="padding-left:10px; font-size: 16px; ">学生信息管理系统</span>
    </div>
    
	<div id="menuBox01" class="menuBox" fit="true">
		
	</div>
    
        <div id="tabs" class="tab">
			<jsp:include page="/WEB-INF/view/admin/welcome.jsp" />
		</div>
	
	<iframe width=0 height=0 src="refresh.jsp"></iframe>
	
</body>
    <script type="text/javascript">
    	var dataObj01 = [{
			name: "成绩统计分析",
			subMenus: [{
				name: "考试列表",
				url: "ExamServlet?method=toExamListView"
			}]
		}, {
			name: "学生信息管理",
			subMenus: [{
				name: "学生列表",
				url: "StudentServlet?method=toStudentListView"
			}]
		}, {
			name: "教师信息管理",
			subMenus: [{
				name: "教师列表",
				url: "TeacherServlet?method=toTeacherListView"
			}]
		},{
			name: "基础信息管理",
			subMenus: [{
				name: "年级列表",
				url: "GradeServlet?method=toGradeListView"
			},{
				name: "班级列表",
				url: "ClazzServlet?method=toClazzListView"
			},{
				name: "课程列表",
				url: "CourseServlet?method=toCourseListView"
			}]
		},{
			name: "系统管理",
			subMenus: [{
				name: "系统设置",
				url: "SystemServlet?method=toAdminPersonalView"
			}]
		}];

		var config01 = {
			menuBoxId: "#menuBox01",
			multiple: true,
			openIndex: []
		}
		menuBox.init(config01, dataObj01);
    </script>
</html>