package com.td1madao.math;

import java.util.List;
/**
 * ArrayList用的方便，
 * 除重时就用下这个吧
 * 不用Set好了
 * 
 * 这里list不能定义成final！！！！
 * */
public class ListProcess {
	public static void removeDuplicate(List<?> list){
		for (int i=0;i<list.size()-1;i++) {
		for(int j=list.size()-1;j>i;j--){
		if(list.get(j).equals(list.get(i))){list.remove(j);}
		}
		}
		}
}
