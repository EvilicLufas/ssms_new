package com.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bean.Clazz;
import com.bean.Course;
import com.bean.CourseItem;
import com.bean.Grade;
import com.bean.Page;
import com.bean.Student;
import com.bean.Teacher;
import com.bean.User;
import com.dao.impl.BaseDaoImpl;
import com.dao.impl.StudentDaoImpl;
import com.dao.impl.TeacherDaoImpl;
import com.dao.inter.BaseDaoInter;
import com.dao.inter.StudentDaoInter;
import com.dao.inter.TeacherDaoInter;
import com.tools.MysqlTool;
import com.tools.StringTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TeacherService {
	
	private TeacherDaoInter dao;
	
	public TeacherService(){
		dao = new TeacherDaoImpl();
	}

	public String getTeacherList(Page page) {
		String sql = "SELECT * FROM teacher ORDER BY id DESC LIMIT ?,?";

		List<Teacher> list = dao.getTeacherList(sql, new Object[]{page.getStart(), page.getSize()}, null, null);
		long total = dao.count("SELECT COUNT(*) FROM teacher", new Object[]{});
		Map<String, Object> jsonMap = new HashMap<String, Object>();  
        jsonMap.put("total", total);

        jsonMap.put("rows", list); 

        String result = JSONObject.fromObject(jsonMap).toString();

		return result;
	}
	
	public Teacher getTeacher(String number) {

		String sql = "SELECT * FROM teacher WHERE number=?";

		List<Teacher> list = dao.getTeacherList(sql, new Object[]{number}, null, null);

		return list.get(0);
	}
	
	public String getExamClazz(String number, Grade grade) {

		String sql = "SELECT * FROM teacher WHERE number=?";

		Teacher list = dao.getTeacherList(sql, new Object[]{number}, grade, null).get(0);
		
		List<Clazz> clazzList = new LinkedList<>();
		List<CourseItem> courseItem = list.getCourseList();
		for(CourseItem item : courseItem){
			boolean flag = true;
			for(Clazz clazz : clazzList){
				if(clazz.getId() == item.getClazzid()){
					flag = false;
					break;
				}
			}
			if(flag){
				clazzList.add(item.getClazz());
			}
		}
		String result = JSONArray.fromObject(clazzList).toString();
		
        //返回
		return result;
	}

	public String getExamClazz(String number, Grade grade, Clazz clazz) {
		//sql语句
		String sql = "SELECT * FROM teacher WHERE number=?";
		//获取数据
		Teacher list = dao.getTeacherList(sql, new Object[]{number}, grade, clazz).get(0);
		
		List<Course> courseList = new LinkedList<>();
		List<CourseItem> courseItem = list.getCourseList();
		for(CourseItem item : courseItem){
			courseList.add(item.getCourse());
		}
		String result = JSONArray.fromObject(courseList).toString();
		
        //返回
		return result;
	}
	
	public String getTeacherResult(String number) {
		Teacher teacher = getTeacher(number);
		String result = JSONObject.fromObject(teacher).toString();
        //返回
		return result;
	}
	
	public void addTeacher(Teacher teacher) throws Exception {
		Connection conn = MysqlTool.getConnection();
		try {
			//开启事务
			MysqlTool.startTransaction();
			
			String sql = "INSERT INTO teacher(number, name, sex, qq, photo) value(?,?,?,?,?)";
			Object[] param = new Object[]{
					teacher.getNumber(), 
					teacher.getName(), 
					teacher.getSex(), 
					teacher.getPhone(),
					teacher.getQq()
				};
			//添加教师信息
			int teacherid = dao.insertReturnKeysTransaction(conn, sql, param);
			//设置课程
			if(teacher.getCourse() != null && teacher.getCourse().length > 0){
				for(String course : teacher.getCourse()){
					String[] gcc = course.split("_");
					int gradeid = Integer.parseInt(gcc[0]);
					int clazzid = Integer.parseInt(gcc[1]);
					int courseid = Integer.parseInt(gcc[2]);
					
					dao.insertTransaction(conn, 
							"INSERT INTO clazz_course_teacher(clazzid, gradeid, courseid, teacherid) value(?,?,?,?) ", 
							new Object[]{clazzid, gradeid, courseid, teacherid});
				}
			}
			//添加用户记录
			dao.insertTransaction(conn, "INSERT INTO user(account, name, type) value(?,?,?)", 
					new Object[]{
						teacher.getNumber(),
						teacher.getName(),
						User.USER_TEACHER
				});
			
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
	
	public void editTeacher(Teacher teacher) throws Exception {
		Connection conn = MysqlTool.getConnection();
		try {
			//开启事务
			MysqlTool.startTransaction();
			
			String sql = "UPDATE teacher set name=?,sex=?,phone=?,qq=? WHERE id=?";
			Object[] param = new Object[]{
					teacher.getName(), 
					teacher.getSex(), 
					teacher.getPhone(),
					teacher.getQq(),
					teacher.getId()
				};
			//修改教师信息
			dao.updateTransaction(conn, sql, param);
			//修改系统用户信息
			dao.update("UPDATE user SET name=? WHERE account=?", 
					new Object[]{teacher.getName(), teacher.getNumber()});
			//删除教师与课程的关联
			dao.deleteTransaction(conn, "DELETE FROM clazz_course_teacher WHERE teacherid =?", new Object[]{teacher.getId()});
			//设置课程
			if(teacher.getCourse() != null && teacher.getCourse().length > 0){
				for(String course : teacher.getCourse()){
					String[] gcc = course.split("_");
					int gradeid = Integer.parseInt(gcc[0]);
					int clazzid = Integer.parseInt(gcc[1]);
					int courseid = Integer.parseInt(gcc[2]);
					
					dao.insertTransaction(conn, 
							"INSERT INTO clazz_course_teacher(clazzid, gradeid, courseid, teacherid) value(?,?,?,?) ", 
							new Object[]{clazzid, gradeid, courseid, teacher.getId()});
				}
			}
			
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
	
	public void editTeacherPersonal(Teacher teacher){
		
		String sql = "UPDATE teacher SET name=?, sex=?, phone=?, qq=? WHERE number=?";
		
		//更新信息
		dao.update(sql, new Object[]{
				teacher.getName(), 
				teacher.getSex(),
				teacher.getPhone(),
				teacher.getQq(),
				teacher.getNumber()});
		
		dao.update("UPDATE user SET name=? WHERE account=?", 
				new Object[]{teacher.getName(), teacher.getNumber()});
	}

	public void deleteTeacher(String[] ids, String[] numbers) throws Exception{
		//获取占位符
		String mark = StringTool.getMark(ids.length);
		Integer tid[] = new Integer[ids.length];
		for(int i =0 ;i < ids.length;i++){
			tid[i] = Integer.parseInt(ids[i]);
		}
		//获取连接
		Connection conn = MysqlTool.getConnection();
		//开启事务
		MysqlTool.startTransaction();
		try {
			//删除教师与课程的关联
			dao.deleteTransaction(conn, "DELETE FROM clazz_course_teacher WHERE teacherid IN("+mark+")", tid);
			//删除教师
			dao.deleteTransaction(conn, "DELETE FROM teacher WHERE id IN("+mark+")", tid);
			//删除系统用户
			dao.deleteTransaction(conn, "DELETE FROM user WHERE account IN("+mark+")",  numbers);
			
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
	
	public void setPhoto(String number, String fileName) {
		String photo = "photo/"+fileName;
		dao.update("UPDATE teacher SET photo=? WHERE number=?", new Object[]{photo, number});
	}
	
}
