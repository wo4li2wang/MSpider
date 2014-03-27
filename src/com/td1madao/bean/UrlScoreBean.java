package com.td1madao.bean;

import java.util.HashSet;

import com.td1madao.stringUtil.MyStringUtil;

/**
 * 名字虽然蛋疼了点，但是它是用来存放URL和关联系数的结构 大致是这个意思，虽然意义上不叫bean了
 * */
public class UrlScoreBean implements Comparable<UrlScoreBean> {
	public UrlScoreBean(double score, String url,HashSet<String> child,int childLevel) {
		this.score = score;
		this.url = url;
		this.setChildLevel(childLevel);
		this.child=child;
		host = MyStringUtil.getHost(url);
	}

	private double score = 0;// 关联系数
	private String url = "";// URL
	private String host = "";// host
	private int childLevel = 0;// 第几层
	public HashSet<String> child = null;// 所有内部的链接

	public double getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	/**
	 * 因为需要，我要的是降序
	 * */
	public int compareTo(UrlScoreBean o) {
		if (score - o.score < 0)
			return 1;
		return -1;
	}

	@Override
	public String toString() {
		return "(" + host + "," + score + "," + url + "," + child +")";
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	

	public int getChildLevel() {
		return childLevel;
	}

	public void setChildLevel(int childLevel) {
		this.childLevel = childLevel;
	}
	public static void main(String[] args) {
//		UrlScoreBean uBean=new UrlScoreBean(3, "www.hehe.com", null);
//		System.out.println(uBean.getHost());
	}
}
