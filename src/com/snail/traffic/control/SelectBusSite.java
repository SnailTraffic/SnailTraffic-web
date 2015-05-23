package com.snail.traffic.control;

import java.sql.Connection;
import java.util.Arrays;

import com.snail.traffic.persistence.AdminInfoTableBase;
import com.snail.traffic.persistence.AdminLineTable;
import com.snail.traffic.persistence.InfoStruct;
import com.snail.traffic.persistence.SelectOperated;
import com.snail.traffic.persistence.TwoLongStruct;
/**
 * 站点查询功能
 * 2.根据站点名,从数据库中获取该站点所经过的所有线路组成的线路数组(注意去行和回行)
 * 3.返回一个按照站点序号从小到大排列的线路数组数组
 */
class SelectBusSite extends SelectBase {
	SelectOperated seloper = null;	// 数据库查询对象
	AdminInfoTableBase adminLine = null; 
	/**
	 * 构造函数
	 * @param con
	 * 			数据库连接
	 */
	public SelectBusSite(Connection con) {
		seloper = new SelectOperated(con);
		adminLine = new AdminLineTable(con);
	}

	/**
	 * 查询站点信息函数
	 * @param input
	 * 			查询关键字
	 * @return siteinfo
	 * 				站点信息
	 */
	public InfoStruct query(String input) {
		
		TwoLongStruct lineSeq = seloper.getSiteLineSeq(input);	// 获取两条长串字符
		
		InfoStruct siteinfo = new InfoStruct();	// 声明定义站点信息数组
		
		siteinfo.put(true, getLines(lineSeq.get(true)));	// 把左边的线路名数组放到左边
		siteinfo.put(false, getLines(lineSeq.get(false)));	
		
		return siteinfo;
	}
	
	
	/**
	 * 返回一个按照站点序号从小到大排列的线路数组数组
	 * @param sitename
	 * 				站点名
	 * @return lineSort(lines)
	 * 				排序好的字符串数组
	 */
	private String[] getLines(String lidseq) {
		
		if(lidseq == null)	// 线路序列为空时，返回
			return null;
		
		String[] lines = lidseq.split(",");	// 经过站点的左线路
		
		for(int i = 0; i < lines.length; i++)
			lines[i] = adminLine.getName(Integer.parseInt(lines[i])); // 把线路名替换线路代码字符串
		
		Arrays.sort(lines);	// 排序线路
		return lines;
	}
	
	/**
	 * 模糊查询中根据部分信息,匹配符合的全称(无需判断是否有匹配项)
	 * @param partname
	 * 				站点名
	 * @return fullname
	 * 				站点全称
	 */
	public String[] getFullName(String partname) {
		String[] fullname = null;
		/*数据库操作:根据partName返回匹配的fullName数组*/
		
		return fullname;	
	}
	

}
