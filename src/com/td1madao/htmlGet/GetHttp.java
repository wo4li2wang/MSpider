package com.td1madao.htmlGet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.td1madao.bean.JsoupBean;
import com.td1madao.filters.fetchUrlUtil;
import com.td1madao.global.GlobalVar;
import com.td1madao.stringUtil.MyStringUtil;


/**
 * 这个类获得指定网址返回的HTML代码
 * 这个东西比较简单，没有HttpClient牛逼，但是执行起来速度绝对不慢，可以用于测试
 * @demo 
 * */
public class GetHttp {
	/**
	 * 模拟俺的google浏览器
	 * 能检查出编码是否出现错误(非GBK编码)
	 * @param url 访问的网址
	 * @return String 网址的HTML代码
	 * */
public static String work(final String url) {
	StringBuilder builder=new StringBuilder();
	try {
		URL realUrl = new URL(url);
		
		URLConnection connection = realUrl.openConnection();//网页参数
		connection.setRequestProperty("Accept", "*/*");
		connection.setRequestProperty("Proxy-Connection:", "keep-alive");
		connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36");
		String str=new String(url);
		str=str.replace("http://", "");
		connection.setRequestProperty("Host", MyStringUtil.getHost(url));//host
		connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");//host
		InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "GBK");
		BufferedReader in = new BufferedReader(isr);		
		String line;
		boolean correctEncoding=true;
		while ((line = in.readLine()) != null) {
			if(!isGBKEncoding(line)){
				correctEncoding=false;
				builder.delete(0, builder.length());
				break;
			}
			builder.append(line);
		}
		in.close();
		isr.close();
		if (!correctEncoding) {//发现编码错误，重新连接
			connection=realUrl.openConnection();
			isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
			in = new BufferedReader(isr);
			while ((line = in.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			isr.close();
		}
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
return builder.toString();
}

/**
 * 判断编码方式是否为GBK
 * */
private static boolean isGBKEncoding(final String line){
	return java.nio.charset.Charset.forName("GBK").newEncoder().canEncode(line);
}


/**
 * 给个URL我就爬
 * 
 * 返回一个JsoupBean 
 * 
 * 打磨打磨，通过数学模型处理一下，就能得到
 * 
 * UrlScoreBean
 * 
 * */
public static JsoupBean workByClient(final String url){
	
	
	Document doc=null;
	String temp=null;
	for (int i = 0; i < GlobalVar.tryTime; i++) {
		
	try{
	Connection connection=Jsoup.connect(url);
	doc = connection.get(); //用Document记录页面信息，和CHROME那玩儿意挺像的
ArrayList<String> al=new ArrayList<String>();	
	 Element body = doc.body();
	  Elements es=body.select("a");
	  for (Iterator<Element> it = es.iterator(); it.hasNext();) {
	   Element e = (Element) it.next();
	   String href=e.attr("href");
	   if (href!=null&&href.length()!=0&&href.indexOf("http")==0) {
		al.add(href);
	}
	  }
	  
	temp=connection.response().url().toString();//情况一：肚熊玩重定向
	if (temp.equals(url)) {//情况一：肚熊用js玩重定向
		String testRedirect=MyStringUtil.deletEnter(doc.toString());
		if (testRedirect.contains("window.location.replace")) {
			temp=fetchUrlUtil.work(testRedirect);
		}
	}
	return new JsoupBean(doc.toString(), temp, al);
	}catch(HttpStatusException e){
	}
	catch(Exception e){
	}
	}
	return new JsoupBean(null, null, null);
}

/**
 * 模块测试
 * */

public static void main(String[] args) {
//	String urlString="http://www.baidu.com/link?url=eJujH2-xVJdDkr5qD5k4JYeoCLEwmODdkxi7Wirv7r6hgnYAYhxUGuBH3xizc9t_CmAydH_tDn7fNrzes__M8K";
//	urlString=workByClient(urlString)[1];
//	System.out.println(urlString);
//	System.out.println("http://donghua.52pk.com/yinhun3/0-13.shtml".indexOf("http")==0);
	
}
}
