package com.service;

import java.sql.Connection;
import java.util.List;

import com.bean.Course;
import com.dao.impl.BaseDaoImpl;
import com.dao.inter.BaseDaoInter;
import com.tools.MysqlTool;
import com.tools.StringTool;

import net.sf.json.JSONArray;

public class CourseService {
	
	BaseDaoInter dao = new BaseDaoImpl();
	
	public String getCourseList(String gradeid){
		List<Object> list;
		if(StringTool.isEmpty(gradeid)){
			list = dao.getList(Course.class, "SELECT * FROM course");
		} else{
			list = dao.getList(Course.class, 
					"SELECT c.* FROM course c, grade_course gc WHERE c.id=gc.courseid AND gc.gradeid=?", 
					new Object[]{Integer.parseInt(gradeid)});
		}
		//json化
        String result = JSONArray.fromObject(list).toString();
        
        return result;
	}

	public void addCourse(Course course) {
		dao.insert("INSERT INTO course(name) value(?)", new Object[]{course.getName()});
	}

	public void deleteClazz(int courseid) throws Exception {
		//获取连接
		Connection conn = MysqlTool.getConnection();
		try {
			//开启事务
			MysqlTool.startTransaction();
			//删除成绩表
			dao.deleteTransaction(conn, "DELETE FROM escore WHERE courseid=?", new Object[]{courseid});
			//删除班级的课程和老师的关联
			dao.deleteTransaction(conn, "DELETE FROM clazz_course_teacher WHERE courseid=?", new Object[]{courseid});
			//删除年级与课程关联
			dao.deleteTransaction(conn, "DELETE FROM grade_course WHERE courseid=?",  new Object[]{courseid});
			//最后删除课程
			dao.deleteTransaction(conn, "DELETE FROM course WHERE id=?",  new Object[]{courseid});
			
			//提交事务
			MysqlTool.commit();
		} catch (Exception e) {
			//回滚事务
			MysqlTool.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			MysqlTool.closeConnection();
		}
	}
	
	
	
	
}
