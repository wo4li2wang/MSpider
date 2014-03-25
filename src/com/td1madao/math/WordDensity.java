package com.td1madao.math;

import com.td1madao.bean.KeyWord;

/**
 * 计算词频密度的数学模型
 * 
 * 如果有时间打算用向量相似度优化这个模型
 * 就是求关键词分布情况，出现位置记为1，没出现的记为0
 * 之后各个关键词数组看做向量，求相似度
 * 这么确定关键词的关联程度
 * 
 * 数模国赛时有个题目是拼接纸条，当时我用的就是这个原理
 * 
 * 熵来判断两个向量之间的相似度，可以用利用 [熵权系数法] 来评价
 * 
 * */
public class WordDensity {
	/**
	 * @param keyWord 关键词数组
	 * @param article 文章(网页)
	 * */
public static double work(final KeyWord []keyWord,final String article) {
	if (article==null) {
		return 0;
	}
int keyWordLength=keyWord.length;
int articleLength=article.length();//理解为x的区间吧！
int countKeyWord[]=new int[keyWordLength];//关键词出现次数
double density=0;
for (int i = 0; i < countKeyWord.length; i++) {
	countKeyWord[i]=calTime(article, keyWord[i]);//这个东东是计算词频密度和关键词分布用的东东
	density+=((double)countKeyWord[i]/articleLength)*keyWord[i].getWeight();
	if (countKeyWord[i]==0&&keyWord[i].isNecessary()) {//是关键词，但它没出现过
		return 0;//直接无效
	}
}
return density;
}
/**
 * 关键词出现次数
 * */
public static int calTime(final String str,final KeyWord str1) {
	int count = 0;
	int start = 0;
	while (str.indexOf(str1.getName(), start) >= 0 && start < str.length()) {
		count++;
		start = str.indexOf(str1.getName(), start) + str1.getName().length();
	}
	return count;
}
public static void main(String[] args) {
	//这回拿《银魂》来试试吧！╮(╯▽╰)╭
//	String getHTML=GetHttp.work("http://tieba.baidu.com/f?kw=%B3%A4%B9%C8%B4%A8%CC%A9%C8%FD&fr=ala0");//继续拿肚熊做实验
//	String deleteEnter=MyStringUtil.deletEnter(getHTML);
//	double ans=work(new String[]{"MADAO","长谷川","坂田银时"},deleteEnter);
//	System.out.println(ans);
}
}
