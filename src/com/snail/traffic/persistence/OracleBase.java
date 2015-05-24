package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库基类，保证一个用户只进行一次数据库连接
 * 
 * @author weiliu
 *
 */
public class OracleBase {
	
	private String driver = "oracle.jdbc.driver.OracleDriver";	// 驱动名
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:test";	// 数据库地址
	private String user = "g7";		// 用户名
	private String pass = "123";	// 密码
	private Connection conn;// 连接
	
	/**
	 * 构造函数
	 * @param paramFile
	 * 				配置文件名
	 * @throws Exception
	 */
	public OracleBase() {
		try {
			Class.forName(driver);	
			System.out.println("开始尝试连接数据库！");
			
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			this.conn = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 获取数据库连接
	 * @param paramFile
	 * @return Connection
	 */
	public Connection getConnection(){
		if( conn != null)
			System.out.println("连接数据库成功！");
		return conn;
	}
}

