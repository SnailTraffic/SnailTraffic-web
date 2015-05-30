package com.snail.traffic.persistence.admin;

import java.sql.Connection;
import java.sql.SQLException;

public class AdminLineToSiteTable extends BaseAdminRelationTable {

	/**
	 * constructor
	 * @param con
	 */
	public AdminLineToSiteTable(Connection con) {
		this.con = con;
		initPreparedStatement();
	}

	@Override
	protected void initPreparedStatement() {
		try {
			String insertSql = "INSERT INTO linetosite VALUES(?,?,?)";
			String updateSidSeqSql = "UPDATE linetosite SET lsidseq=?, rsidseq=? WHERE lid=?";
			String deleteSql = "DELETE FROM linetosite WHERE lid = ?";
			
			pre_insert = con.prepareStatement(insertSql);
			pre_update = con.prepareStatement(updateSidSeqSql);
			pre_delete = con.prepareStatement(deleteSql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * delete a record
	 * delete a line on LineSite Table
	 * @param input
	 */
	@Override
	public boolean delete(Object input) {
		try {
			pre_delete.setInt(1, (Integer) input);
			pre_delete.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * add a record to lineSite table
	 * @param key
	 * 			line id
	 * @param left
	 * 			left sites
	 * @param right
	 * 			right sites
	 */
	@Override
	public boolean addKeyToValue(int lid, String left, String right) {
		try {
			pre_insert.setInt(1, lid);
			pre_insert.setString(2, left);			
			pre_insert.setString(3, right);
			pre_insert.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return false;
	}

	/**
	 * update line site table left and right
	 * @param lid
	 * @param left
	 * @param right
	 */
	@Override
	public boolean updateKeyToValue(int lid, String left, String right) {
		try{
			pre_update.setString(1, left);
			pre_update.setString(2, right);
			pre_update.setInt(3, lid);
			pre_update.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
