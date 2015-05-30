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
 * @introduction 阅读Excel文件中表格的基类
 */
public class ReadSheetBase {
	private Workbook book_;		// 文件句柄
	protected int curLineNumber	= 0;	// 当前线路条数
	protected int curSiteNumber	= 0;	// 当前站点数
	
	/**
	 * @name 构造函数
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
	 * 处理公交数据
	 * @param siteMap
	 * @param lineMap
	 * @param lidSeqMap
	 */
	public void processBusData(Map<String, Integer> siteMap
								, Map<String, Integer> lineMap
								, Map<Integer,SiteToLineStruct> lidSeqMap) {}
	
	/**
	 * 读第 sheetN个表格中的第 x列，第 y行
	 * @param sheetN
	 * 			表格编号
	 * @param x
	 * 			列
	 * @param y
	 * 			行
	 * @return 
	 */
	public String readCell(int sheetN, int x, int y) {
		Sheet sheet = this.book_.getSheet(sheetN);
		
		String cellstr = sheet.getCell(x, y).getContents();  //把单元格中的内容转换为字符串
		
		return cellstr;
	}
	
	/**
	 * @param str： 以空格为间隔的字符串
	 * @return
	 * @describe 把字符串按照空格分割为子字符串数组
	 */
	public static String[] readCellStr(String str) {	
		if (str == null || str.equals(""))
			return null;
		
		String[] s = str.split("-"); 
		
		return s;
	}
	
	/**
	 * 添加站点id到站点集合中
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
	 * 获取当前线路数量
	 * @return
	 */
	public int getCurLineNumber() {
		return curLineNumber;
	}
	
	/**
	 * 获取当前统计站点数量
	 * @return
	 */
	public int getCurSiteNumber() {
		return curSiteNumber;
	}
}
