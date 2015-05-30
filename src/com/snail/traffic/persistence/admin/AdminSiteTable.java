package com.snail.traffic.persistence.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;


public class AdminSiteTable extends BaseAdminInfoTable {

	private PreparedStatement pre_selFuzzyResult = null;	// 模糊查询预编译

	/**
	 * constructor
	 * @param con
	 */
	public AdminSiteTable(Connection con) {
		this.con = con;
		initPreparedStatement();
	}

	@Override
	protected void initPreparedStatement() {
		try {
			String insertSql = "INSERT INTO siteinfo(sid,sname) VALUES(?,?)";
			String updateSql = "UPDATE siteinfo SET sname=? WHERE sname=?";
			String deleteSql = "DELETE FROM siteinfo WHERE sname=?";
			String getSidSql = "SELECT sid FROM siteinfo WHERE sname=?";
			String getSiteName = "SELECT sname FROM siteinfo WHERE sid=?";
			String getFuzzyResult = "SELECT sname FROM siteinfo WHERE sname LIKE ?";
			
			this.pre_insert = con.prepareStatement(insertSql);
			this.pre_update = con.prepareStatement(updateSql);
			this.pre_delete = con.prepareStatement(deleteSql);
			this.pre_getId = con.prepareStatement(getSidSql);
			this.pre_getName = con.prepareStatement(getSiteName);

			pre_selFuzzyResult = con.prepareStatement(getFuzzyResult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * add a site
	 * @param sid
	 * @param siteName
	 *
	 */
	public boolean addSiteInfo(int sid, String siteName) {
		try {
			pre_insert.setInt(1, sid);	
			pre_insert.setString(2, siteName);
			pre_insert.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * update site name
	 * @param siteName
	 * @param newName
	 */
	public boolean updateSite(String siteName, String newName) {
		try {
			pre_update.setString(1, newName);
			pre_update.setString(2, siteName);
			pre_update.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * delete a site according to site name
	 * @param siteName
	 *
	 */
	@Override
	public boolean delete(Object siteName) {
		try {
			pre_delete.setString(1, (String)siteName);
			pre_delete.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * get id of site according to site name
	 * @param siteName
	 * @return sid
	 * 			int or 0
	 */
	@Override
	public int getId(Object siteName) {
		try {
			pre_getId.setString(1, (String)siteName);
			ResultSet rs = pre_getId.executeQuery();

			if(rs.next()) {
				int sid = rs.getInt(1);
				return sid;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * get number of sites
	 * @return siteN
	 * 			int or 0
	 */
	@Override
	public int getNumber() {
		String countSql = "SELECT COUNT(*) FROM siteinfo";
		try {
			Statement count = con.createStatement();
			ResultSet rs = count.executeQuery(countSql);	// 执行查询
			
			if(rs.next()) {
				int siteN = rs.getInt(1);
				return siteN;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * get a new id from site table
	 * @return
	 */
	@Override
	public int getNewId() {
		String newSidSql = "SELECT MAX(sid) FROM siteinfo";
		int newSid = 0;
		try {
			Statement maxSid = con.createStatement();
			ResultSet rs = maxSid.executeQuery(newSidSql);	// 执行查询
			
			if(rs.next())
				 newSid = rs.getInt(1) + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newSid;
	}

	/**
	 * get site name according to site id
	 * @param id
	 * @return
	 */
	@Override
	public String getName(int id) {
		String siteName = null;
		try {
			this.pre_getName.setInt(1, id);
			ResultSet rs = this.pre_getName.executeQuery();
			
			if(rs.next())
				siteName = rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return siteName;
	}
	
	/**
	 * get fuzzy result
	 * @param input
	 * @return fuzzyResult
	 * 			Vector or null
	 */
	public Vector<String> getFuzzyResult(String input) {
		Vector<String> fuzzyResult = null;
		try {
			input = "%" + input + "%";
			pre_selFuzzyResult.setString(1, input);	
			ResultSet rs = pre_selFuzzyResult.executeQuery();	// 得到结果集

			fuzzyResult = new Vector<>();
			while(rs.next())
				fuzzyResult.add(rs.getString(1));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fuzzyResult;
	}
}