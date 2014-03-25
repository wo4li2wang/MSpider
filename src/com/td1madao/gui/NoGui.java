package com.td1madao.gui;


import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.td1madao.bean.KeyWord;
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
	KeyWord args2[]=null;
 boolean pause=false;
 public static MSpider[] array=new MSpider[GlobalVar.spiderNum];//后面还会放别的线程……也有可能
private static NoGui uniqueInstance = new NoGui();
	
	private NoGui() {
	}
	
	public static NoGui getInstance() {
        return uniqueInstance;
}
	
	/**
	 * 把关键词语法保存为KeyWord结构，很重要的部分
	 * 
	 * */
	public  boolean init(String s) {
		ArrayList<KeyWord> als=new ArrayList<KeyWord>();
		String s2[]=s.split(" ");
		ArrayList<String> aList=new ArrayList<String>();
		for (int i = 0; i < s2.length; i++) {
			if (s2[i].length()!=0) {
				aList.add(s2[i].trim());
			}
		}
		//得到关键词
		String temp;
		for (int i = 0; i < aList.size(); i++) {
			KeyWord kw = null;
			temp=aList.get(i);
			
			String[] result = temp.split(",");
			int count = result.length - 1;
			
			if (temp.contains("(")&&temp.contains(")")&&count==1) {
				String name=temp.substring(0,temp.indexOf("("));
				String getString[]=temp.substring(temp.indexOf("(")+1,temp.lastIndexOf(")")).split(",");
				try {
					kw=new KeyWord(name, Double.parseDouble(getString[0]), Boolean.parseBoolean(getString[1]));
					if (Double.parseDouble(getString[0])<=0) {
						return false;
					}
				} catch (Exception e) {
					return false;
				}
			}
			else if (!temp.contains("(")&&!temp.contains(")")) {
				kw=new KeyWord(temp, 1,false);
			}
			else {
				return false;
			}
			//得到一个关键词
			als.add(kw);
		}
		args2= (KeyWord[])als.toArray(new KeyWord[als.size()]);
		return true;
	}
	public void run() {
		MyFrame.yesButton.setEnabled(false);
		if (args2.length==0||args2==null) {
			GlobalVar.keyStrings=new  KeyWord[]{new KeyWord("银魂",1, true),new KeyWord("坂田银时",5, false)};//这里也可以定义搜索关键词
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
