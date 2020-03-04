package com.dao.inter;

import java.util.List;
import java.util.Map;

import com.bean.Exam;


public interface ScoreDaoInter extends BaseDaoInter {
	
	List<Map<String, Object>> getScoreList(Exam exam);
	
}
