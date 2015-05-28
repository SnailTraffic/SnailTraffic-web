package com.snail.traffic.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminNextSiteTable {
	protected Connection con = null;	// 数据库连接
	protected PreparedStatement pre_insert = null;	// 插入数据预编译
	
	/**
	 * 构造函数
	 * @param con
	 * 			数据库连接
	 */
	public AdminNextSiteTable(Connection con) {
		this.con = con;	// 数据库连接成员变量初始化	
		initPreparedStatement();	// 初始化预编译语句
	}
	
	/**
	 * 初始化预编译语句
	 */
	private void initPreparedStatement() {
		try {
			String insert = "insert into NEXTSITE values(?,?,?,?,?,?)";		
			pre_insert = con.prepareStatement(insert);					
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 增加一条两站点关系
	 * @param startsid
	 * 				起始站点id
	 * @param lid
	 * 			线路id
	 * @param endsid
	 * 			终止站点id
	 * @param time
	 * 			时间
	 * @param distance
	 * 				距离
	 */
	public void addKeyToValue(int startsid
							, int lid
							, int isleft
							, int endsid
							, int time
							, int distance) {
		try {
			pre_insert.setInt(1, startsid);		
			pre_insert.setInt(2, lid);
			pre_insert.setInt(3, isleft);
			pre_insert.setInt(4, endsid);
			pre_insert.setInt(5, time);			
			pre_insert.setInt(6, distance);	
			pre_insert.executeUpdate();		
		} catch (SQLException e) {
			//System.out.println(startsid + "," + lid + "," + isleft + "," + endsid);
			e.printStackTrace();
		}	
	}
	
	/**
	 * 根据站点名删除相关的关系
	 * @param name
	 */
	public void deleteSite(String name) {
		 CallableStatement proc = null;
	      try {
			proc = con.prepareCall("{ call G7.DELETE_NEXTSITE_STARTSITE(?) }");
		    proc.setString(1, name);
		    proc.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据线路名删除一条关系
	 * @param name
	 */
	public void deleteLine(String name) {
		 CallableStatement proc = null;
	      try {
			proc = con.prepareCall("{ call G7.DELETE_NEXTSITE_LINE(?) }");
		    proc.setString(1, name);
		    proc.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
