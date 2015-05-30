package com.snail.traffic.control.admin;

import java.sql.Connection;
import java.util.Map;

import com.snail.traffic.container.data.NormalBusStruct;
import com.snail.traffic.container.data.SiteToLineStruct;
import com.snail.traffic.persistence.admin.AdminLineToSiteTable;
import com.snail.traffic.persistence.admin.AdminLineTable;
import com.snail.traffic.persistence.admin.AdminNextSiteTable;
import com.snail.traffic.persistence.admin.BaseAdminRelationTable;
import com.snail.traffic.persistence.admin.AdminSiteTable;

/**
 * @author weiliu
 * @describe ��ȡ���湫����·����࣬��excel�ļ������ݿ�
 */
public class ReadNormalBus extends ReadSheetBase {
	private static final int SHEETN_NUMBER 	= 0;	// ���湫������Ǳ��0
	private final static int ROW_NUMBER		= 292;  // ��������311
	private final static int COLUMN_NUMBER	= 38; 	// ��������38
	private AdminSiteTable st;
	private AdminLineTable lt;
	private BaseAdminRelationTable lst;
	private AdminNextSiteTable adnextsite;
	
	/**
	 * ���캯��
	 * @param filename
	 * 				excel�ļ���
	 */
	public ReadNormalBus(Connection con, String filename) {
		super(filename);
		st 	= new AdminSiteTable(con);
		lt 	= new AdminLineTable(con);
		lst = new AdminLineToSiteTable(con);
		adnextsite = new AdminNextSiteTable(con);
	}
	
	/**
	 * �����湫������
	 * @param siteMap
	 * @param lineMap
	 * @param lidSeqLMap
	 * @param lidSeqRMap
	 */
	public void processBusData (Map<String,Integer> siteMap
								, Map<String,Integer> lineMap
								, Map<Integer,SiteToLineStruct> lidSeqMap) {
		for (int i = 1; i < ROW_NUMBER; i++)
			processOneRow(i, siteMap, lineMap, lidSeqMap);	// ����������
	}
	
	/**
	 * �����湫������е�һ������
	 * @param i 
	 * 			��i��
	 * @param siteMap 
	 * 			վ���
	 * @param lineMap 
	 * 			��·��
	 * @param lidSeqLMap 
	 * 			����վ����·��
	 * @param lidSeqRMap 
	 * 			����վ����·��
	 */
	private void processOneRow (int i 
								, Map<String,Integer> siteMap
								, Map<String,Integer> lineMap
								, Map<Integer,SiteToLineStruct> lidSeqMap) {
		NormalBusStruct normalbus = getOneRow(i);
		if (normalbus == null)
			return;
		
		Integer lidvalue = null;	// ��·idֵ
		
		// ������·��Ϣ
		lidvalue = processLineInfo(lineMap, normalbus);
		
		String leftSidSet = null;	// ����վ�㼯��
		String rightSidSet = null;	// ����վ�㼯��
		
		// ѭ������վ������
		leftSidSet = processOneWay(normalbus.goStops, lidvalue, siteMap, lidSeqMap, true);
		
		// ѭ������վ������
		rightSidSet = processOneWay(normalbus.comeStops, lidvalue, siteMap, lidSeqMap, false);
		
		// ��·վ�������ݿ�
		lst.addKeyToValue(lidvalue, leftSidSet, rightSidSet);
	}
	
	/**
	 * ������վ������
	 * @param siteArr
	 * 				վ������
	 * @param lidvalue
	 * 				��·id
	 * @param siteMap
	 * 				վ���map
	 * @param lidSeqMap
	 * 				��·��map
	 * @param sidSet
	 * 				վ������
	 * @param isleft
	 * 				�Ƿ�����
	 * @return sidSet
	 * 				վ�㼯���ַ���
	 */
	private String processOneWay(String[] siteArr
								, Integer lidvalue
								, Map<String,Integer> siteMap
								, Map<Integer,SiteToLineStruct> lidSeqMap
								, Boolean isleft) {
		Integer sidvalue = null;	// վ��idֵ
		String sidSet = null;	// վ�㼯��
		
		if (siteArr == null)	// վ������Ϊnull	
			return null;	
		int sitelen = siteArr.length;	// վ�������鳤��		
		String sitestr = null;	// վ����
		int runleft = 0;	// �Ƿ�����
		int lastSid = 0;	// ��һ��վ��id
		
		for (int k = 0; k < sitelen; k++) {
			sitestr = siteArr[k].trim();	// ȥ��վ������ǰ��ո�
			
			if (sitestr.equals(""))
				continue;
			
			sidvalue = siteMap.get(sitestr);		
			if (sidvalue == null) {
				curSiteNumber++;	
				siteMap.put(sitestr, curSiteNumber);	
				sidvalue = curSiteNumber;
						
				// վ����Ϣ�����ݿ�
				st.addSiteInfo(curSiteNumber, sitestr);
			}

			// ����·����վ���
			sidSet = addSidSet(sidSet, sidvalue);
			
			// ��վ�㵥����·map
			setSiteLineMap(lidvalue, sidvalue, lidSeqMap, isleft);	
			
			if (lastSid != 0) {
				if (isleft)
					runleft = 1;
				else 
					runleft = 0;
				adnextsite.addKeyToValue(lastSid, lidvalue, runleft, sidvalue, 0, 0);
			}
			lastSid = sidvalue;
		}
		return sidSet;
	}
	
	/**
	 * ����·����������һ����·
	 * @param lidvalue
	 * @param sidvalue
	 * @param lidSeqMap
	 * @param isleft
	 */
	private void setSiteLineMap(Integer lidvalue
								, Integer sidvalue
								, Map<Integer,SiteToLineStruct> lidSeqMap
								, Boolean isleft) {
		SiteToLineStruct siteline = null;		// վ����·map�Ļ�ȡkey��Ӧ��value��ʱֵ
		
		siteline = lidSeqMap.get(sidvalue);		// ����sid��ȡ��ǰ������վ�����������·
			
		if (siteline == null) {
			siteline = new SiteToLineStruct();
			
			if (isleft)
				siteline.setALlid(lidvalue);	// ������·��������һ������·
			else
				siteline.setARlid(lidvalue);	// ������·��������һ������·
				
			lidSeqMap.put(sidvalue, siteline);	// ���·���map��
		}
		else {
			if (isleft)	
				siteline.setALlid(lidvalue);	// ������·��������һ������·
			else
				siteline.setARlid(lidvalue);	// ������·��������һ������·
			
			lidSeqMap.put(sidvalue, siteline);	// ���·���map��
		}
	}
	
	/**
	 * ��ȡ���湫������е�һ������
	 * @param i
	 * 			���������
	 * @return normalbus
	 * 			���湫��һ����·
	 */
	private NormalBusStruct getOneRow(int i) {
		NormalBusStruct normalbus = new NormalBusStruct();	// ���湫����ʱ����
		
		String firstcell = readCell(SHEETN_NUMBER, 0, i);  // firstcell���׸���Ԫ���ڵ��ַ���
		
		if (firstcell == null || firstcell.equals(""))	//��ÿ�е���λΪ��,��ֱ������
    		return null;
    	
		String columnStr = null;
		
		// ��ȡһ����Ϣ
		for (int j = 0; j < COLUMN_NUMBER; j++ ) {
	    	columnStr = readCell(SHEETN_NUMBER, j, i).trim();	
	    	
	    	// ���ַ�Ϊnullʱ������
	    	if (columnStr == null)
	    		continue;
	    	
	    	// �ж���������
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
	    curLineNumber++;	// ��·������
	    return normalbus;
	}
	
	/**
	 * ����һ����·��Ϣ����
	 * @param lineMap
	 * @param normalbus
	 * @return Integer
	 */
	private Integer processLineInfo(Map<String,Integer> lineMap, NormalBusStruct normalbus) {
		Integer lidvalue = null;	// ��·id 
		lidvalue = lineMap.get(normalbus.lineName);
		
		if (lidvalue == null) {
			lineMap.put(normalbus.lineName, curLineNumber);
			
			lidvalue = curLineNumber;
			
			// ��·������ݿ�
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
