package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminSiteLineTable extends AdminRelationTableBase {
	
	protected static PreparedStatement pre_updateL = null;	// ��������Ԥ����
	protected static PreparedStatement pre_updateR = null;	// ��������Ԥ����
	
	/**
	 * ���캯��
	 * @param con
	 * 			���ݿ�����
	 */
	public AdminSiteLineTable(Connection con) {
		this.con = con;	// ���ݿ����ӳ�Ա������ʼ��		
		initPreparedStatement();
	}
	
	/**
	 * ��ʼ��Ԥ�������
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
	  * �����ݿ�վ����·�������һ��վ����·��¼
	  * @param lid
	  * 			վ��id
	  * @param Llidseq
	  * 			������·id����
	  * @param Rlidseq
	  * 			������·id����
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
	 * ����sidɾ��վ����·��ϵ
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
	 * ����վ����·��
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
