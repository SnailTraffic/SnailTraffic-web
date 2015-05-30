package com.snail.traffic.persistence.admin;

import java.sql.Connection;
import java.sql.SQLException;

public class AdminSiteToLineTable extends BaseAdminRelationTable {

	/**
	 * constructor
	 * @param con
	 */
	public AdminSiteToLineTable(Connection con) {
		this.con = con;
		initPreparedStatement();
	}

	protected void initPreparedStatement() {
		try {
			String insertSql = "INSERT INTO sitetoline VALUES(?,?,?)";
			String updateLidSeqSql = "UPDATE sitetoline SET LLidseq=?, RLidseq=? WHERE sid=?";
			String deleteSql = "DELETE FROM sitetoline WHERE sid=?";
			
			pre_insert = con.prepareStatement(insertSql);
			pre_update = con.prepareStatement(updateLidSeqSql);
			pre_delete = con.prepareStatement(deleteSql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * delete a record
	 *
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
	  * add a record to (site to line) table
	  * @param sid
	  * 		site id
	  * @param left
	  * 		left line id
	  * @param left
	  * 		right line id
	  */
	public boolean addKeyToValue(int sid, String left, String right) {
		try {	
			pre_insert.setInt(1, sid);
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
	 * update a record on (site to line) table
	 * @param sid
	 * @param left
	 * @param right
	 */
	public boolean updateKeyToValue(int sid, String left, String right) {
		try{
			pre_update.setString(1, left);
			pre_update.setString(2, right);
			pre_update.setInt(3, sid);
			pre_update.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
