package com.td1madao.threads;

import com.td1madao.bean.JsoupBean;
import com.td1madao.bean.UrlScoreBean;
import com.td1madao.filters.FiltTag;
import com.td1madao.global.GlobalVar;
import com.td1madao.htmlGet.GetHttp;
import com.td1madao.math.WordDensity;
import com.td1madao.stringUtil.MyStringUtil;

/**
 * 这是一个工具类
 * 
 * 说白了就是输入一个地址，返回一个UrlScoreBean 对象
 * 
 * */
public class SpiderUtil {
public static UrlScoreBean work(final String url) {
	JsoupBean jbBean=GetHttp.workByClient(url);
	String newUrl=new String(url);
	if (jbBean.getChangeURL()!=null) {
		newUrl= jbBean.getChangeURL();//正确的链接地址
	}
	UrlScoreBean usb=new UrlScoreBean(WordDensity.work(GlobalVar.keyStrings, FiltTag.work(MyStringUtil.deletEnter(jbBean.getArticle()))), newUrl, jbBean.child);
	return usb;//名字霸气了点，但就是一个数据结构，也是存入到数据库里面的
	
}
public static void main(String[] args) {
	GlobalVar.keyStrings=new String[]{"地狱少女","阎魔爱"};//╮(╯▽╰)╭ 尽显我大叔本性啊
	System.out.print(work("http://baike.baidu.com/subview/8773/7380728.htm?fr=aladdin"));
	//没啥问题，爬到了所有的链接，后面过滤掉和 地狱少女没啥关系的就O了
}
}
