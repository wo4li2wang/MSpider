package com.td1madao.threads;

import java.util.ArrayList;

import com.td1madao.bean.UrlScoreBean;
import com.td1madao.db.DBOperator;
import com.td1madao.global.GlobalVar;
import com.td1madao.global.TaskQueue;
import com.td1madao.gui.MyFrame;
import com.td1madao.useEngine.FetchBaidu;

/**
 * 这个与其说是爬虫，倒不如说它是搜索引擎
 * 
 * 当然，它也是单例模式的
 * 
 * 它利用肚熊。谷歌、奇虎、搜搜四个引擎一起产生种子
 * 当然，队列满了它就一边凉快去了
 * 完全听从守护线程DaemonThread的调遣
 * 
 * 共享变量表示线程状态
 * 
 * */
public class MEngine extends Thread {
	public volatile boolean flag= false;
	private static Object obj = new Object();
	private static MEngine uniqueInstance = new MEngine();
	private MEngine(){}
	public static MEngine getInstance() {
        return uniqueInstance;
}
	
	@Override
	public void run() {
//		System.out.println("引擎线程启动");
		MyFrame.Trace("搜索引擎线程启动");
		flag=true;
		ArrayList<String> al=new ArrayList<String>();
		if (GlobalVar.baidu) {
			ArrayList<String> temp	=FetchBaidu.work(GlobalVar.maxHost);//百度
			if (temp!=null) {
				al.addAll(temp);
			}
		}
		if (GlobalVar.google) {
			ArrayList<String> temp	=FetchBaidu.work(GlobalVar.maxHost);//百度
			if (temp!=null) {
				al.addAll(temp);
			}
		}
		if (GlobalVar.sousou) {
			ArrayList<String> temp	=FetchBaidu.work(GlobalVar.maxHost);//百度
			if (temp!=null) {
				al.addAll(temp);
			}
		}	
		if (GlobalVar.qihu) {
			ArrayList<String> temp	=FetchBaidu.work(GlobalVar.maxHost);//百度
			if (temp!=null) {
				al.addAll(temp);
			}
		}
		UrlScoreBean usb[]=new UrlScoreBean[al.size()];
			for (int i = 0; i < al.size()&&flag; i++) {//flag=false表示让它停止工作
				
				if (MyFrame.pause) {
				    synchronized (obj) {
			            try {
			                obj.wait();
			            } catch (InterruptedException e) {
			                System.out.println(getName() + " Test Thread Interrupted");
			            }
			        }
				}
				usb[i]=SpiderUtil.work(al.get(i));
				

				if (Double.isNaN(usb[i].getScore())||usb[i].getScore()<=GlobalVar.filterScore||GlobalVar.blackList.contains(usb[i].getHost()))
				{
					continue;//没用或价值不高或者在黑名单内
				}
				
				//得到关联度最高的host，当然列入黑名单的除外
				if (usb[i].getScore()>GlobalVar.maxScore) {
					GlobalVar.maxScore=usb[i].getScore();
					GlobalVar.maxHost=usb[i].getHost();
				}				

				MyFrame.Trace(">来自搜索引擎{地址:"+usb[i].getUrl()+",关联度:"+usb[i].getScore()+"}");
				TaskQueue.getInstance().offer(usb[i]);//貌似有价值的东东，偷摸扔进队列里
				  DBOperator.getInstance().inputRecord(usb[i]);//丢到数据库里面
		}
			//正常结束
//			System.out.println("引擎线程关闭");
			MyFrame.Trace("搜索引擎线程关闭");
	}
	public void toNotify() {
		 synchronized (obj) {
	            obj.notify();
	        }
	}

}
