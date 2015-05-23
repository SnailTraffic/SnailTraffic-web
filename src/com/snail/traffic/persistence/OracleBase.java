package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ���ݿ���࣬��֤һ���û�ֻ����һ�����ݿ�����
 * 
 * @author weiliu
 *
 */
public class OracleBase {
	
	private String driver = "oracle.jdbc.driver.OracleDriver";	// ������
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:test";	// ���ݿ��ַ
	private String user = "g7";		// �û���
	private String pass = "123";	// ����
	private Connection conn;// ����
	
	/**
	 * ���캯��
	 * @param paramFile
	 * 				�����ļ���
	 * @throws Exception
	 */
	public OracleBase() {
		try {
			Class.forName(driver);	
			System.out.println("��ʼ�����������ݿ⣡");
			
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
	 * ��ȡ���ݿ�����
	 * @param paramFile
	 * @return Connection
	 */
	public Connection getConnection(){
		if( conn != null)
			System.out.println("�������ݿ�ɹ���");
		return conn;
	}
}

