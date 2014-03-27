package com.td1madao.stringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串用到的一点处理
 * */
public class MyStringUtil {
	/**
	 * 去掉所有的换行符
	 * */
	public static String deletEnter(final String string) {
		if (string!=null) {
			String temp= string.replaceAll(" ", "");
			 temp= temp.replaceAll("\t", "");
			return temp.replaceAll("[\\t\\n\\r]", "");
		}
		return null;
	}
	/**
	 * 基于正则表达式从URL中撸出host
	 * */
	public static String getHost(final String url){
		  if(url==null||url.trim().equals("")){
		   return "";
		  }
		  String host = "";
		  Pattern p =  Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
		  Matcher matcher = p.matcher(url);  
		  if(matcher.find()){
		   host = matcher.group();  
		  }
		  return host;
		 }
	public static void main(String[] args) {
		System.out.println(getHost("www.hehe.com"));
	}
}
