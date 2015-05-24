package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 线路站点表管理类
 * @author weiliu
 */
public class AdminLineSiteTable extends AdminRelationTableBase {
	
	/**
	 * 构造函数
	 * @param con
	 * 			数据库连接
	 */
	public AdminLineSiteTable(Connection con) {
		this.con = con;	// 数据库连接成员变量初始化	
		initPreparedStatement();	// 初始化预编译语句
	}
	
	/**
	 * 初始化预编译语句
	 */
	protected void initPreparedStatement() {
		try {
			String insertsql = "insert into LINETOSITE values(?,?,?)";
			String updateLRsidseq = "update LINETOSITE SET lsidseq=?, rsidseq=? WHERE lid = ?";		
			String deletesql = "delete FROM LINETOSITE WHERE lid = ?";
			
			pre_insert = con.prepareStatement(insertsql);			
			pre_update = con.prepareStatement(updateLRsidseq);			
			pre_delete = con.prepareStatement(deletesql);			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向线路站点表中添加一个记录
	 * @param key
	 * 			线路id
	 * @param left
	 * 			左行站点集合
	 * @param right
	 * 			右行站点集合
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
	 * 删除一条线路与站点的关系
	 * @param lid
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
	 * 更新线路站点表中的站点编号序列（左右）
	 * @param lid
	 * @param lsidseq
	 * @param rsidseq
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
