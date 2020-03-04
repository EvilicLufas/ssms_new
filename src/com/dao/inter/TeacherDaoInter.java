package com.dao.inter;

import java.util.List;

import com.bean.Clazz;
import com.bean.Grade;
import com.bean.Teacher;

public interface TeacherDaoInter extends BaseDaoInter {
	
	public List<Teacher> getTeacherList(String sql, Object[] param, Grade grade, Clazz clazz);
	
}
