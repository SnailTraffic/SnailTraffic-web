package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminSiteLineTable extends AdminRelationTableBase {
	
	protected static PreparedStatement pre_updateL = null;	// 更新数据预编译
	protected static PreparedStatement pre_updateR = null;	// 更新数据预编译
	
	/**
	 * 构造函数
	 * @param con
	 * 			数据库连接
	 */
	public AdminSiteLineTable(Connection con) {
		this.con = con;	// 数据库连接成员变量初始化		
		initPreparedStatement();
	}
	
	/**
	 * 初始化预编译语句
	 */
	protected void initPreparedStatement() {
		try {
			String insertsql = "insert into SITETOLINE values(?,?,?)";			
			String updateLRlidseq = "update SITETOLINE SET LLidseq=?, RLidseq=? WHERE sid = ?";	
			String deletesql = "delete FROM SITETOLINE WHERE sid = ?";
			
			pre_insert = con.prepareStatement(insertsql);			
			pre_update = con.prepareStatement(updateLRlidseq);				
			pre_delete = con.prepareStatement(deletesql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	 /**
	  * 向数据库站点线路表中添加一行站点线路记录
	  * @param lid
	  * 			站点id
	  * @param Llidseq
	  * 			左行线路id集合
	  * @param Rlidseq
	  * 			右行线路id集合
	  */
	public void addKeyToValue(int key, String left, String right) {
		try {	
			pre_insert.setInt(1, key);
			pre_insert.setString(2, left);
			pre_insert.setString(3, right);
			pre_insert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * 根据sid删除站点线路关系
	 */
	public void deleteKey(int key) {
		try {
			pre_delete.setInt(1, key);
			pre_delete.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新站点线路表
	 */
	public void updateKeyToValue(int key, String left, String right) {
		try{
			pre_update.setInt(1, key);
			pre_update.setString(2, left);
			pre_update.setString(3, right);
			pre_update.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
