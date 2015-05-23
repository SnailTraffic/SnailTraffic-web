package com.snail.traffic.control;

import java.sql.Connection;

import com.snail.traffic.persistence.AdminLineSiteTable;
import com.snail.traffic.persistence.AdminLineTable;
import com.snail.traffic.persistence.AdminSiteLineTable;
import com.snail.traffic.persistence.AdminSiteTable;


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
	Connection con = null;
	
	AddLine(Connection con) {
		this.con = con;
	}
	
	AdminLineTable alt = new AdminLineTable(con);            //线路表对象
	AdminSiteLineTable aslt = new AdminSiteLineTable(con);   //站点线路表对象
	AdminSiteTable ast = new AdminSiteTable(con);            //站点表对象
	AdminLineSiteTable alst = new AdminLineSiteTable(con);   //线路站点表对象
	
	//判断输入的线路是否已存在
	public void isExist(String linename){
		
		//int[] line = addLine.isExistLine(linename);
		int line = alt.getlid(linename);
		
		//不存在该线路信息时添加线路表
		if(line == 0){
			/*前端显示可用*/
		}
		else{
			/*前端显示已存在该线路名*/
		}
	}
	
	public void submitLineInfo(String linename,
								String linterval,
								String lfirstopen,
								String llastopen,
								String lfirstclose,
								String llastclose, 
								String lprice, 
								String lcardprice,
								String lcompany,
								String[] leftSite,  //左行站点组成的数组
								String[] rightSite){
									
		//获取lid
		int lid = alt.getNewID();
		
		//修改线路表信息
		alt.editLineInfo(lid,linename,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany);
		
		//修改线路站点表信息
		//修改站点线路表信息
		String lsidseq = null;
		String rsidseq = null;
		String llidseq = null;
		String rlidseq = null;
		int sid = 0;
		
		for(int i = 0; i < leftSite.length; i++){
			lsidseq += leftSite[i];                   //获取站点组成的字符串
			sid = ast.getSid(leftSite[i]);            //获取sid
			String[] lidseq = aslt.getLidSeq(sid);    //根据sid获取左行线路id字符串
			llidseq = lidseq[0] + "," + lid;
			aslt.updateLSiteLine(sid,llidseq);       //根据sid来更新左行信息
		}
		for(int i = 0; i< rightSite.length; i++){
			rsidseq += rightSite[i];
			sid = ast.getSid(rightSite[i]);        //获取sid
			String[] lidseq = aslt.getLidSeq(sid); //根据sid获取右行线路id字符串
			rlidseq = lidseq[1] + "," + lid;
			aslt.updateRSiteLine(sid,rlidseq);     //根据sid来凤县右行信息
		}
		alst.addLineToSite(lid,lsidseq,rsidseq);
		
	}
		
	
	
	
	
	
	
	
	
}
















