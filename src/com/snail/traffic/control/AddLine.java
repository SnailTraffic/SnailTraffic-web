package com.snail.traffic.control;

import java.sql.Connection;

import com.snail.traffic.persistence.AdminLineSiteTable;
import com.snail.traffic.persistence.AdminLineTable;
import com.snail.traffic.persistence.AdminNextSiteTable;
import com.snail.traffic.persistence.AdminSiteLineTable;
import com.snail.traffic.persistence.AdminSiteTable;
import com.snail.traffic.persistence.SelectOperated;
import com.snail.traffic.persistence.TwoLongStruct;


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
	
	private Connection con = null;	// 声明数据库连接
	
	public AddLine(Connection con){
		this.con = con;
	}
	
	AdminLineTable alt = new AdminLineTable(con);            //线路表对象
	AdminSiteLineTable aslt = new AdminSiteLineTable(con);   //站点线路表对象
	AdminSiteTable ast = new AdminSiteTable(con);            //站点表对象
	AdminLineSiteTable alst = new AdminLineSiteTable(con);   //线路站点表对象
	SelectOperated so = new SelectOperated(con);
	TwoLongStruct tls = new TwoLongStruct();
	AdminNextSiteTable anst = new AdminNextSiteTable(con);
	
	
	//判断输入的线路是否已存在,若存在才进行下一步
	public boolean isExist(String linename){
		
		int line = alt.getId(linename);
		
		//不存在该线路信息时添加线路表
		if(line == 0){
			
			/*前端显示可用*/
			return true;
			
		}
		else{
			/*前端显示已存在该线路名*/
			return false;
		}
	}
	
	/*前端显示已经添加完成*/
	public boolean confirmAddLineInfo(String linename,
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
									
		//获取lid
		int lid = alt.getNewId();
		
		/*根据线路名删除一条后一个站点表的关系*/
		anst.deleteLine(linename);
		
		//增加线路表信息
		alt.addLineInfo(lid,linename,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany,remark);
		
		//修改线路站点表信息
		//修改站点线路表信息
		String lsidseq = null;
		String rsidseq = null;
		int sid = 0;
		
		for(int i = 0; i < leftSite.length; i++){
			
			lsidseq += leftSite[i];                  //获取左行站点组成的字符串
	
			sid = ast.getId(leftSite[i]);            //获取sid
			tls = so.getSiteLineSeq(leftSite[i]);    //根据站点名获取左行和右行线路id字符串
			String leftStr = tls.get(true);
			String rightStr = tls.get(false);
			leftStr = leftStr + "," + lid;           //更新站点线路表
			aslt.updateKeyToValue(sid,leftStr,rightStr);
			
		}
		for(int i = 0; i< rightSite.length; i++){
			
			rsidseq += rightSite[i];                  //获取右行站点组成的字符串
			
			sid = ast.getId(rightSite[i]);            //获取sid
			tls = so.getSiteLineSeq(rightSite[i]);    //根据站点名获取左行和右行线路id字符串
			String leftStr = tls.get(true);
			String rightStr = tls.get(false);
			rightStr = rightStr + "," + lid;           //更新站点线路表
			aslt.updateKeyToValue(sid,leftStr,rightStr);
			
		}
		alst.addKeyToValue(lid,lsidseq,rsidseq);
		
		return true;
	}
	
}
















