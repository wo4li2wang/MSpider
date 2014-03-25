package com.td1madao.bean;

import java.util.ArrayList;
/**
 * 这个数据结构是个临时变量，请尽可能无视它
 * 
 * 之所以要用正确的URL，是因为后面的数学模型需要用到host来对域名评级
 * 
 * */
public class JsoupBean {
private String article=null;
private String changeURL=null;
public ArrayList<String> child=null;
public JsoupBean(String article,String changeURL,ArrayList<String>child) {
	this.setArticle(article);
	this.setChangeURL(changeURL);
	this.child=child;
}
public String getChangeURL() {
	return changeURL;
}
public void setChangeURL(String changeURL) {
	this.changeURL = changeURL;
}
public String getArticle() {
	return article;
}
public void setArticle(String article) {
	this.article = article;
}
}
