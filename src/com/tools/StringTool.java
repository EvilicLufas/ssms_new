package com.tools;

import java.io.UnsupportedEncodingException;

public class StringTool {
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str.trim())){
			return true;
		} else{
			return false;
		}
	}
	public static String messyCode(String str){
		String code = null;
		try {
			byte[] by = str.getBytes("ISO-8859-1");
			code = new String(by, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	public static String getMark(int length){
		StringBuffer mark = new StringBuffer("");
		for(int i = 0;i < length;i++){
			mark.append("?,");
		}
		mark.deleteCharAt(mark.length()-1);
		return mark.toString();
	}
	
}
