package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ��·վ��������
 * @author weiliu
 */
public class AdminLineSiteTable extends AdminRelationTableBase {
	
	/**
	 * ���캯��
	 * @param con
	 * 			���ݿ�����
	 */
	public AdminLineSiteTable(Connection con) {
		this.con = con;	// ���ݿ����ӳ�Ա������ʼ��	
		initPreparedStatement();	// ��ʼ��Ԥ�������
	}
	
	/**
	 * ��ʼ��Ԥ�������
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
	 * ����·վ��������һ����¼
	 * @param key
	 * 			��·id
	 * @param left
	 * 			����վ�㼯��
	 * @param right
	 * 			����վ�㼯��
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
	 * ɾ��һ����·��վ��Ĺ�ϵ
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
	 * ������·վ����е�վ�������У����ң�
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
