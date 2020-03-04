package com.dao.inter;

import java.util.List;

import com.bean.Clazz;
import com.bean.Page;

public interface ClazzDaoInter extends BaseDaoInter {
	
	public List<Clazz> getClazzDetailList(String gradeid, Page page);
	
}
