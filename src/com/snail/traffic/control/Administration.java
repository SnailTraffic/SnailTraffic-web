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
	private static OracleBase oracle = new OracleBase();		// ���ݿ����
	private static Connection con = oracle.getConnection();	// ��ȡ���ݿ�����
	private AdminSiteLineTable slt = new AdminSiteLineTable(con);	// վ����·�����
	/**
	 * ����Excel�ļ�����API
	 * @param filename
	 */
	public boolean importExcelData(String filename) {
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
		return true;
	}
	
	/*������·��Ϣ*/
	public static  boolean addLine(String linename,
						String linterval,
						String lfirstopen,
						String llastopen,
						String lfirstclose,
						String llastclose, 
						String lprice, 
						String lcardprice,
						String lcompany,
						String remark,
						String[] leftSite,  //����վ����ɵ�����
						String[] rightSite) {
		AddLine al = new AddLine(con);
		return al.confirmAddLineInfo(linename,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany,remark,leftSite,rightSite);
		
	}
	
	//ɾ����·��Ϣ
	public static boolean deleteLine(String linename){
		DeleteLine dl = new DeleteLine(con);
		return dl.confirmDeleteLineInfo(linename);
		
	}
	
	/*public void main(String[] args){
		String[] left = {"����·����","����·����","����·��Ҷ�"};
		String[] right = {"����·��Ҷ�","����·����","����·����"};
		addLine("1000·", "342-dd", "242", "l323", "35", "523", "532", "3sdh", "325d", "dgh",left,right );
		
	}*/
	public static void main(String[] args){
		deleteLine("602·");
	}
}
