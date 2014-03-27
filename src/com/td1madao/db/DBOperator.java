package com.td1madao.db;

import java.sql.*;

import com.mysql.jdbc.MysqlDataTruncation;
import com.td1madao.bean.UrlScoreBean;
import com.td1madao.global.GlobalVar;
import com.td1madao.gui.MyFrame;
/**
 * 专门搞数据库的类
 * 
 * 这玩意也是单例模式
 * 
 * */
public class DBOperator {
	 private static DBOperator uniqueInstance = new DBOperator();
	private static  Connection conn=null;
	private static String sql = null;
	private static ResultSet rs = null;
	private static PreparedStatement pstmt=null; 
	private static Statement stmt=null; 
	private DBOperator(){}
	public static DBOperator getInstance() {

		String driver = "com.mysql.jdbc.Driver";
         try { 
        	 Class.forName(driver);
           conn = DriverManager.getConnection(GlobalVar.db, GlobalVar.username, GlobalVar.password);
           stmt=conn.createStatement();
         }catch(SQLException e)
         {
        	 MyFrame.Trace("请检查你输入的数据库是否有误");
         }
         catch(Exception e) {
             e.printStackTrace();
         } 
        return uniqueInstance;
}
//	public DBOperator() {}
	
	/**
	 * 我想以host作为表进行存储，但是.会引起歧义
	 * 这里把.全换成-
	 * */
	private static String hostFix(final String string) {
		return string.replace(".", "_");
	}
	/**
	 * 判断表是否存在，如果不存在，就新建表
	 * */
	private static void testTable(final String tableName){
		sql="CREATE TABLE IF NOT EXISTS `"+hostFix(tableName)+"` (  `Id` int(11) NOT NULL AUTO_INCREMENT,  `url` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',  `score` double NOT NULL DEFAULT '0',  `child` blob ,  PRIMARY KEY (`Id`))Engine =InnoDB DEFAULT CHARSET=latin1";
		try {
            stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 判断这个网址是否存在，如果存在，就true
	 * */
	private static boolean select(final UrlScoreBean bean) {
	         sql = "select * from "+hostFix(bean.getHost())+" where url= '"+bean.getUrl()+"'";
	        try {
	            rs = stmt.executeQuery(sql);
	            if(rs.next()){
	            	return true;
	            }
	        }catch (Exception e){
	           e.printStackTrace();
	        }
	        return false;
	    }
	/**
	 * 写入数据
	 * */
	private static void insert(UrlScoreBean bean) {
		sql="INSERT INTO `"+hostFix(bean.getHost())+"` VALUES (0,'"+bean.getUrl()+"',"+bean.getScore()+",?);";
	        try {
	        	pstmt = conn.prepareStatement(sql);
	        	 if (bean.child==null) {
	        		 pstmt.setObject(1,Types.BLOB);//子串可以空
				}else{
					pstmt.setObject(1,bean.child);
				}
		         pstmt .executeUpdate();          
	        }
	        catch(MysqlDataTruncation e){
	        	if (GlobalVar.notice) {
	        		System.err.println("请修改MySQL的 my.ini文件，去掉\"STRICT_TRANS_TABLES,\",默认字符集不支持中文,所以无法保存这类记录");
	        		MyFrame.Trace("你的数据库不支持中文，部分信息不能保存。请修改MySQL的 my.ini文件，去掉\"STRICT_TRANS_TABLES,\"");
	        		GlobalVar.notice=false;
				}
	        }
	        catch (Exception e){
	           e.printStackTrace();
	        }
	    }	
	
	/**
	 * 对外开放的方法，直接把爬虫扔进数据库
	 * 
	 * 如果没表就建表
	 * 如果存在则不管
	 * 如果不存在就插入数据
	 * 数据结构 
	 * 
	 * host{
	 * score
	 * url
	 * child
	 * }
	 * */
	public synchronized  boolean inputRecord(UrlScoreBean bean) {
		boolean succ=false;
		testTable(bean.getHost());
		if (!select(bean)) {
			insert(bean);
			succ=true;
		}
		return succ;
	}
	
public static void main(String[] args){
	
//DBOperator dbOperator=getInstance(); 
//dbOperator.inputRecord(new UrlScoreBean(0.3, "www.hoho.com",new HashSet<String>()));
} 
}