package com.snail.traffic.persistence.admin;

import java.sql.Connection;
import java.sql.SQLException;

public class AdminNextSiteTable extends BaseAdminTable {

    /**
     * constructor
     * @param con
     */
	public AdminNextSiteTable(Connection con) {
		this.con = con;
		initPreparedStatement();
	}
	
	/**
	 * initialize PreparedStatement
	 */
    @Override
	protected void initPreparedStatement() {
		try {
			String insert = "insert into NEXTSITE values(?,?,?,?,?,?)";		
			pre_insert = con.prepareStatement(insert);					
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean delete(Object input) {
		return true;
	}

    /**
     * add a record to next site table
     * @param start
     *          start site id
     * @param lid
     *          line id
     * @param left
     *          is left
     * @param end
     *          end site id
     * @param time
     * @param distance
     */
	public boolean addKeyToValue(int start
                            , int lid
							, int left
							, int end
							, int time
							, int distance) {
		try {
			pre_insert.setInt(1, start);
			pre_insert.setInt(2, lid);
			pre_insert.setInt(3, left);
			pre_insert.setInt(4, end);
			pre_insert.setInt(5, time);			
			pre_insert.setInt(6, distance);	
			pre_insert.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
