package com.snail.traffic.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminNextSiteTable {
	protected Connection con = null;	// ���ݿ�����
	protected PreparedStatement pre_insert = null;	// ��������Ԥ����
	
	/**
	 * ���캯��
	 * @param con
	 * 			���ݿ�����
	 */
	public AdminNextSiteTable(Connection con) {
		this.con = con;	// ���ݿ����ӳ�Ա������ʼ��	
		initPreparedStatement();	// ��ʼ��Ԥ�������
	}
	
	/**
	 * ��ʼ��Ԥ�������
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
	 * ����һ����վ���ϵ
	 * @param startsid
	 * 				��ʼվ��id
	 * @param lid
	 * 			��·id
	 * @param endsid
	 * 			��ֹվ��id
	 * @param time
	 * 			ʱ��
	 * @param distance
	 * 				����
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
	 * ����վ����ɾ����صĹ�ϵ
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
	 * ������·��ɾ��һ����ϵ
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
