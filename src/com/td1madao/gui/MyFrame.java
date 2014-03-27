package com.td1madao.gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.td1madao.global.GlobalVar;
/**
 * 最简单的界面
 * */
public class MyFrame extends JFrame{
	/**
	 * 
	 */
	public static FileWriter fwFileWriter=null;
	public static BufferedWriter bWriter=null;
	private static final long serialVersionUID = 3216185994578075384L;
	public static boolean pause=false;
	JLabel te=new JLabel("爬虫种子来源引擎：");
	JLabel te2=new JLabel("相关页面URL，多个逗号隔开(可空)：");
	JLabel te3=new JLabel("搜索语句(可空，填写的话就是正常引擎搜索，支持搜索语法)");
	JCheckBox jCheckBox0 = new JCheckBox("百度",true);
	JCheckBox jCheckBox1 = new JCheckBox("360",true);
	JCheckBox jCheckBox2 = new JCheckBox("搜搜",true);
	JCheckBox jCheckBox3 = new JCheckBox("谷歌",true);
	
	
	
	static JButton yesButton=new JButton(); 
	JTextField keyWord=new JTextField();
	JTextField search=new JTextField();
	JTextField user=new JTextField();
	JTextField password=new JTextField();
	JTextField database=new JTextField();
	JTextField seed=new JTextField(20);
	JLabel keyWordJLabel=new JLabel("输入关键词(空格分开):");
	JLabel userJLabel=new JLabel("输入用户名:");
	JLabel passwordJLabel=new JLabel("输入密码:");
	JLabel databaseLabel=new JLabel("输入数据库名:");
	JPanel p1=new JPanel();
	JPanel p3=new JPanel();
	JPanel p2=new JPanel();
	JPanel p4=new JPanel();
	JPanel p5=new JPanel();
	JPanel p6=new JPanel();
	JPanel p7=new JPanel();
	static TextArea t2=new TextArea();
	boolean alreadyrun=false;
public MyFrame() {
	try {
		fwFileWriter=new FileWriter("data.log");
		bWriter=new BufferedWriter(fwFileWriter);
		bWriter.write(new Date().toString());
		bWriter.newLine();
	} catch (Exception e) {
		e.printStackTrace();
	}
	search.setText("MSpider 天地一MADAO");
	setSize(480,420);
	setResizable(false);
	t2.setEditable(false);
	setLayout(new FlowLayout());
	user.setText(GlobalVar.username);
	password.setText(GlobalVar.password);
	database.setText(GlobalVar.db);
	this.addWindowListener(new WindowListener() {
		@Override
		public void windowOpened(WindowEvent e) {
		}
		
		@Override
		public void windowIconified(WindowEvent e) {
			
		}
		
		@Override
		public void windowDeiconified(WindowEvent e) {
			
		}
		
		@Override
		public void windowDeactivated(WindowEvent e) {
			
		}
		
		@Override
		public void windowClosing(WindowEvent e) {
			try {			fwFileWriter.close();			bWriter.close();			} catch (Exception e2) {}
			System.exit(0);
		}
		
		@Override
		public void windowClosed(WindowEvent e) {
			
		}
		
		@Override
		public void windowActivated(WindowEvent e) {
			
		}
	});
	setTitle("天地一MADAO小工具");
	
	keyWord.setText("MSpider");
	p1.setLayout(new BorderLayout());
	p6.setLayout(new BorderLayout());
	p7.setLayout(new BorderLayout());
	
	p6.add(keyWordJLabel,BorderLayout.NORTH);
	p6.add(keyWord);
	p7.add(te3,BorderLayout.NORTH);
	p7.add(search);
	p1.add(p6,BorderLayout.NORTH);
	p1.add(p7);
	p1.add(t2,BorderLayout.SOUTH);
	yesButton.setText("爬吧！");
	yesButton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (alreadyrun) {
				pause=true;
				alreadyrun=false;
				yesButton.setText("继续");
			}
			else if ((!(GlobalVar.baidu||GlobalVar.google||GlobalVar.sousou||GlobalVar.qihu))&&seed.getText().length()<4) {
				Trace("如果不启动搜索引擎，至少给个种子链接吧？");
			}
			else if(keyWord.getText().equals("")){
				Trace("请输入关键字");
			}
			else if(user.getText().equals("")){
				Trace("请输入数据库账号");
			}
			else if(password.getText().equals("")){
				Trace("请输入数据库密码");
			}
			else if(database.getText().equals("")){
				Trace("请输入你要写入的数据库，要自己新建");
			}
			else if (yesButton.getText().equals("继续")) {
				
				NoGui.notifys();
				yesButton.setText("暂停");
				pause=false;
				alreadyrun=true;
			} 
			else
			{
				
				boolean b=NoGui.getInstance().init(keyWord.getText());
				if (!b) {
					JOptionPane.showConfirmDialog(null, "你输入的表达式不合法！","提示:", JOptionPane.OK_OPTION);
					return;
				}
				GlobalVar.seed=seed.getText();//输入了搜索内容
				String temp=search.getText().trim();
				if (temp.length()>0) {
					while(temp.contains("  ")){
					temp=temp.replaceAll("  ", " ");
					}
					temp=temp.replaceAll(" ", "+");
					GlobalVar.searchCont=new String(temp);
				}
				NoGui.getInstance().start();
				alreadyrun=true;
				yesButton.setText("暂停");
			}
		}
	});
	add(p1);
	
	p5.add(te2);
	p5.add(seed);
	add(p5);
	
	jCheckBox0.addItemListener(new ItemListener() {
		JCheckBox jCheckBox;
		@Override
		public void itemStateChanged(ItemEvent e) {
			 jCheckBox = (JCheckBox) e.getSource();
			 if (jCheckBox.isSelected()) {
				 GlobalVar.baidu=true;
			 }else {
				 GlobalVar.baidu=false;
			}
		}
	});
	jCheckBox1.addItemListener(new ItemListener() {
		JCheckBox jCheckBox;
		@Override
		public void itemStateChanged(ItemEvent e) {
			 jCheckBox = (JCheckBox) e.getSource();
			 if (jCheckBox.isSelected()) {
				 GlobalVar.qihu=true;
			 }else {
				 GlobalVar.qihu=false;
			}
		}
	});
	jCheckBox2.addItemListener(new ItemListener() {
		JCheckBox jCheckBox;
		@Override
		public void itemStateChanged(ItemEvent e) {
			 jCheckBox = (JCheckBox) e.getSource();
			 if (jCheckBox.isSelected()) {
				 GlobalVar.sousou=true;
			 }else {
				 GlobalVar.sousou=false;
			}
		}
	});
	jCheckBox3.addItemListener(new ItemListener() {
		JCheckBox jCheckBox;
		@Override
		public void itemStateChanged(ItemEvent e) {
			 jCheckBox = (JCheckBox) e.getSource();
			 if (jCheckBox.isSelected()) {
				 GlobalVar.google=true;
			 }else {
				 GlobalVar.google=false;
			}
		}
	});
	
	
	p4.add(jCheckBox0);
	p4.add(jCheckBox1);
	p4.add(jCheckBox2);
	p4.add(jCheckBox3);
	add(p4);
	
	
	p3.add(userJLabel);
	p3.add(user);
	p3.add(passwordJLabel);
	p3.add(password);
	add(p3);
	p2.add(databaseLabel);
	p2.add(database);
	p2.add(yesButton);
	add(p2);
	
	Trace("使用方法：\n确定你的数据库是存在的！\n，输入关键词、账户、密码、数据库名、爬信息！\n技巧:通过括号可以定义关键词的权重和是否必须存在 \n\n比如\"JAVA 本科生 暑假 实习(4,true) 2014\"\n\n表示实习的权重为4，且必须在文章中出现,默认词的权重为1\n格式务必要正确，括号内只能为(正实数,[true/false])");

	setVisible(true);
	repaint();
}

public static void Trace(String s){
	t2.append("\n"+s);
	try {
		bWriter.append(s);
		bWriter.newLine();
		bWriter.flush();
	} catch (Exception e) {
		e.printStackTrace();
	}
}
public static void main(String[] args) {
	new MyFrame();
}
}