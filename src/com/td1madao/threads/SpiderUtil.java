package com.td1madao.threads;

import com.td1madao.bean.JsoupBean;
import com.td1madao.bean.UrlScoreBean;
import com.td1madao.global.GlobalVar;
import com.td1madao.gui.MyFrame;
import com.td1madao.htmlGet.GetHttp;
import com.td1madao.math.WordDensity;

/**
 * 这是一个工具类
 * 
 * 说白了就是输入一个地址，返回一个UrlScoreBean 对象
 * 
 * */
public class SpiderUtil {
public static UrlScoreBean work(final String url,final int father) {
	JsoupBean jbBean=GetHttp.workByClient(url);
	String newUrl=new String(url);
	if (jbBean.getChangeURL()!=null) {
		newUrl= jbBean.getChangeURL();//正确的链接地址
	}
	double asso=WordDensity.work(GlobalVar.keyStrings, jbBean.getArticle());
	UrlScoreBean usb=new UrlScoreBean(asso, newUrl, jbBean.child,father+1);
	
	String temp="";
	for (int i = 0; i < GlobalVar.keyStrings.length; i++) {
		temp+= GlobalVar.keyStrings[i]+" ";
	}
	MyFrame.Trace(">>>关键词:"+temp);
	MyFrame.Trace(">>>关联度:"+asso);
	
	return usb;//名字霸气了点，但就是一个数据结构，也是存入到数据库里面的
	
}
public static void main(String[] args) {
//	GlobalVar.keyStrings=new String[]{"地狱少女","阎魔爱"};//╮(╯▽╰)╭ 尽显我大叔本性啊
//	System.out.print(work("http://baike.baidu.com/subview/8773/7380728.htm?fr=aladdin"));
	//没啥问题，爬到了所有的链接，后面过滤掉和 地狱少女没啥关系的就O了
}
}
