package com.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.User;
import com.service.SystemService;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	private SystemService service = new SystemService();
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求的方法
		String method = request.getParameter("method");
		
		if("Login".equals(method)){ //验证登录
			login(request, response);
		}
	}
	
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取用户输入的账户
		String account = request.getParameter("account");
		//获取用户输入的密码
		String password = request.getParameter("password");
		//获取登录类型
		int type = Integer.parseInt(request.getParameter("type"));
		
		//返回信息
		String msg = "";
		
		//判断用户名和密码是否正确
			//将账户和密码封装
			User user = new User();
			user.setAccount(account);
			user.setPassword(password);
			user.setType(Integer.parseInt(request.getParameter("type")));
			//创建系统数据层对象
			
			//查询用户是否存在
			User loginUser = service.getAdmin(user);
			if(loginUser == null){//如果用户名或密码错误
				msg = "loginError";
			} else{ //正确
				if(User.USER_ADMIN == type){
					msg = "admin";
				} else if(User.USER_STUDENT == type){
					msg = "student";
				} else if(User.USER_TEACHER == type){
					msg = "teacher";
				}
				//将该用户名保存到session中
				request.getSession().setAttribute("user", loginUser);
			}
		//返回登录信息
		response.getWriter().write(msg);
	}
	
}
