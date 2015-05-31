package com.snail.traffic.control.admin;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.snail.traffic.container.data.SiteToLineStruct;
import com.snail.traffic.persistence.OracleBase;
import com.snail.traffic.persistence.admin.AdminLineTable;
import com.snail.traffic.persistence.admin.AdminSiteTable;
import com.snail.traffic.persistence.admin.AdminSiteToLineTable;

public class AdministrationAPI {
	private static OracleBase oracle = new OracleBase();
	private static Connection con = oracle.getConnection();

	/**
	 * ����Excel�ļ�����API
	 * @param filename
	 */
	public boolean importExcelData(String filename) {
		Map<String,Integer> siteMap = new HashMap<String,Integer>();
		Map<String,Integer> lineMap = new HashMap<String,Integer>();
		Map<Integer,SiteToLineStruct> lidSeqMap = new HashMap<Integer,SiteToLineStruct>();

		ReadSheetBase normalBus = new ReadNormalBus(con, filename);
		normalBus.processBusData(siteMap, lineMap, lidSeqMap);

		AdminSiteToLineTable slt = new AdminSiteToLineTable(con);
		Iterator<Entry<Integer, SiteToLineStruct>> iter = lidSeqMap.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			int key = (int)entry.getKey();

			SiteToLineStruct val = (SiteToLineStruct)entry.getValue();
			slt.addKeyToValue(key, val.getLeftLid(), val.getRightLid());	
		}
		System.out.print("�������");
		return true;
	}
	
	//�ж���·�Ƿ����
	public boolean isLineExist(String lineName) {
		AdminLineTable alt = new AdminLineTable(con);
		
		int lid = alt.getId(lineName);
		//ǰ�˸��ݾ����������ж��Ƿ����
		if(lid == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	//�ж�վ���Ƿ����
	public boolean isSiteExist(String siteName){
		AdminSiteTable ast = new AdminSiteTable(con);
		
		int sid = ast.getId(siteName);
		//ǰ�˸��ݾ����������ж�
		if( sid == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	/*������·��Ϣ*/
	//����ֵΪtrueʱ��ʾ���ӳɹ�,������ʾʧ��
	public static boolean addLine(String lineName,
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
		return al.confirmAddLineInfo(lineName,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany,remark,leftSite,rightSite);
		
	}
	
	//ɾ����·��Ϣ
	public static boolean deleteLine(String lineName){
		DeleteLine dl = new DeleteLine(con);
		return dl.confirmDeleteLineInfo(lineName);
		
	}
	
	//������·��Ϣ
	public static boolean updateLine(String lineName,
			            String newname,
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
						String[] rightSite){
		UpdateLine ul = new UpdateLine(con);
		
		return ul.confirmUpdateLineInfo(lineName,newname,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany,remark,leftSite,rightSite);
	}
	
	//����վ��
	public static void addSite(String siteName){
		AddSite as = new AddSite(con);
		as.confirmAddSite(siteName);
	}
	
	//ɾ��վ��
    public static void deleteSite(String siteName){
    	DeleteSite ds = new DeleteSite(con);
    	ds.confirmDeleteSite(siteName);
    }
    
	//����վ��
    public static void updateSite(String siteName,String newname){
    	UpdateSite us = new UpdateSite(con);
    	us.confirmUpdateSite(siteName, newname);
    }
	
	public static void main(String[] args){
		//String[] left = {"����·����","����·����","����·��Ҷ�"};
		//String[] right = {"����·��Ҷ�","����·����","����·����"};
		//addLine("1000·", "342-dd", "242", "l323", "35", "523", "532", "3sdh", "325d", "dgh",left,right );
		//deleteLine("602·");
		String[] a = {"������˫��","������·�Ͼ�·","��Ŵ���ڹ�"};
		String[] b = {"��Ŵ���ڹ�","������·�Ͼ�·","������˫��"};
		updateLine("1·","14513·","fgfgds","dgs","dfd","dfgsg","dg","gf","df","dg","df",a,b);
		/*deleteSite("����һ·");*/
		//addSite("��ţ����");
		//updateSite("��ţ����","��������");
	}
}
