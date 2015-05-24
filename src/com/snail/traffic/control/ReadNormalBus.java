package com.snail.traffic.control;

import java.sql.Connection;
import java.util.Map;

import com.snail.traffic.persistence.AdminLineSiteTable;
import com.snail.traffic.persistence.AdminLineTable;
import com.snail.traffic.persistence.AdminRelationTableBase;
import com.snail.traffic.persistence.AdminSiteTable;

/**
 * @author weiliu
 * @describe 读取常规公交线路表格类，从excel文件到数据库
 */
public class ReadNormalBus extends ReadSheetBase {
	private static final int SHEETN_NUMBER 	= 0;	// 常规公交表格是表格0
	private final static int ROW_NUMBER		= 292;  // 表行数是311
	private final static int COLUMN_NUMBER	= 38; 	// 表列数是38
	private AdminSiteTable st;
	private AdminLineTable lt;
	private AdminRelationTableBase lst;
	
	/**
	 * 构造函数
	 * @param filename
	 * 				excel文件名
	 */
	public ReadNormalBus(Connection con, String filename) {
		super(filename);
		st 	= new AdminSiteTable(con);
		lt 	= new AdminLineTable(con);
		lst = new AdminLineSiteTable(con);	
	}
	
	/**
	 * 处理常规公交数据
	 * @param siteMap
	 * @param lineMap
	 * @param lidSeqLMap
	 * @param lidSeqRMap
	 */
	public void processBusData (Map<String,Integer> siteMap
								, Map<String,Integer> lineMap
								, Map<Integer,SiteLineClass> lidSeqMap) {
		for (int i = 1; i < ROW_NUMBER; i++)
			processOneRow(i, siteMap, lineMap, lidSeqMap);	// 处理单条数据
	}
	
	/**
	 * 处理常规公交表格中的一行数据
	 * @param i 
	 * 			第i行
	 * @param siteMap 
	 * 			站点表
	 * @param lineMap 
	 * 			线路表
	 * @param lidSeqLMap 
	 * 			左行站点线路表
	 * @param lidSeqRMap 
	 * 			右行站点线路表
	 */
	private void processOneRow (int i 
								, Map<String,Integer> siteMap
								, Map<String,Integer> lineMap
								, Map<Integer,SiteLineClass> lidSeqMap) {
		NormalBusClass normalbus = getOneRow(i);
		if (normalbus == null)
			return;
		
		Integer lidvalue = null;	// 线路id值
		
		// 处理线路信息
		lidvalue = processLineInfo(lineMap, normalbus);
		
		String leftSidSet = null;	// 左行站点集合
		String rightSidSet = null;	// 右行站点集合
		
		// 循环左行站点序列
		leftSidSet = processOneWay(normalbus.goStops, lidvalue, siteMap, lidSeqMap, true);
		
		// 循环右行站点序列
		rightSidSet = processOneWay(normalbus.comeStops, lidvalue, siteMap, lidSeqMap, false);
		
		// 线路站点表存数据库
		lst.addKeyToValue(lidvalue, leftSidSet, rightSidSet);
	}
	
	/**
	 * 处理单行站点数组
	 * @param siteArr
	 * 				站点数组
	 * @param lidvalue
	 * 				线路id
	 * @param siteMap
	 * 				站点表map
	 * @param lidSeqMap
	 * 				线路表map
	 * @param sidSet
	 * 				站点序列
	 * @param isleft
	 * 				是否左行
	 * @return sidSet
	 * 				站点集合字符串
	 */
	private String processOneWay(String[] siteArr
								, Integer lidvalue
								, Map<String,Integer> siteMap
								, Map<Integer,SiteLineClass> lidSeqMap
								, Boolean isleft) {
		Integer sidvalue = null;	// 站点id值
		String sidSet = null;	// 站点集合
		
		if (siteArr == null)	// 站点数组为null	
			return null;	
		int sitelen = siteArr.length;	// 站点名数组长度		
		String sitestr = null;	// 站点名
		
		for (int k = 0; k < sitelen; k++) {
			sitestr = siteArr[k].trim();	// 去除站点名的前后空格
			
			if (sitestr.equals(""))
				continue;
			
			sidvalue = siteMap.get(sitestr);		
			if (sidvalue == null) {
				curSiteNumber++;	
				siteMap.put(sitestr, curSiteNumber);	
				sidvalue = curSiteNumber;
						
				// 站点信息存数据库
				st.addSiteInfo(curSiteNumber, sitestr);
			}

			// 建线路单行站点表
			sidSet = addSidSet(sidSet, sidvalue);
			
			// 建站点单行线路map
			setSiteLineMap(lidvalue, sidvalue, lidSeqMap, isleft);	
		}
		return sidSet;
	}
	
	/**
	 * 向线路集合中增加一条线路
	 * @param lidvalue
	 * @param sidvalue
	 * @param lidSeqMap
	 * @param isleft
	 */
	private void setSiteLineMap(Integer lidvalue
								, Integer sidvalue
								, Map<Integer,SiteLineClass> lidSeqMap
								, Boolean isleft) {
		SiteLineClass siteline = null;		// 站点线路map的获取key对应的value临时值
		
		siteline = lidSeqMap.get(sidvalue);		// 根据sid获取当前经过该站点的左右行线路
			
		if (siteline == null) {
			siteline = new SiteLineClass();
			
			if (isleft)
				siteline.setALlid(lidvalue);	// 左行线路集合增加一条新线路
			else
				siteline.setARlid(lidvalue);	// 右行线路集合增加一条新线路
				
			lidSeqMap.put(sidvalue, siteline);	// 重新放入map中
		}
		else {
			if (isleft)	
				siteline.setALlid(lidvalue);	// 左行线路集合增加一条新线路
			else
				siteline.setARlid(lidvalue);	// 右行线路集合增加一条新线路
			
			lidSeqMap.put(sidvalue, siteline);	// 重新放入map中
		}
	}
	
	/**
	 * 获取常规公交表格中的一行数据
	 * @param i
	 * 			表格中行数
	 * @return normalbus
	 * 			常规公交一条线路
	 */
	private NormalBusClass getOneRow(int i) {
		NormalBusClass normalbus = new NormalBusClass();	// 常规公交临时变量
		
		String firstcell = readCell(SHEETN_NUMBER, 0, i);  // firstcell是首个单元格内的字符串
		
		if (firstcell == null || firstcell.equals(""))	//若每行的首位为空,则直接跳过
    		return null;
    	
		String columnStr = null;
		
		// 获取一行信息
		for (int j = 0; j < COLUMN_NUMBER; j++ ) {
	    	columnStr = readCell(SHEETN_NUMBER, j, i).trim();	
	    	
	    	// 列字符为null时，跳过
	    	if (columnStr == null)
	    		continue;
	    	
	    	// 判断数据属性
	    	if (j == 0)
	    		normalbus.lineName = columnStr;
	    	else if (j == 1)
	    		normalbus.lineRange = columnStr;
	    	else if (j == 4)
	    		normalbus.goStops = readCellStr(columnStr);
	    	else if (j == 16)
	    		normalbus.comeStops = readCellStr(columnStr);	    	
	    	else if (j == 28)
	    		normalbus.firstOpen = columnStr;
	    	else if (j == 29)
	    		normalbus.lastOpen = columnStr;
	    	else if (j == 30)
	    		normalbus.firstClose = columnStr;
	    	else if (j == 31)
	    		normalbus.lastClose = columnStr;
	    	else if (j == 32)
	    		normalbus.price = columnStr;
	    	else if (j == 33)
	    		normalbus.cardPrice = columnStr;
	    	else if (j == 34)
	    		normalbus.company = columnStr;
	    	else if (j == 36)
	    		normalbus.remark = columnStr;
	    }
	    curLineNumber++;	// 线路数增加
	    return normalbus;
	}
	
	/**
	 * 处理一条线路信息函数
	 * @param lineMap
	 * @param normalbus
	 * @return Integer
	 */
	private Integer processLineInfo(Map<String,Integer> lineMap, NormalBusClass normalbus) {
		Integer lidvalue = null;	// 线路id 
		lidvalue = lineMap.get(normalbus.lineName);
		
		if (lidvalue == null) {
			lineMap.put(normalbus.lineName, curLineNumber);
			
			lidvalue = curLineNumber;
			
			// 线路表存数据库
			lt.addLineInfo(lidvalue, normalbus.lineName
							, normalbus.lineRange, normalbus.firstOpen
							, normalbus.lastOpen, normalbus.firstClose
							, normalbus.lastClose, normalbus.price
							, normalbus.cardPrice, normalbus.company
							, normalbus.remark);
		}
		return lidvalue;
	}
}
