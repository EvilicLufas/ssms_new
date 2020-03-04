package com.dao.inter;

import java.util.List;

import com.bean.Page;
import com.bean.Student;

public interface StudentDaoInter extends BaseDaoInter {
	
	public List<Student> getStudentList(String sql, List<Object> param);
	
}
