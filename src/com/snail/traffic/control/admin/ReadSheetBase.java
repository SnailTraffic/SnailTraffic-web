package com.snail.traffic.control.admin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.snail.traffic.container.data.SiteToLineStruct;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * @name ReadSheetBase
 * @verion 1.0
 * @data 2015.5.09
 * @introduction �Ķ�Excel�ļ��б��Ļ���
 */
public class ReadSheetBase {
	private Workbook book_;		// �ļ����
	protected int curLineNumber	= 0;	// ��ǰ��·����
	protected int curSiteNumber	= 0;	// ��ǰվ����
	
	/**
	 * @name ���캯��
	 * @param filename
	 */
	public ReadSheetBase(String filename) {
		try {
			book_ = Workbook.getWorkbook(new File(filename));
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����������
	 * @param siteMap
	 * @param lineMap
	 * @param lidSeqMap
	 */
	public void processBusData(Map<String, Integer> siteMap
								, Map<String, Integer> lineMap
								, Map<Integer,SiteToLineStruct> lidSeqMap) {}
	
	/**
	 * ���� sheetN������еĵ� x�У��� y��
	 * @param sheetN
	 * 			�����
	 * @param x
	 * 			��
	 * @param y
	 * 			��
	 * @return 
	 */
	public String readCell(int sheetN, int x, int y) {
		Sheet sheet = this.book_.getSheet(sheetN);
		
		String cellstr = sheet.getCell(x, y).getContents();  //�ѵ�Ԫ���е�����ת��Ϊ�ַ���
		
		return cellstr;
	}
	
	/**
	 * @param str�� �Կո�Ϊ������ַ���
	 * @return
	 * @describe ���ַ������տո�ָ�Ϊ���ַ�������
	 */
	public static String[] readCellStr(String str) {	
		if (str == null || str.equals(""))
			return null;
		
		String[] s = str.split("-"); 
		
		return s;
	}
	
	/**
	 * ���վ��id��վ�㼯����
	 * @param sid
	 * @return
	 */
	public String addSidSet(String sidset, Integer sid) {
		if (sidset == null)
			sidset = "" + sid;
		else
			sidset += ("," + sid);
		
		return sidset;
	}
	
	/**
	 * ��ȡ��ǰ��·����
	 * @return
	 */
	public int getCurLineNumber() {
		return curLineNumber;
	}
	
	/**
	 * ��ȡ��ǰͳ��վ������
	 * @return
	 */
	public int getCurSiteNumber() {
		return curSiteNumber;
	}
}
