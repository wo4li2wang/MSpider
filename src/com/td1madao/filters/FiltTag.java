package com.td1madao.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.td1madao.htmlGet.GetHttp;
import com.td1madao.stringUtil.MyStringUtil;

/**
 * 用于过滤网页标签的类
 * */
public class FiltTag {
private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

public static String work(final String htmlStr) {
	if (htmlStr==null) {
		return null;
	}
	
	String temp=new String(htmlStr);
    Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
    Matcher m_script = p_script.matcher(temp);
    temp = m_script.replaceAll(""); // 过滤script标签
    Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
    Matcher m_style = p_style.matcher(temp);
    temp = m_style.replaceAll(""); // 过滤style标签
    Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
    Matcher m_html = p_html.matcher(temp);
    temp = m_html.replaceAll(""); // 过滤html标签
    return temp.trim().replaceAll("&nbsp;", ""); // 返回文本字符串
}
public static void main(String[] args) {
	String getHTML=GetHttp.work("http://www.baidu.com/");//继续拿肚熊做实验
	String deleteEnter=MyStringUtil.deletEnter(getHTML);
	String deleteTag=work(deleteEnter);
	System.out.println(deleteTag);
	//嗯，貌似没啥问题，好吧，放过你吧！\(^o^)/~
}
}
