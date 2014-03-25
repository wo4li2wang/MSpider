package com.td1madao.useEngine;

import java.util.ArrayList;

import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;

import com.td1madao.bean.KeyWord;
import com.td1madao.filters.fetchUrlUtil;
import com.td1madao.global.GlobalVar;
import com.td1madao.stringUtil.MyStringUtil;



public class FetchBaidu {
	/**
	 * 在肚熊上搜索关键词,保存到Global里面
	 * 利用肚熊当种子
	 * 
	 * 搜索的数量为Global里面规定的数量
	 * @param String string 是限制的网站，没要求就是null
	 * @return 搜索的结果为一个字符串数组
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
		String threadUrl ="http://www.baidu.com/s?wd="+key+"&pn="+(urlPage*10);
		try{
		Document doc = (Document)Jsoup.connect(threadUrl).get(); //用Document记录页面信息，和CHROME那玩儿意挺像的
		for (int i = 0; i < GlobalVar.baiduNum; i++) {
			elementString=doc.getElementById(String.valueOf(getURLNum+1)).toString();
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
		KeyWord[] keyword = GlobalVar.keyStrings.clone();
		StringBuffer keyComb=new StringBuffer();
		for (int i = 0; i < keyword.length-1; i++) {
			keyComb.append(keyword[i].getName());
			keyComb.append('+');
		}
		keyComb.append(keyword[keyword.length-1].getName());
		return keyComb.toString();
	}
	
	
	 /**
		 * 我测！
		 */
		public static void main(String[] args) {
//			GlobalVar.keyStrings=new String[]{"阿里巴巴","马云"};
//				work(null);
//				System.out.println(GlobalVar.urlStore);
//				System.out.println(GlobalVar.urlStore.size());
		}
}
