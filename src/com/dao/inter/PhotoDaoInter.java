package com.dao.inter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import com.bean.User;

public interface PhotoDaoInter {
	
	void setPhoto(User user, InputStream is) throws Exception;
	
	InputStream getPhoto(User user);
}
