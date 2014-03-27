package com.td1madao.global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.td1madao.bean.KeyWord;
import com.td1madao.bean.UrlScoreBean;

/**
 * 名字虽然很欠揍，但它是存全局配置的
 * 
 * 它里面的数据除了初始化时会改，正常基本不变，我就先不用同步啦 啦啦啦啦啦啦……
 * */
public class GlobalVar {
	public static int searchNum = 3;// 每个搜索引擎构建模型时提取数量,建议不要太多，否则初始化会很慢
	public static int searchLevel = 3;// 与关键词不相关的允许层数，太小会搜不到资源，太大会搜到大量不相关资源，建议是3
	public static int baiduNum = 10;// 百度每页是10个搜索结果
	public static int tryTime = 3;// http访问失败次数，表示失败3次就拉倒了
	public static int queueLength = 100;// 任务队列的长度，打算后来可以调整
	public static int spiderNum = 8;// 爬虫的个数
	public static boolean spiderState[]=new boolean[spiderNum];
	public static double filterPos = 0.8;// 如果队列太慢，过滤到这个位置×100%后面的东西，一个小小的优化,免得浪费资源
	public static double filterScore = 0;// 这个是数学模型得到的评测分数，小于这个分数咱就把它当成垃圾网页，直接过滤掉不放入数据库中
	public static volatile double maxScore = 0;// 关联度最强的分数
	public static volatile String maxHost = null;// 最相关的host，如果爬虫真的耗尽资源，那就用这个host创造更多的资源吧！哈哈哈
	public static boolean init = true;// 最相关的host，如果爬虫真的耗尽资源，那就用这个host创造更多的资源吧！哈哈哈
	public static List<String> urlStore = new ArrayList<String>();
	// 这东东是存储第一次引擎获得的URL用的！后期打算多个引擎一起添加
	public static List<UrlScoreBean> beanList = new ArrayList<UrlScoreBean>();
	// 存储URL和评分，后面会对host排序
	public static KeyWord[] keyStrings = null;//搜索的关键词
	public static String username = "root";// 数据库
	public static String searchCont = null;// 搜索引擎搜索内容
	public static String seed = null;// 输入的种子
	public static HashSet<String> seed2 = new HashSet<String>();// 过滤后的种子,防止重复
	public static String password = "123456";
	public static String db = "jdbc:mysql://127.0.0.1:3306/mspider";
	public static ArrayList<String> blackList = new ArrayList<String>();
	// host黑名单，过滤掉这些host，出于对网站的尊重，我就不在这里举例了 O(∩_∩)O
	public static boolean notice = true;//数据库警告,有的mysql默认设置不支持中文
	//控制4个搜索引擎是否启用
	public static boolean baidu = true;
	public static boolean google = true;
	public static boolean qihu = true;
	public static boolean sousou = true;
	
}
