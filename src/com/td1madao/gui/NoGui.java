package com.td1madao.gui;


import javax.swing.JOptionPane;

import com.td1madao.global.GlobalVar;
import com.td1madao.global.TaskQueue;
import com.td1madao.threads.DaemonThread;
import com.td1madao.threads.MEngine;
import com.td1madao.threads.MSpider;

/**
 * GUI有点烦，想先拿非GUI的做测试
 * 
 * */
public class NoGui extends Thread{
 String args2[]=null;
 boolean pause=false;
 public static MSpider[] array=new MSpider[GlobalVar.spiderNum];//后面还会放别的线程……也有可能
private static NoGui uniqueInstance = new NoGui();
	
	private NoGui() {
	}
	
	public static NoGui getInstance() {
        return uniqueInstance;
}
	public  void init(String[] args) {
		args2=args;
	}
	public void run() {
		MyFrame.yesButton.setEnabled(false);
		if (args2.length==0||args2[0]=="") {
			GlobalVar.keyStrings=new String[]{"银魂","坂田银时"};//这里也可以定义搜索关键词
		}
		else {
			GlobalVar.keyStrings=args2.clone();
		}
		
		MEngine.getInstance().run();//这个是为了初始化得种子，千万不要start哦！
		if (TaskQueue.getInstance().size()==0) {
			JOptionPane.showConfirmDialog(null, "搜索失败！如果你用的是谷歌，请换用别的引擎试试！","提示:", JOptionPane.OK_OPTION);
			System.exit(0);
		}
		MyFrame.yesButton.setEnabled(true);
//		System.out.println("得到了"+TaskQueue.getInstance().size()+"个种子链接！");
		MyFrame.Trace("得到了"+TaskQueue.getInstance().size()+"个种子链接！");
		MEngine.getInstance().start();
		MEngine.getInstance().flag=false;
	    
	    for (int i = 0; i < array.length; i++) {
			array[i]=new MSpider(i);
			array[i].start();
		}
	    DaemonThread.getInstance().setDaemon(true);//这个是垃圾回收的后台线程
	    DaemonThread.getInstance().start();//后台线程
	}

	public static void notifys() {
		// TODO Auto-generated method stub
		array[0].toNotify();
		MEngine.getInstance().toNotify();
		 DaemonThread.getInstance().toNotify();
	}

}
