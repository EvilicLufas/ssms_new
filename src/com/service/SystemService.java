package com.service;

import java.util.List;

import com.bean.Clazz;
import com.bean.Grade;
import com.bean.SystemInfo;
import com.bean.User;
import com.dao.impl.BaseDaoImpl;
import com.dao.impl.SystemDaoImpl;
import com.dao.inter.BaseDaoInter;
import com.dao.inter.SystemDaoInter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class SystemService {
	
	SystemDaoInter dao = new SystemDaoImpl();
	
	public String getAccountList(){
		//获取数据
		List<String> list = dao.getColumn("SELECT account FROM user", null);
		//json化
        String result = JSONArray.fromObject(list).toString();
        
        return result;
	}

	public User getAdmin(User user) {
		User searchUser = (User) dao.getObject(User.class, 
				"SELECT * FROM user WHERE account=? AND password=? AND type=?", 
				new Object[]{user.getAccount(), user.getPassword(), user.getType()});
		
		return searchUser;
	}

	public void editPassword(User user) {
		dao.update("UPDATE user SET password=? WHERE account=?", 
				new Object[]{user.getPassword(),user.getAccount()});
	}
	
	public SystemInfo editSystemInfo(String name, String value) {
		//修改数据库
		dao.update("UPDATE system SET "+name+" = ?", new Object[]{value});
		//重新加载数据
		//获取系统初始化对象
    	SystemInfo sys = (SystemInfo) dao.getObject(SystemInfo.class, "SELECT * FROM system", null);
    	return sys;
	}
}
