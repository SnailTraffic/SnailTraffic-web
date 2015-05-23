package com.snail.traffic.control;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.snail.traffic.persistence.AdminSiteLineTable;
import com.snail.traffic.persistence.OracleBase;

/**
 * Ϊǰ���ṩһ�����ĺ�̨����ҵ��API��
 * 
 * ��Ҫ�����ǵ���Excel�ļ���
 * 
 * ��Ҫ����3��map��
 * 
 * ����վ����·map��Ҫ�ڶ���������·����ܵõ�����������
 * 
 * ���ͨ��ѭ��map������ݿ�
 */
public class Administration {
	private OracleBase oracle = new OracleBase();		// ���ݿ����
	private Connection con = oracle.getConnection();	// ��ȡ���ݿ�����
	private AdminSiteLineTable slt = new AdminSiteLineTable(con);	// վ����·�����
	
	/**
	 * ����Excel�ļ�����API
	 * @param filename
	 */
	public void importExcelData(String filename) {
		// �½�ӳ���ϵ
		Map<String,Integer> siteMap = new HashMap<String,Integer>();	// վ��ӳ��
		Map<String,Integer> lineMap = new HashMap<String,Integer>();	// ��·ӳ��
		Map<Integer,SiteLineClass> lidSeqMap = new HashMap<Integer,SiteLineClass>();	// վ����·��

		ReadSheetBase normalbus = new ReadNormalBus(con, filename);	// ��ȡ�ļ����湫������
		
		normalbus.processBusData(siteMap, lineMap, lidSeqMap);
		
		// ѭ��
		Iterator<Entry<Integer, SiteLineClass>> iter = lidSeqMap.entrySet().iterator();
		
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			
			int key = (int)entry.getKey();
			
			SiteLineClass val = (SiteLineClass)entry.getValue();
			
			// �������ݿ�վ����·����
			slt.addKeyToValue(key, val.getALlid(), val.getARlid());	
		}
		System.out.print("�������");
	}
}
