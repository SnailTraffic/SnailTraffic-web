package com.snail.traffic.control;

import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;

import com.snail.traffic.persistence.InfoStruct;
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
	private static SelectBusSite selectSite = new SelectBusSite(con);		// ��������һ����ѯ����
	/**
	 * վ���ѯAPI
	 * @param sitename
	 * 				վ����
	 * @return ��ѯ���map
	 * 			���ͣ�EnumMap<ListEnum, String[]>
	 */
	public InfoStruct queryBusSite(String sitename) {	
		return selectSite.query(sitename.trim());	// ����һ����ѯ���map
	}
	
	/**
	 * ��·��ѯAPI
	 * @param linename
	 * @return
	 */
	public InfoStruct queryBusLine(String linename) {
		SelectBase selectLine = new SelectBusLine(con);		// ��������һ����ѯ����
		return selectLine.query(linename.trim());	// ����һ����ѯ���map
	}	
	
	// ģ����ѯ
	public static Vector<String> fuzzySearch(String input) {
		return selectSite.fuzzySearch(input.trim());
	}
	
	// ���˲�ѯ
	public Vector<TransitSchemeStruct> queryTransit(String start, String end) {
		SelectTransit selectTran = new SelectTransit(con);
		return selectTran.query(start, end);
	}
	
	public static void main(String[] args) {
		 
	        String ss = "���";  
	        while(ss != "q"){ //��ȡ�������е��ֽ�ֱ������ĩβ����1  
                //���黺��  
	        	   
                byte[] b = new byte[1024];  

                //��ȡ����  

                int n = 0;
				try {
					System.out.println("�����룺");  
					n = System.in.read(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  

                //ת��Ϊ�ַ���  

                 ss = new String(b,0,n);  
                
	    		Vector<String> tt = fuzzySearch(ss);
	    		for(int i = 0; i < tt.size(); i++) 
	    			System.out.println(tt.get(i)); 
	        }

	}
}
