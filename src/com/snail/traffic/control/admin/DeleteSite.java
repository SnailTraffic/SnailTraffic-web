package com.snail.traffic.control.admin;

import java.sql.Connection;

import com.snail.traffic.persistence.admin.AdminLineToSiteTable;
import com.snail.traffic.persistence.admin.AdminLineTable;
import com.snail.traffic.persistence.admin.AdminNextSiteTable;
import com.snail.traffic.persistence.admin.AdminSiteToLineTable;
import com.snail.traffic.persistence.admin.AdminSiteTable;
import com.snail.traffic.persistence.select.SelectLineToSiteView;
import com.snail.traffic.persistence.select.SelectSiteToLineView;
import com.snail.traffic.container.data.TwoStringStruct;

/*
 *1.从前端获取要删除的站点名
 *2.判断站点是否存在
 *3.修改线路站点表信息
 *4.删除对应的站点表信息,由于级联关系可以删除对应的站点线路表
 */
public class DeleteSite{
	private Connection con = null;	// 声明数据库连接
	private AdminLineTable alt;	//站点线路表对象
	private AdminSiteToLineTable aslt;
	private AdminSiteTable ast;
	private AdminLineToSiteTable alst;//线路站点表对象
	//private SelectOperated so;
	private SelectLineToSiteView siteView;
	private SelectSiteToLineView lineView;
	private AdminNextSiteTable anst;
	private TwoStringStruct tlsLine,tlsSite;
	private DeleteSite ds;
	
	public DeleteSite(Connection con){
		this.con = con;
		alt = new AdminLineTable(con);            //线路表对象
	    aslt = new AdminSiteToLineTable(con);   //站点线路表对象
	    ast = new AdminSiteTable(con);            //站点表对象
	    alst = new AdminLineToSiteTable(con);   //线路站点表对象
	   // so = new SelectOperated(con);
	    siteView = new SelectLineToSiteView(con);
	    lineView = new SelectSiteToLineView(con);
	    tlsLine = new TwoStringStruct();
	    tlsSite = new TwoStringStruct();
	}
	
	/*若返回值为false则前端显示不存在该站点,否则显示删除完成*/
	public boolean confirmDeleteSite(String sitename) {
		if(ast.getId(sitename) == 0){
			return false;
		}
		//删除站点线路表时需要先从线路站点表
		else{
			//更新线路站点表
			editLineSite(sitename,true);
			editLineSite(sitename,false);
			
			//删除站点表和站点线路表
			ast.delete(sitename);
			
			return true;
		}
	}
	
	private void editLineSite(String sitename,boolean isLeft){
		//tlsLine = so.getSiteLineSeq(sitename);  //根据站点名获取两边线路序列
		tlsLine = lineView.getSeq(sitename);	
		String lineSeq = tlsLine.get(isLeft);   
		String[] lidseq = lineSeq.split(",");
		int sid = ast.getId(sitename);
		
		for(int i = 0 ; i < lidseq.length; i++){
			int lid = Integer.parseInt(lidseq[i]);
			String linename = alt.getName(lid);    //获取相应线路id对应的线路名
			//tlsSite = so.getLineSiteSeq(linename); //根据线路名获取两边站点序列
			tlsSite = siteView.getSeq(linename);
			String siteSeq = tlsSite.get(isLeft);  //siteSeq是站点组成的字符串
			String leftSiteSeq = tlsSite.get(true);
			String rightSiteSeq = tlsSite.get(false);
			
			String[] temp = siteSeq.split(",");
			int a = Integer.parseInt(temp[temp.length-1]);
			if(  a == sid){
				siteSeq = siteSeq.replace(","+sid,"");
			}
			else{
				siteSeq = siteSeq.replace(sid+",","");
			}
			if(isLeft){
				alst.updateKeyToValue(lid,siteSeq,rightSiteSeq);
			}
			else{
				alst.updateKeyToValue(lid,leftSiteSeq,siteSeq);
			}
		}
	}
	
}