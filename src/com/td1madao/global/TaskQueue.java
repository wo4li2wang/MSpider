package com.td1madao.global;

import java.util.Iterator;
import java.util.TreeSet;

import com.td1madao.bean.UrlScoreBean;

/**
 * 这个就是任务队列(尼玛，分明是TreeSet)
 * 
 * 也是整个爬虫里最关键的东西啦！！！
 * 
 * 这东西是单例模式
 * 
 * 而且防止多线程同时往里塞东西
 * 
 * 没办法，任务不能重复，还可以排序，也只能用TreeSet了，叫队列听起来比较亲切嘛……
 * 
 * */
public class TaskQueue{
	
	
	 private static TaskQueue uniqueInstance = new TaskQueue();
	 private static TreeSet<UrlScoreBean> queue=new TreeSet<UrlScoreBean>();
//	 private static Queue<UrlScoreBean> queue = new LinkedList<UrlScoreBean>();
	 private TaskQueue(){	 }
	 public static TaskQueue getInstance() {
	             return uniqueInstance;
	 }
	 /**
	  * 这个是防止内存爆了，当然，打算后面看情况
	  * 如果内存足够适当变长，内存不够就适当缩小爬虫队列
	  * 
	  * */
	public synchronized void offer(UrlScoreBean u) {
		if (queue.size()<=GlobalVar.queueLength) {
			queue.add(u);  
		}
	}
	public synchronized UrlScoreBean poll() {
		if (!queue.isEmpty()) {
			return queue.pollFirst();
		}
		return null;
	}
	/**
	 * 如果任务队列太撑了
	 * 就提高过滤要求,清理垃圾
	 * 这个过滤要求为选择大于当前队列前75%的
	 * 否则队列满了就浪费爬虫爬到的东西了
	 * 
	 * 给方法起名字啥的最头疼了，干脆叫李狗蛋1，李狗蛋2……李狗蛋n，好了
	 * */
	public synchronized double getHigherQuality() {
		
		int nowLength=TaskQueue.getInstance().size();//当前任务数量
		int maxLength=GlobalVar.queueLength;//任务数量上限
		int expectPos=(int)(maxLength*GlobalVar.filterPos);//期望的过滤位置
		int times=nowLength-expectPos;
		if (times<0) {//虽然按道理来说这种情况不可能，但毕竟多线程，以防万一越界了
			return 0;
		}
		
		for(Iterator<UrlScoreBean> iterator = queue.iterator() ;iterator.hasNext();){
			if (times<=0) {
				return iterator.next().getScore();//找到这个位置的评分，返回
			}
			iterator.next();
			iterator.remove();
			times--;
		}
		return 0;//按道理来说这种情况也是不可能的
	}
	
	/**
	 * 这个长度其实可以不同步的，后台线程不需要太精确的长度
	 * */
	public synchronized int size() {
		return queue.size();
	}
}
