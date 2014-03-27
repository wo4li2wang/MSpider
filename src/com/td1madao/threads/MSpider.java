package com.td1madao.threads;

import java.util.ArrayList;
import java.util.List;

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
			UrlScoreBean usb=TaskQueue.getInstance().poll();//得到相关度最高的任务
			
			MyFrame.Trace("爬虫线程"+num+"开始工作");
			GlobalVar.spiderState[num]=false;//线程在工作
			if (MyFrame.pause||usb==null) {//让它停下来，或者没收到任何任务
			    synchronized (obj) {
		            try {
		            	GlobalVar.spiderState[num]=true;//线程阻塞(没有任务)
		            	MyFrame.Trace("爬虫线程"+num+"阻塞");
		                obj.wait();
		            } catch (InterruptedException e) {
		                System.out.println(getName() + "线程中断");
		            }
		        }
			}
			MyFrame.Trace("爬虫线程"+num+"得到主链接"+usb.getUrl());
			
			
				 if (usb.child==null) {//空链接
					 MyFrame.Trace("爬虫线程"+num+"发现"+usb.getUrl()+"没有子链接");
					 continue;
				}	
			
//			 Iterator<String> itera	 = usb.child.iterator();//所有的链接
				 //本来想用迭代器，但是涉及到修改元素的操作
				 
			 int father=usb.getChildLevel();
//			  while (itera.hasNext()) {
			 
			 List<String> list =  new  ArrayList<String>(usb.child);
			 
				  for (int j = 0; j < list.size(); j++) {
					
				  if (MyFrame.pause) {
					    synchronized (obj) {
				            try {
				                obj.wait();
				            } catch (InterruptedException e) {
				                System.out.println(getName() + "线程中断");
				            }
				        }
					}
				  
				  
				  String url=list.get(j);
					if (url.charAt(0)=='\"') {
						url=url.substring(1);
					}
					if (url.charAt(url.length()-1)=='\"') {
						url=url.substring(0,url.length()-1);
					}
					list.set(j, url);
				  
				  
				  
				UrlScoreBean tempbean=  SpiderUtil.work(list.get(j),father);
				MyFrame.Trace(">爬虫线程"+num+"得到子链接"+tempbean.getUrl());
				
				if (father>3&&(Double.isNaN(tempbean.getScore())||tempbean.getScore()<=GlobalVar.filterScore||GlobalVar.blackList.contains(tempbean.getHost())))
				{
					MyFrame.Trace(">爬虫线程"+num+"抛弃了链接"+tempbean.getUrl());
					continue;//没用或价值不高或者在黑名单内
				}
				
				//得到关联度最高的host
				if (tempbean.getScore()>GlobalVar.maxScore) {
					MyFrame.Trace("最大host是："+tempbean.getHost()+"得分是："+tempbean.getScore());
					GlobalVar.maxScore=tempbean.getScore();
					GlobalVar.maxHost=tempbean.getHost();
				}
				  TaskQueue.getInstance().offer(tempbean);//貌似有价值的东东，偷摸扔进队列里
				  if (tempbean.getScore()<GlobalVar.filterScore||tempbean.getScore()==0||Double.isNaN(tempbean.getScore())) {
					continue;
				}
				  
				  				  
				  MyFrame.Trace(">爬虫线程"+num+"打算写子链接"+tempbean.getUrl()+"(评分："+tempbean.getScore()+")");
				  if(DBOperator.getInstance().inputRecord(tempbean))//丢到数据库里面
					  MyFrame.Trace(">>爬虫线程"+num+"写入"+tempbean.getUrl()+",评分："+tempbean.getScore()+")");
			  }
		}//干不完的活啊，除非队列里面的任务空了，否则……
	}

	public static void toNotify() {
		  synchronized (obj) {
			  obj.notifyAll();
	        }
	}
}
