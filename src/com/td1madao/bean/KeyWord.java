package com.td1madao.bean;
/**
 * 将关键词作为一种数据结构
 * */
public class KeyWord {
	
private String name=null;//关键词
private double weight=0;//关键词的权重，权重越高评分越高
private boolean necessary=false;//是否必须存在(不存在将会被过滤)
public KeyWord(String name,double weight,boolean necessary) {
	this.setName(name);
	this.setWeight(weight);
	this.setNecessary(necessary);
}
public double getWeight() {
	return weight;
}
public void setWeight(double weight) {
	this.weight = weight;
}
public boolean isNecessary() {
	return necessary;
}
public void setNecessary(boolean necessary) {
	this.necessary = necessary;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

@Override
public String toString() {
	return "("+name+","+weight+","+necessary+")";
}
}
