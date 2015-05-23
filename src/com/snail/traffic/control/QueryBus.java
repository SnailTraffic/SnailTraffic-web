package com.snail.traffic.control;

import java.sql.Connection;

import com.snail.traffic.persistence.InfoStruct;
import com.snail.traffic.persistence.OracleBase;

/**
 * 公交查询类，
 * 包括站点查询、线路查询，换乘查询与一键回家
 * @author weiliu
 *
 */
public class QueryBus {
	private static OracleBase oracle = new OracleBase();	// 数据库对象
	private static Connection con = oracle.getConnection();	// 获取数据库连接
	
	/**
	 * 站点查询API
	 * @param sitename
	 * 				站点名
	 * @return 查询结果map
	 * 			类型：EnumMap<ListEnum, String[]>
	 */
	public static InfoStruct queryBusSite(String sitename) {
		SelectBase selectSite = new SelectBusSite(con);		// 声明定义一个查询对象
		return selectSite.query(sitename);	// 返回一个查询结果map
	}
	
	public static InfoStruct queryBusLine(String linename) {
		SelectBase selectLine = new SelectBusLine(con);		// 声明定义一个查询对象
		return selectLine.query(linename);	// 返回一个查询结果map
	}	
	
	public static void main(String[] args) {
		// 汉口火车站
//		ArrayStruct emap = queryBusSite("汉口火车站");
//		
//		String[] earr = emap.get(true);
//		String[] err = emap.get(false);
//		
//		if(earr != null)	
//			System.out.println(earr.length);
//		else
//			System.out.println("左边为空");
//		
//		if(err != null)
//			System.out.println(err.length);
//		else
//			System.out.println("右边为空");
		
		InfoStruct ww = queryBusLine("ed");
		String[] e = ww.get(true);
		System.out.println(e.length);
	}
}
