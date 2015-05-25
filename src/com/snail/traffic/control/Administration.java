package com.snail.traffic.control;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.snail.traffic.persistence.AdminSiteLineTable;
import com.snail.traffic.persistence.OracleBase;

/**
 * 为前端提供一个简洁的后台管理业务API，
 * 
 * 主要功能是导入Excel文件，
 * 
 * 需要建立3个map，
 * 
 * 其中站点线路map需要在读完所有线路后才能得到完整的数据
 * 
 * 最后，通过循环map存好数据库
 */
public class Administration {
	private static OracleBase oracle = new OracleBase();		// 数据库对象
	private static Connection con = oracle.getConnection();	// 获取数据库连接
	private AdminSiteLineTable slt = new AdminSiteLineTable(con);	// 站点线路表对象
	/**
	 * 导入Excel文件数据API
	 * @param filename
	 */
	public boolean importExcelData(String filename) {
		// 新建映射关系
		Map<String,Integer> siteMap = new HashMap<String,Integer>();	// 站点映射
		Map<String,Integer> lineMap = new HashMap<String,Integer>();	// 线路映射
		Map<Integer,SiteLineClass> lidSeqMap = new HashMap<Integer,SiteLineClass>();	// 站点线路表

		ReadSheetBase normalbus = new ReadNormalBus(con, filename);	// 读取文件常规公交数据
		
		normalbus.processBusData(siteMap, lineMap, lidSeqMap);
		
		// 循环
		Iterator<Entry<Integer, SiteLineClass>> iter = lidSeqMap.entrySet().iterator();
		
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			
			int key = (int)entry.getKey();
			
			SiteLineClass val = (SiteLineClass)entry.getValue();
			
			// 加入数据库站点线路表中
			slt.addKeyToValue(key, val.getALlid(), val.getARlid());	
		}
		System.out.print("导入完成");
		return true;
	}
	
	/*增加线路信息*/
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
	
	/*public void main(String[] args){
		String[] left = {"南李路李桥","南李路板桥","南李路徐家墩"};
		String[] right = {"南李路徐家墩","南李路板桥","南李路李桥"};
		addLine("1000路", "342-dd", "242", "l323", "35", "523", "532", "3sdh", "325d", "dgh",left,right );
		
	}*/
	public static void main(String[] args){
		deleteLine("602路");
	}
}
