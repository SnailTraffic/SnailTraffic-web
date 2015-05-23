package com.snail.traffic.control;

import java.sql.Connection;

import com.snail.traffic.persistence.InfoStruct;
import com.snail.traffic.persistence.LineAllInfoStruct;
import com.snail.traffic.persistence.OracleBase;

/**
 * ������ѯ�࣬
 * ����վ���ѯ����·��ѯ�����˲�ѯ��һ���ؼ�
 * @author weiliu
 *
 */
public class QueryBus {
	private static OracleBase oracle = new OracleBase();	// ���ݿ����
	private static Connection con = oracle.getConnection();	// ��ȡ���ݿ�����
	
	/**
	 * վ���ѯAPI
	 * @param sitename
	 * 				վ����
	 * @return ��ѯ���map
	 * 			���ͣ�EnumMap<ListEnum, String[]>
	 */
	public InfoStruct queryBusSite(String sitename) {
		SelectBase selectSite = new SelectBusSite(con);		// ��������һ����ѯ����
		return selectSite.query(sitename);	// ����һ����ѯ���map
	}
	
	public InfoStruct queryBusLine(String linename) {
		SelectBase selectLine = new SelectBusLine(con);		// ��������һ����ѯ����
		return selectLine.query(linename);	// ����һ����ѯ���map
	}	
	
/*	public static void main(String[] args) {
		// ���ڻ�վ
//		ArrayStruct emap = queryBusSite("���ڻ�վ");
//		
//		String[] earr = emap.get(true);
//		String[] err = emap.get(false);
//		
//		if(earr != null)	
//			System.out.println(earr.length);
//		else
//			System.out.println("���Ϊ��");
//		
//		if(err != null)
//			System.out.println(err.length);
//		else
//			System.out.println("�ұ�Ϊ��");
		
		InfoStruct ww = queryBusLine("59·");
		String[] e = ww.get(true);
		System.out.println(ww.lineRange);	
		
		for(int i = 0; i < e.length; i++) {
			System.out.println(e[i]);	
		}
		
		System.out.println(e.length);
	}*/
}
