package com.dao.inter;

import java.util.List;

import com.bean.Exam;

public interface ExamDaoInter extends BaseDaoInter {
	
	public List<Exam> getExamList(String sql, List<Object> param);
	
}
