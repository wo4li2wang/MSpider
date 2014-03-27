package com.td1madao.useEngine;

import java.util.ArrayList;

import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;

import com.td1madao.bean.KeyWord;
import com.td1madao.filters.fetchUrlUtil;
import com.td1madao.global.GlobalVar;
import com.td1madao.gui.MyFrame;



public class FetchSouSou {
	/**
	 * 说实话，搜搜的引擎和360引擎很像，彼此就是变量名字不同，换页方式都一摸一样
	 * 百度引擎和谷歌很像
	 * 
	 * */

	
	
	
	public static ArrayList<String> work(String host){
		
		ArrayList<String> ret=new ArrayList<String>();
		int urlPage=0;//肚熊的第几页
		String elementString = null;//js提取的元素
		String key=null;
		if (GlobalVar.searchCont==null) {
			
			if (host!=null) {
				key="site%3A"+host+"%20"+getKeySerials();
			}else {
				key=getKeySerials();
			}
			}else {
				key=GlobalVar.searchCont;
			}
			
		while (true) {
		String threadUrl ="http://www.soso.com/q?query="+key+"&pg="+(urlPage+1);
		MyFrame.Trace("搜搜搜索："+threadUrl);
		urlPage++;//翻页
		try{
			Document doc = null;
			
				doc = (Document)Jsoup.connect(threadUrl).get(); //用Document记录页面信息，和CHROME那玩儿意挺像的
			for (int i = 0; i < GlobalVar.baiduNum; i++) {
				elementString=doc.getElementsByClass("rb").toString();//搜索到的代码
//						Id(String.valueOf(getURLNum+1)).toString();
				int nowSize=ret.size();//当前大小
				ArrayList<String>ansArrayList=fetchUrlUtil.workAll(elementString);//获得符合标准的超链接
				if (nowSize+ansArrayList.size()>GlobalVar.searchNum) {//数量太多
					
					for (int j = 0; j < GlobalVar.searchNum-nowSize; j++) {
						ret.add(ansArrayList.get(j));
					}
					break;	
				}
				else
				ret.addAll(ansArrayList);
			
			}//end for
			if (ret.size()>=GlobalVar.searchNum) {
				break;
			}
			
			}catch(NullPointerException e){
				MyFrame.Trace("搜搜君找不到你想要的，不信你搜搜试试！");
				return null;
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
//			GlobalVar.searchNum=20;//让搜搜找20个
//			System.out.println("fetch搜搜");
//			GlobalVar.keyStrings=new String[]{"阿里巴巴","马云"};//每次测试都搜阿里巴巴呢，马云叔叔不会生气吧？
//			ArrayList<String>aList=work(null);
//				System.out.println(aList);
//				System.out.println(aList.size());
		}
}
