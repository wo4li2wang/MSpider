package com.td1madao.useEngine;

import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;

import com.td1madao.filters.fetchUrlUtil;
import com.td1madao.global.GlobalVar;
import com.td1madao.gui.MyFrame;
import com.td1madao.gui.NoGui;
import com.td1madao.stringUtil.MyStringUtil;



public class FetchGoogle {
	/**
	 *	谷歌搜索，建议不要开
	 *	天朝卡死 
	 *
	 *	我测试这个模块都卡疯了
	 *	
	 *
	 * */

	
	
	
	public static ArrayList<String> work(String host){
		
		ArrayList<String> ret=new ArrayList<String>();
		int getURLNum=0;//爬到的个数，适可而止嘛(Global里面定义了数量)
		int urlPage=0;//肚熊的第几页
		String elementString = null;//js提取的元素
		String key=null;
		if (host!=null) {
			key="site%3A"+host+"%20"+getKeySerials();
		}else {
			key=getKeySerials();
		}
		while (true) {
		String threadUrl ="http://www.google.com.hk/search?q="+key+"&start="+(urlPage*10);
		System.out.println(threadUrl);
		try{
			Connection conn=Jsoup.connect(threadUrl);
			conn.header("Host", "www.google.com.hk");
			conn.header("Proxy-Connection", "keep-alive");
			conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
			conn.header("Accept-Encoding", "gzip,deflate,sdch");
			conn.header("Accept-Language", "zh-CN,zh;q=0.8");
			Document doc=null ;
			try{
		 doc = (Document)conn.get(); //用Document记录页面信息，和CHROME那玩儿意挺像的
			}catch(Exception e){
				MyFrame.Trace("谷歌检测出流量异常，需要输入验证码，建议关闭谷歌搜索引擎");
				return null;
			}
		for (int i = 0; i < GlobalVar.baiduNum; i++) {
			elementString=doc.getElementsByClass("g").toString();
			elementString=MyStringUtil.deletEnter(elementString);//√
		String gerStrings=fetchUrlUtil.work(elementString);//获得网页的URL
		if(gerStrings!=null||getURLNum>=GlobalVar.searchNum){
			ret.add(gerStrings);//得到的连接丢进去！
			getURLNum++;
		}
		else {
			break;
		}
		}//end for
		if (getURLNum>=GlobalVar.searchNum) {
			break;
		}
		else {
			urlPage++;//翻页
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		}//end while
		return ret;
	}
	
	/**
	 * 搜索引擎的关键词
	 * */
	private static String getKeySerials() {
		String[] keyword = GlobalVar.keyStrings.clone();
		StringBuffer keyComb=new StringBuffer();
		for (int i = 0; i < keyword.length-1; i++) {
			keyComb.append(keyword[i]);
			keyComb.append('+');
		}
		keyComb.append(keyword[keyword.length-1]);
		return keyComb.toString();
	}
	
	
	 /**
		 * 我测！
		 */
		public static void main(String[] args) {
			GlobalVar.searchNum=20;//让搜搜找20个
			System.out.println("fetch谷歌");
			GlobalVar.keyStrings=new String[]{"阿里巴巴","马云"};//每次测试都搜阿里巴巴呢，马云叔叔不会生气吧？
			ArrayList<String>aList=work(null);
				System.out.println(aList);
				System.out.println(aList.size());
		}
}
