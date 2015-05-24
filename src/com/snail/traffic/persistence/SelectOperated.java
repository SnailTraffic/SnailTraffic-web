package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ��ѯ���ݿ���
 * 
 * ������ֻ������������ݿ���ͼ��ѯ
 * 
 * ֻ��Ҫ����ò�ѯ��API,���ڲ�ѯ���ʲô�ã���ѯ��õ�ʲô��ʽ
 * 
 * �Ҷ����ܣ�������
 * 
 * @author weiliu
 *
 */
public class SelectOperated {
	private Connection con = null;	// �������ݿ�����
	
	private PreparedStatement pre_viewSiteLine = null;	// ��ѯվ����·��ͼԤ����
	private PreparedStatement pre_viewLineSite = null;	// ��ѯ��·վ����ͼԤ����
	
	public SelectOperated(Connection con) {
		this.con = con;
		initPreparedStatement();	// ��ʼ��Ԥ����
	}
	
	/**
	 * ��ʼ��Ԥ�������
	 */
	private void initPreparedStatement() {
		try {
			String viewSiteLine = "SELECT llidseq, rlidseq FROM��View_SiteLine�� WHERE sname = ?";	
			String viewLineSite = "SELECT lsidseq, rsidseq FROM��View_LineSite�� WHERE lname = ?";

			pre_viewSiteLine = con.prepareStatement(viewSiteLine);
			pre_viewLineSite = con.prepareStatement(viewLineSite);	
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * ������ͼ����ȡ����������վ����·�����ַ���API
	 * @param sitename
	 * 				վ����
	 * @return siteline
	 * 				վ����·(������ߣ��ұ�)
	 */
	public TwoLongStruct getSiteLineSeq(String sitename) {
		
		TwoLongStruct siteline = new TwoLongStruct();	// ����վ����·�����ַ���
		
		try {
			pre_viewSiteLine.setString(1, sitename);
			
			ResultSet rs = pre_viewSiteLine.executeQuery();
			
			if(rs.next()) {
				siteline.put(true, rs.getString(1));	// ���������·�����ַ���
				siteline.put(false, rs.getString(2));	// �����ұ���·�����ַ���
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		return siteline;
	}
	
	/**
	 * ��ȡ��·վ�������վ����������������վ����������
	 * @param lname
	 * 			��·��
	 * @return linesite
	 * 				��·վ��ö��map����ߡ��ұߣ�
	 */
	public TwoLongStruct getLineSiteSeq(String lname) {
		
		TwoLongStruct linesite = new TwoLongStruct();	// ��ȡ����վ����·�����ַ���
		
		try {
			pre_viewLineSite.setString(1, lname);
			
			ResultSet rs = pre_viewLineSite.executeQuery();
			
			if (rs.next()) {
				linesite.put(true, rs.getString(1));	// �������վ�㼯���ַ���
				linesite.put(false, rs.getString(2));	// �����ұ�վ�㼯���ַ���
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		return linesite;
	}
}
