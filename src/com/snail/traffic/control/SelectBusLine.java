package com.snail.traffic.control;

import java.sql.Connection;

import com.snail.traffic.persistence.AdminLineTable;
import com.snail.traffic.persistence.AdminSiteTable;
import com.snail.traffic.persistence.InfoStruct;
import com.snail.traffic.persistence.LineAllInfoStruct;
import com.snail.traffic.persistence.SelectOperated;
import com.snail.traffic.persistence.TwoLongStruct;

/*
 * 线路查询
 * 1.从前端获取线路名称
 * 2.根据线路名,从数据库中获取线路上的站点(注意去行和回行)组成的数组
 * 3.返回步骤2的数组
 */
public class SelectBusLine extends SelectBase {
	private SelectOperated seloper = null;	// 数据库查询对象
	private AdminLineTable adminLine = null;// 管理线路信息对象
	private AdminSiteTable adminSite = null;// 管理站点信息对象
	/**
	 * 构造函数
	 * @param con
	 * 			数据库连接
	 */
	public SelectBusLine(Connection con) {
		this.seloper = new SelectOperated(con);
		this.adminLine = new AdminLineTable(con);
		this.adminSite = new AdminSiteTable(con);
	}
	
	/**
	 * 查询线路信息函数
	 * @param input
	 * 			查询关键字
	 * @return lineinfo
	 * 				站点信息
	 */
	public InfoStruct query(String input) {
		InfoStruct lineinfo = adminLine.getInfo(input);		// 获取线路基本信息
		TwoLongStruct sidSeq = seloper.getLineSiteSeq(input);	// 获取两条长串字符
		
		lineinfo.put(true, getSites(sidSeq.get(true)));	// 把左边的线路名数组放到左边
		lineinfo.put(false, getSites(sidSeq.get(false)));	
		
		return lineinfo;
	}
	
	private String[] getSites(String sidseq) {
		if(sidseq == null)	// 站点序列为空时，返回
			return null;
		
		String[] sites = sidseq.split(",");	// 经过站点的左线路
		
		for(int i = 0; i < sites.length; i++)
			sites[i] = adminSite.getName(Integer.parseInt(sites[i])); // 把线路名替换线路代码字符串
		
		return sites;
	}
}
