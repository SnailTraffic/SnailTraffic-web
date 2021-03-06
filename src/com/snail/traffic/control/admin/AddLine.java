﻿package com.snail.traffic.control.admin;

import java.sql.Connection;

import com.snail.traffic.container.data.TwoStringStruct;
import com.snail.traffic.persistence.admin.AdminLineTable;
import com.snail.traffic.persistence.admin.AdminLineToSiteTable;
import com.snail.traffic.persistence.admin.AdminNextSiteTable;
import com.snail.traffic.persistence.admin.AdminSiteTable;
import com.snail.traffic.persistence.admin.AdminSiteToLineTable;
import com.snail.traffic.persistence.select.SelectSiteToLineView;

	/*
	 * 1.从前端获取线路名称lname
	 * 2.数据库检索lname是否已存在
	 * 3.若存在则前端显示已经存在该线路名,否则在线路表中增加line线路
	 * 4.前端设置线路站点信息时,每次选择一个站点,数据库都返回一个sid,组成一个sid序列
	 * 5.根据4获取sidseq1,sidseq2
	 * 6.根据5在线路站点表中增加一行信息
	 * 7.在步骤4中,每次选择一个站点,都需要在站点线路表中更新该站点信息
	 */
public class AddLine {

	private AdminLineTable adline;		// 线路表管理对象
	private AdminSiteToLineTable adsiteline;// 站点线路表管理对象
	
	private AdminSiteTable adsite;		// 站点表管理对象
	private AdminLineToSiteTable alinesite;// 线路站点表管理对象

	private SelectSiteToLineView sstlv;
	private AdminNextSiteTable adnextsite;// 下一站点表管理对象
	private TwoStringStruct tls;		//
	
	public AddLine(Connection con) {
		adline 		= new AdminLineTable(con);
		adsiteline 	=  new AdminSiteToLineTable(con);
		adsite 		= new AdminSiteTable(con);            //站点表对象
		alinesite 	=  new AdminLineToSiteTable(con);
		
		sstlv = new SelectSiteToLineView(con);
		adnextsite 	= new AdminNextSiteTable(con);
	}
	
	
	
	/*前端显示已经添加完成*/
	public boolean confirmAddLineInfo(String linename
									, String linterval
									, String lfirstopen
									, String llastopen
									, String lfirstclose
									, String llastclose
									, String lprice 
									, String lcardprice
									, String lcompany
									, String remark
									, String[] leftSite  //左行站点组成的数组
									, String[] rightSite) {					
		//获取lid
		int lid = adline.getNewId();
		
		//增加线路表信息
		adline.addLineInfo(lid,linename,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany,remark);
		
	
		String leftSidSeq = addTable(leftSite, lid, true);
		String rightSidSeq = addTable(rightSite, lid, false);
		alinesite.addKeyToValue(lid,leftSidSeq,rightSidSeq);	
		return true;
	}
	
	/**
	 * 循环新加的线路中的站点序列,并加入到数据库中
	 * @param array
	 * 			输入的站点序列数组
	 * @param str
	 * 			需要修改的站点线路表原序列
	 * @param lid
	 * 			线路id
	 * @param isleft
	 * 			是左边线路
	 * @return sidseq
	 * 				线路站点表中的站点序列
	 */
	private String addTable(String[] array, int lid, Boolean isleft) {
		//修改站点线路表信息
		String sidseq = "";	// 站点序列
		String lineseq = null;	// 经过一个站点的线路序列
		String left = null;
		String right = null;
		int sid = 0;	// 站点id
		int temp = 0;	// 上一个站点id
		int runleft;
		
		for (int i = 0; i < array.length; i++) {						
			sid = adsite.getId(array[i]);	//获取sid
			if (temp != 0) {
				/**
				 * 时间和距离暂时设为0，后期需要前端提供
				 */
				if (isleft)
					runleft = 1;
				else
					runleft = 0;
				adnextsite.addKeyToValue(temp,lid,runleft,sid,0,0);
			}
			if (sidseq.equals(""))
				sidseq = sid + "";
			else
				sidseq += ("," + sid);
			
			//tls = seloper.getSiteLineSeq(array[i]);    //根据站点名获取左行和右行线路id字符串
			tls = sstlv.getSeq(array[i]);
			lineseq = tls.get(isleft);	
			lineseq += ("," + lid);   //更新站点线路表
			
			if (isleft) {
				left = lineseq;
				right = tls.get(!isleft);
			}
			else {
				left = tls.get(!isleft);
				right = lineseq;
			}
			adsiteline.updateKeyToValue(sid,left,right);
			temp = sid; //上一个站点id
		}
		return sidseq;
	}
}