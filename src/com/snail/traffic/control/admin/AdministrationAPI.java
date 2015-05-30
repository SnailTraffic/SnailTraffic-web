package com.snail.traffic.control.admin;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.snail.traffic.container.data.SiteToLineStruct;
import com.snail.traffic.persistence.admin.AdminSiteToLineTable;
import com.snail.traffic.persistence.OracleBase;

public class AdministrationAPI {
	private static OracleBase oracle = new OracleBase();
	private static Connection con = oracle.getConnection();

	/**
	 * 导入Excel文件数据API
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
			slt.addKeyToValue(key, val.getALlid(), val.getARlid());	
		}
		System.out.print("导入完成");
		return true;
	}
	
	/*增加线路信息*/
	public static boolean addLine(String linename,
						String linterval,
						String lfirstopen,
						String llastopen,
						String lfirstclose,
						String llastclose, 
						String lprice, 
						String lcardprice,
						String lcompany,
						String remark,
						String[] leftSite,  //左行站点组成的数组
						String[] rightSite) {
		AddLine al = new AddLine(con);
		return al.confirmAddLineInfo(linename,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany,remark,leftSite,rightSite);
		
	}
	
	//删除线路信息
	public static boolean deleteLine(String linename){
		DeleteLine dl = new DeleteLine(con);
		return dl.confirmDeleteLineInfo(linename);
		
	}
	
	//更新线路信息
	public static void updateLine(String linename,
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
						String[] leftSite,  //左行站点组成的数组
						String[] rightSite){
		UpdateLine ul = new UpdateLine(con);
		
		ul.confirmUpdateLineInfo(linename,newname,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany,remark,leftSite,rightSite);
	}
	
	//增加站点
	public static void addSite(String sitename){
		AddSite as = new AddSite(con);
		as.confirmAddSite(sitename);
	}
	
	//删除站点
    public static void deleteSite(String sitename){
    	DeleteSite ds = new DeleteSite(con);
    	ds.confirmDeleteSite(sitename);
    }
    
    public static void updateSite(String sitename,String newname){
    	UpdateSite us = new UpdateSite(con);
    	us.confirmUpdateSite(sitename, newname);
    }
	
	public static void main(String[] args){
		/*String[] left = {"南李路李桥","南李路板桥","南李路徐家墩"};
		String[] right = {"南李路徐家墩","南李路板桥","南李路李桥"};
		addLine("1000路", "342-dd", "242", "l323", "35", "523", "532", "3sdh", "325d", "dgh",left,right );*/
		//deleteLine("602路");
		String[] a = {"建设大道双墩","江汉二路南京路","解放大道宗关"};
		String[] b = {"解放大道宗关","江汉二路南京路","建设大道双墩"};
		updateLine("1路","14512路","fgfgds","dgs","dfd","dfgsg","dg","gf","df","dg","df",a,b);
		/*deleteSite("民意一路");*/
		//addSite("蜗牛公交");
		//updateSite("蜗牛公交","哈哈公交");
	}
}
