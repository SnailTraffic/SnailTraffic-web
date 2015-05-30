package com.snail.traffic.control.query;

import java.sql.Connection;
import java.util.Vector;

import com.snail.traffic.container.info.InfoStruct;
import com.snail.traffic.persistence.OracleBase;

public class QueryBusAPI {
	private static OracleBase oracle = new OracleBase();
	private static Connection con = oracle.getConnection();
	private static SelectBusSite selectSite = new SelectBusSite(con);
	private static SelectBusLine selectLine = new SelectBusLine(con);
	private static SelectTransfer selectTran = new SelectTransfer(con);
	
	/**
	 * query site information
	 * @param siteName
	 * 			site name
	 * @return InfoStruct
	 * 			return InfoSiteStruct or null
	 */
	public static InfoStruct queryBusSite(String siteName) {
		return selectSite.query(siteName.trim());
	}
	
	/**
	 * query line information
	 * @param lineName
	 * @return
	 */
	public static InfoStruct queryBusLine(String lineName) {
		return selectLine.query(lineName.trim());
	}

	/**
	 * fuzzySearch
	 * @param partName
	 * @return
	 */
	public static Vector<String> fuzzySearch(String partName) {
		return selectSite.fuzzySearch(partName.trim());
	}

	/**
	 * query Transfer
	 * @param start
	 * @param end
	 * @return
	 */
	public static InfoStruct queryTransfer(String start, String end) {
		return selectTran.query(start.trim(), end.trim());
	}
	
	public static void main(String[] args) {
	    // InfoStruct v = queryBusSite("建设大道双墩");
	    // InfoStruct v = queryBusLine("1路");
	     InfoStruct v = queryTransfer("建设大道双墩", "汉黄路岱家山");
	}
}
