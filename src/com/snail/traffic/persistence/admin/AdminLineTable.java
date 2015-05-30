package com.snail.traffic.persistence.admin;

import com.snail.traffic.container.info.InfoLineStruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminLineTable extends BaseAdminInfoTable {

	private PreparedStatement pre_selLineInfo = null;

	/**
	 * constructor
	 * @param con
	 */
	public AdminLineTable(Connection con) {
		this.con = con;
		initPreparedStatement();
	}

	@Override
	protected void initPreparedStatement() {
		try {
			String insertSql = "INSERT INTO lineinfo VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			String deleteSql = "DELETE FROM lineinfo WHERE lname=?";
			String getLidSql = "SELECT lid FROM lineinfo WHERE lname=?";
			String updateSql = "UPDATE lineinfo SET lname=?,linterval=?,lfirstopen=?"
							+ ",llastopen=?,lfirstclose=?,llastclose=?"
							+ ",lprice=?,lcardprice=?,lcompany=?, remark=?"
							+ " WHERE lname=?";

			String selLineName = "SELECT lname FROM LineInfo WHERE lid = ?";
			String selLineInfo = "SELECT lname, LINTERVAL"
								+ ", LFIRSTOPEN, LLASTOPEN, LFIRSTCLOSE, LLASTCLOSE"
								+ ", LPRICE, LCARDPRICE, LCOMPANY, remark"
								+ " FROM LineInfo"
								+ " WHERE lname = ?";
			
			this.pre_insert = con.prepareStatement(insertSql);
			this.pre_delete = con.prepareStatement(deleteSql);
			this.pre_getId =  con.prepareStatement(getLidSql);
			this.pre_update = con.prepareStatement(updateSql);
			this.pre_getName = con.prepareStatement(selLineName);

			pre_selLineInfo = con.prepareStatement(selLineInfo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	/**
	 * add an information of line
	 *
	 * @param lid
	 * @param lineName
	 * @param lInterVal
	 * @param lFirstOpen
	 * @param lLastOpen
	 * @param lFirstClose
	 * @param lLastClose
	 * @param lPrice
	 * @param lCardPrice
	 * @param lCompany
	 * @param remark
	 */
	public boolean addLineInfo(int lid
							, String lineName
							, String lInterVal
							, String lFirstOpen
							, String lLastOpen
							, String lFirstClose
							, String lLastClose
							, String lPrice
							, String lCardPrice
							, String lCompany
							, String remark) {
		try {
			pre_insert.setInt(1, lid);
			pre_insert.setString(2, lineName);
			pre_insert.setString(3, lInterVal);
			pre_insert.setString(4, lFirstOpen);
			pre_insert.setString(5, lLastOpen);
			pre_insert.setString(6, lFirstClose);
			pre_insert.setString(7, lLastClose);
			pre_insert.setString(8, lPrice);
			pre_insert.setString(9, lCardPrice);
			pre_insert.setString(10, lCompany);
			pre_insert.setString(11, remark);
			pre_insert.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * update information of a line according to lineName
	 * attention: lineName is the first parameter
	 *
	 * @param lineName
	 * @param newName
	 * @param lInterVal
	 * @param lFirstOpen
	 * @param lLastOpen
	 * @param lFirstClose
	 * @param lLastClose
	 * @param lPrice
	 * @param lCardPrice
	 * @param lCompany
	 * @param remark
	 * @return boolean
	 */
	public boolean updateLine(String lineName
							, String newName
							, String lInterVal
							, String lFirstOpen
							, String lLastOpen
							, String lFirstClose
							, String lLastClose
							, String lPrice
							, String lCardPrice
							, String lCompany
							, String remark) {
		try {
			pre_update.setString(1, newName);
			pre_update.setString(2, lInterVal);
			pre_update.setString(3, lFirstOpen);
			pre_update.setString(4, lLastOpen);
			pre_update.setString(5, lFirstClose);
			pre_update.setString(6, lLastClose);
			pre_update.setString(7, lPrice);
			pre_update.setString(8, lCardPrice);
			pre_update.setString(9, lCompany);
			pre_update.setString(10, remark);

			pre_update.setString(11, lineName);
			pre_update.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * delete a line according to lineName
	 *
	 * @param lineName
	 * 			line name
	 */
	@Override
	public boolean delete(Object lineName) {
		try {
			pre_delete.setString(1, (String)lineName);
			pre_delete.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * get line id according to line name
	 *
	 * @param lineName
	 * 			line name
	 * @return lid
	 * 			return line id ,if have not this line ,return 0
	 */
	@Override
	public int getId(Object lineName) {
		try {
			pre_getId.setString(1, (String)lineName);
			ResultSet rs = pre_getId.executeQuery();
			
			if(rs.next()) {
				int lid = rs.getInt(1);
				return lid;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * get number of table
	 *
	 * @return lineN
	 * 			number of line, if table is empty,return 0
	 */
	@Override
	public int getNumber() {

		String countSql = "SELECT COUNT(*) FROM lineinfo";
		try {
			Statement count = con.createStatement();
			ResultSet rs = count.executeQuery(countSql);

			if(rs.next()) {
				int lineN = rs.getInt(1);
				return lineN;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * get a new id
	 *
	 * @return newLid
	 *
	 */
	@Override
	public int getNewId() {
		int newLid = 0;
		String newLidSql = "SELECT MAX(lid) FROM lineinfo";
		try {
			Statement maxSid = con.createStatement();
			ResultSet rs = maxSid.executeQuery(newLidSql);
			
			if(rs.next())
				newLid = rs.getInt(1) + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newLid;
	}


	/**
	 * get line name according to line id
	 *
	 * @param lid
	 * @return lineName
	 * 		if have not this id ,this function return null
	 */
	@Override
	public String getName(int lid) {
		String lineName = null;
		try {
			this.pre_getName.setInt(1, lid);
			ResultSet rs = this.pre_getName.executeQuery();
			
			if(rs.next())
				lineName = rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lineName;// String / null
	}

	/**
	 * get information of line according to line name
	 *
	 * @param lineName
	 * 			not null
	 * @return lineInfo
	 *			if have not this line name ,return null
	 */
	public InfoLineStruct getInfo(String lineName) {
		InfoLineStruct lineInfo = null;
		try {
			pre_selLineInfo.setString(1, lineName);
			ResultSet rs = pre_selLineInfo.executeQuery();
			
			if(rs.next()) {
				lineInfo = new InfoLineStruct();
				lineInfo.name = rs.getString(1);
				lineInfo.setLineRange(rs.getString(2));
				lineInfo.setTime(rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
				lineInfo.setPrice(rs.getString(7), rs.getString(8));
				lineInfo.setOther(rs.getString(9), rs.getString(10));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lineInfo;
	}
}
