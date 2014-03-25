package com.td1madao.threads;

import java.util.Iterator;

import com.td1madao.bean.UrlScoreBean;
import com.td1madao.db.DBOperator;
import com.td1madao.global.GlobalVar;
import com.td1madao.global.TaskQueue;
import com.td1madao.gui.MyFrame;
/**
 * 爬虫的使命就是
 * 得任务
 * 爬所有链接
 * 过滤
 * 扔进队列
 * 
 * M其实想表示Madao,不是SM那个M……^_^
 * 
 * */
public class MSpider extends Thread {
	 public volatile boolean flag = true;
	 int num=0;
	   private static Object obj = new Object();
	 public MSpider(int num) {
		 this.num=num;
	}
	 
	/**
	 * 其实感觉没必要用共享变量让它停
	 * 死循环就行
	 * 
	 * 算了，还是写吧，免得显得不正规
	 * 
	 * 毕竟操纵着数据库和队列呢
	 * 
	 * */
	@Override
	public void run() {
//		System.out.println("爬虫线程"+num+"启动");
		MyFrame.Trace("爬虫线程"+num+"启动");
		while (flag) {//目前还没做让爬虫停下来的功能，
			if (MyFrame.pause) {
			    synchronized (obj) {
		            try {
		                obj.wait();
		            } catch (InterruptedException e) {
		                System.out.println(getName() + " Test Thread Interrupted");
		            }
		        }
			}
			UrlScoreBean usb=TaskQueue.getInstance().poll();//得到相关度最高的任务
			 Iterator<String> itera	 = usb.child.iterator();//所有的链接
			  while (itera.hasNext()) {
				  if (MyFrame.pause) {
					    synchronized (obj) {
				            try {
				                obj.wait();
				            } catch (InterruptedException e) {
				                System.out.println(getName() + " Test Thread Interrupted");
				            }
				        }
					}
				UrlScoreBean tempbean=  SpiderUtil.work(itera.next());
				
				
				if (Double.isNaN(tempbean.getScore())||tempbean.getScore()<=GlobalVar.filterScore||GlobalVar.blackList.contains(tempbean.getHost()))
				{
					continue;//没用或价值不高或者在黑名单内
				}
				
				//得到关联度最高的host
				if (tempbean.getScore()>GlobalVar.maxScore) {
					GlobalVar.maxScore=tempbean.getScore();
					GlobalVar.maxHost=tempbean.getHost();
				}
				
				  TaskQueue.getInstance().offer(tempbean);//貌似有价值的东东，偷摸扔进队列里
				  if(DBOperator.getInstance().inputRecord(tempbean))//丢到数据库里面
//				  System.out.println(">爬虫"+num+"填入数据:"+tempbean.getScore()+"/"+tempbean.getHost());
					  MyFrame.Trace(">来自爬虫"+num+"{地址:"+tempbean.getUrl()+",关联度:"+tempbean.getScore()+"}");
//				  System.out.println("OK");
			  }
		}//干不完的活啊，除非队列里面的任务空了，否则……
//		System.out.println("爬虫线程关闭");
	}

	public void toNotify() {
		  synchronized (obj) {
			  obj.notifyAll();
	        }
	}
}
