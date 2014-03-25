package com.td1madao.filters;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 名字虽然叫的难听，就是获得一段HTML代码中第一个或所有URL
 * */
public class fetchUrlUtil {
	/**
	 * @param htmlCode 代码段
	 * @return 返回字符串数组，就是所有的URL
	 * 只要第一个
	 * */
	public static String work(final String htmlCode) {
		Matcher matcher = null;
		Pattern pattern1 = Pattern.compile("\"http?\'?(.*?)\"?\'?\"",Pattern.DOTALL);
		matcher = pattern1.matcher(htmlCode);
		if(matcher != null && matcher.find())
		{	String temp=matcher.group(0);
				return 	temp.substring(temp.indexOf("\"")+1,temp.lastIndexOf("\""));
		}
		else return null;
	}
	/**
	 * 得到所有的超链接
	 * */
	public static ArrayList<String> workAll(final String htmlCode) {
		ArrayList<String>aList=new ArrayList<String>();
		Matcher matcher = null;
		Pattern pattern1 = Pattern.compile("\"http?\'?(.*?)\"?\'?\"",Pattern.DOTALL);
		matcher = pattern1.matcher(htmlCode);
		while(matcher.find()) {
			String s=matcher.group();
			if (!(s==null||s.equals(""))) {
				aList.add(s);
			}
		}
		return aList;
	}
	
	public static void main(String[] args) {
//		String ss = "<html> <head>  <script>window.location.replace(\"http://www.bilibili.tv/video/av366276/\")</script>   <noscript>   <meta http-equiv=\"refresh\" content=\"0;URL='http://www.bilibili.tv/video/av366276/'\" />  </noscript>  </head> <body></body></html>";
		String ss = "<a href=\"http://baike.so.com/lottery/?act_id=5&amp;src=ss\" target=\"_blank\" class=\"edit\">有奖编辑</a><a href=\"http://baike.so.com/doc/416057.html\" data-tp=\"kvdb\" data-stp=\"baike\" data-extargs=\"[]\" data-st=\"0\" data-e=\"1\" data-pos=\"1\" data-m=\"612eee9776742d44e020ba83b983e4ec\" target=\"_blank\"><em>狗熊</em>_360百科</a>\"";
		System.out.println(workAll(ss));
	}
}
