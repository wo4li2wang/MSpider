package com.td1madao.threads;

import javax.swing.JOptionPane;

import com.td1madao.global.GlobalVar;
import com.td1madao.global.TaskQueue;
import com.td1madao.gui.MyFrame;

/**
 * 名字虽然很俗
 * 但它是个很重要的幕后黑手——后台线程
 * 肩负着垃圾监控/清理/控制搜索引擎等重大功能
 * 
 * 当然，它也是单例，不可能一群垃圾回收线程吧(⊙o⊙)
 * 
 * 任务队列如果数量超过90%
 * 说明要求太低了，反正是浪费，就提高一下要求，过滤掉相关度低的爬虫结果 ，也执行清理垃圾(简单垃圾回收吧)
 * 
 * 如果任务量小于30%，说明爬虫闲得慌，门槛降为0，只要相关就要(咋感觉跟某些公司招本科实习生似的，说了全是泪啊)
 * 顺便启动搜索引擎进程，继续搞种子来
 * 
 * 任务量大于50%，说明负载正好，就让搜索引擎线程滚掉好了
 * 
 * */
public class DaemonThread extends Thread {
	int taskNum=0;
int queueLen=0;
boolean stop=false;
private static Object obj = new Object();
	private static DaemonThread uniqueInstance = new DaemonThread();
	private DaemonThread(){}
	public static DaemonThread getInstance() {
        return uniqueInstance;
}
	
	public void run() {
		MyFrame.Trace("后台线程启动");
//		System.out.println("后台线程启动");
		while (true) {
			if (MyFrame.pause) {
			    synchronized (obj) {
		            try {
		                obj.wait();
		            } catch (InterruptedException e) {
		                System.out.println(getName() + " Test Thread Interrupted");
		            }
		        }
			}
			if (taskNum!=TaskQueue.getInstance().size()) {
				taskNum=TaskQueue.getInstance().size();
//				System.out.println("当前任务数："+taskNum+"/"+GlobalVar.queueLength);
				MyFrame.Trace("当前任务数："+taskNum+"/"+GlobalVar.queueLength);
			}
			queueLen=TaskQueue.getInstance().size();
			double percentage=(double)queueLen/GlobalVar.queueLength;
			if (percentage<=0.2&&GlobalVar.maxHost!=null) {//任务太少
				MyFrame.Trace("存在最大host"+GlobalVar.maxHost);
				//启动引擎线程，降低成本
				try {
					MEngine.getInstance().start();//尝试启动引擎
				} catch (Exception e) {
				}
				GlobalVar.filterScore=0;
			}else if (percentage<=0.5) {
				MSpider.toNotify();//任务多了就唤醒线程
				//关闭引擎线程
				MEngine.getInstance().flag=false;//共享变量，自生自灭
			}else if(percentage>=0.9){
				MSpider.toNotify();//任务多了就唤醒线程
				//提高成本，清理垃圾任务
				GlobalVar.filterScore=TaskQueue.getInstance().getHigherQuality();
				//其实也可以考虑给队列扩容，当然是我后面的打算了
			}
			for (int i = 0; i < GlobalVar.spiderState.length; i++) {
				stop|=GlobalVar.spiderState[i];
			}
			if (stop) {
				JOptionPane.showConfirmDialog(null, "实在爬不到相关的了，想要更多资源就填写相关URL或者修改下搜索语句","提示:", JOptionPane.OK_OPTION);
				try {MyFrame.fwFileWriter.close();MyFrame.bWriter.close();} catch (Exception e2) {}
				System.exit(0);
			}
			yield();
			try {
				wait(1000);
			} catch (Exception e) {
			}
			}
		}
	public void toNotify() {
		  synchronized (obj) {
	            obj.notify();
	        }
	}
	}
