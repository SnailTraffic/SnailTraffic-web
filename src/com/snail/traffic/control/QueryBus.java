package com.snail.traffic.control;

import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;

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
	private static SelectBusSite selectSite = new SelectBusSite(con);		// 声明定义一个查询对象
	/**
	 * 站点查询API
	 * @param sitename
	 * 				站点名
	 * @return 查询结果map
	 * 			类型：EnumMap<ListEnum, String[]>
	 */
	public InfoStruct queryBusSite(String sitename) {	
		return selectSite.query(sitename.trim());	// 返回一个查询结果map
	}
	
	/**
	 * 线路查询API
	 * @param linename
	 * @return
	 */
	public InfoStruct queryBusLine(String linename) {
		SelectBase selectLine = new SelectBusLine(con);		// 声明定义一个查询对象
		return selectLine.query(linename.trim());	// 返回一个查询结果map
	}	
	
	// 模糊查询
	public static Vector<String> fuzzySearch(String input) {
		return selectSite.fuzzySearch(input.trim());
	}
	
	// 换乘查询
	public Vector<TransitSchemeStruct> queryTransit(String start, String end) {
		SelectTransit selectTran = new SelectTransit(con);
		return selectTran.query(start, end);
	}
	
	public static void main(String[] args) {
		 
	        String ss = "解放";  
	        while(ss != "q"){ //读取输入流中的字节直到流的末尾返回1  
                //数组缓冲  
	        	   
                byte[] b = new byte[1024];  

                //读取数据  

                int n = 0;
				try {
					System.out.println("请输入：");  
					n = System.in.read(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  

                //转换为字符串  

                 ss = new String(b,0,n);  
                
	    		Vector<String> tt = fuzzySearch(ss);
	    		for(int i = 0; i < tt.size(); i++) 
	    			System.out.println(tt.get(i)); 
	        }

	}
}
