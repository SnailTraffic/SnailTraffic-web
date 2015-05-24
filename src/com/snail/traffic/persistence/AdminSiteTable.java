package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * վ��������
 * @author weiliu
 */
public class AdminSiteTable extends AdminInfoTableBase {
	private PreparedStatement pre_selSiteName = null;	// ��ѯվ����Ԥ����
	private PreparedStatement pre_selFuzzyResult = null;	// ģ����ѯԤ����
	
	public AdminSiteTable(Connection con) {
		this.con = con;	// ���ݿ����ӳ�Ա������ʼ��
		initPreparedStatement();
	}
	
	/**
	 * ��ʼ��Ԥ�������
	 */
	protected void initPreparedStatement() {
		try {
			String insertsql = "insert into SITEINFO(sid,sname) values(?,?)";		
			String updatesql = "update SITEINFO SET sname = ? WHERE sname = ?";		
			String deletesql = "delete FROM SITEINFO WHERE sname = ?";
			String getsidsql = "SELECT sid FROM��SITEINFO��WHERE sname = ?";
			String getSiteName = "SELECT sname FROM��SiteInfo�� WHERE sid = ?";
			String getFuzzyResult = "SELECT sname FROM��SiteInfo�� WHERE sname LIKE ?";
			
			pre_insert = con.prepareStatement(insertsql);	
			pre_update = con.prepareStatement(updatesql);		
			pre_delete = con.prepareStatement(deletesql);
			pre_getId = con.prepareStatement(getsidsql);
			pre_selSiteName = con.prepareStatement(getSiteName);
			pre_selFuzzyResult = con.prepareStatement(getFuzzyResult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����վ����Ϣ
	 * @param sid
	 * 			վ��id
	 * @param sitename
	 * 			վ����
	 */
	public void addSiteInfo(int sid, String sitename) {
		try {
			pre_insert.setInt(1, sid);	
			pre_insert.setString(2, sitename);			
			pre_insert.executeUpdate();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����վ����
	 * @param sitename
	 * @param newname
	 */
	public void updateSite(String sitename, String newname) {
		try {
			pre_update.setString(1, newname);
			pre_update.setString(2, sitename);
			pre_update.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ͨ��վ����ɾ��վ��
	 * @param sitename
	 * 				վ����
	 */
	public void delete(Object sitename) {
		try {
			pre_delete.setString(1, (String)sitename);
			pre_delete.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ͨ��վ������ȡվ����
	 * @param sitename
	 * @return
	 */
	public int getId(Object sitename) {
		int sid = 0;	
		try {
			pre_getId.setString(1, (String)sitename);
			ResultSet rs = pre_getId.executeQuery();
			if(rs.next())
				sid = rs.getInt(1);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sid;
	}

	/**
	 * ��ȡվ�����վ������
	 * @return
	 */
	public int getNumber() {
		int siteN = 0;
		String countsql = "select count(*) from SITEINFO";
		try {
			Statement count = con.createStatement();
			ResultSet rs = count.executeQuery(countsql);	// ִ�в�ѯ
			
			if(rs.next())
				siteN = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return siteN;
	}

	/**
	 * ���һ���µ�վ����
	 * @return
	 */
	public int getNewId() {
		int newsid = 0;
		String newsidsql = "select max(sid) from SITEINFO";
		try {
			Statement maxsid = con.createStatement();
			ResultSet rs = maxsid.executeQuery(newsidsql);	// ִ�в�ѯ
			
			if(rs.next())
				newsid = rs.getInt(1) + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newsid;
	}

	/**
	 * ͨ��վ��id��ȡվ����
	 */
	public String getName(int id) {
		String sitename = null;
		try {
			pre_selSiteName.setInt(1, id);		
			ResultSet rs = pre_selSiteName.executeQuery();
			
			if(rs.next())
				sitename = rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sitename;	
	}
	
	/**
	 * ��ȡģ����ѯ���
	 * @param input
	 * @return
	 */
	public Vector<String> getFuzzyResult(String input) {
		Vector<String> fuzzyResult = new Vector<String>();
		try {
			input = "%" + input + "%";
			pre_selFuzzyResult.setString(1, input);	
			ResultSet rs = pre_selFuzzyResult.executeQuery();	// �õ������
			
			while(rs.next()) {
				fuzzyResult.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fuzzyResult;
	}
}