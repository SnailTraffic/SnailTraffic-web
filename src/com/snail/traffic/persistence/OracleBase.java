package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleBase {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:test";
	//private String url = "jdbc:oracle:thin:@DELLT410:1521:DB805";
	private String user = "g7";
	//private String user = "Group7";
	private String pass = "123";
	private Connection conn;

	public OracleBase() {
		try {
			Class.forName(driver);	
			System.out.println("Start connecting to the database!");
			
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
	 * get connection
	 * @return conn
	 */
	public Connection getConnection(){
		if( conn != null)
			System.out.println("Connected Successfully");
		return conn;
	}
}

