package com.snail.traffic.control.admin;

import java.sql.Connection;

import com.snail.traffic.container.data.TwoStringStruct;
import com.snail.traffic.persistence.admin.AdminLineTable;
import com.snail.traffic.persistence.admin.AdminLineToSiteTable;
import com.snail.traffic.persistence.admin.AdminNextSiteTable;
import com.snail.traffic.persistence.admin.AdminSiteTable;
import com.snail.traffic.persistence.admin.AdminSiteToLineTable;
import com.snail.traffic.persistence.select.SelectLineToSiteView;
import com.snail.traffic.persistence.select.SelectSiteToLineView;

/*
 *1.从前端读入需要删除的线路名
 *2.判断是否存在该线路名
 *3.删除线路表中该条线路所在行的信息(线路表)
 *4.获取线路站点表中的站点信息
 *5.根据步骤5,修改获取的站点对应的站点线路表
 *6.删除对应的线路站点表信息
 */


public class DeleteLine{
	
	private Connection con = null;	// 声明数据库连接
	private AdminLineTable alt;	//站点线路表对象
	private AdminSiteToLineTable aslt;
	private AdminSiteTable ast;
	private AdminLineToSiteTable alst;//线路站点表对象
	//private SelectOperated so;
	private SelectLineToSiteView siteView;
	private SelectSiteToLineView lineView;
	private AdminNextSiteTable anst;
	private TwoStringStruct tls;
	
	
	public DeleteLine(Connection con){
		this.con = con;
		alt = new AdminLineTable(con);            //线路表对象
	    aslt = new AdminSiteToLineTable(con);   //站点线路表对象
	    ast = new AdminSiteTable(con);            //站点表对象
	    alst = new AdminLineToSiteTable(con);   //线路站点表对象
	   // so = new SelectOperated(con);
	    siteView = new SelectLineToSiteView(con);
	    lineView = new SelectSiteToLineView(con);
	    tls = new TwoStringStruct();
	   
	}
	
	//private DeleteLine dl = new DeleteLine(con);;
	
	/*前端需要根据是否true和false来进行显示*/
	public boolean confirmDeleteLineInfo(String lineName){
		int lid = alt.getId(lineName);
		if(lid == 0){
			/*前端显示该条线路不存在*/
			return false;
		}
		else{
			
			/*修改获取的站点对应的站点线路表*/
		    editSiteLineTable(lineName);
		    
			/*删除线路表中对应的信息*/
			alt.delete(lineName);
			
			/*删除对应的线路站点表信息*/
			alst.delete(lid);
			
			/*前端显示该条线路已被删除*/
			return true;
		}
	}
		
	/*修改获取的站点对应的站点线路表*/
	private void  editSiteLineTable(String lineName){
		int lid = alt.getId(lineName);
		//tls = .getLineSiteSeq(lineName);
		tls = siteView.getSeq(lineName);
		String leftStr = tls.get(true);
		String rightStr = tls.get(false);
		
		String[] leftSite = leftStr.split(",");         //得到左行站点sid组成的数组
		String[] rightSite = rightStr.split(",");
		
		editLine(lid,leftSite,true);
		editLine(lid,rightSite,false);
		
	}
	
	private void editLine(int lid,String[] site,boolean isLeft){
		for(int i = 0; i < site.length; i++){
			int sid = Integer.parseInt(site[i]);      //得到站点的sid
			String siteName = ast.getName(sid);
			
			//tls = so.getSiteLineSeq(siteName);
			tls = lineView.getSeq(siteName);
			String lineStr = tls.get(isLeft);
			String leftStr =  tls.get(true);
			String rightStr = tls.get(false);
	
			if(lineStr.contains(",")){            //判断经过该站点的是否只有一条线路
				String[] temp = lineStr.split(",");
				
				int last = Integer.parseInt(temp[temp.length-1]);
				if(last == lid){
					lineStr = lineStr.replace(","+lid+"","");
				}
				else{
					lineStr = lineStr.replace(lid+",","");
				}
			}
			else{                                 //若该站点只有一条线路
				lineStr = "";
			}
			if(isLeft){
				aslt.updateKeyToValue(sid,lineStr,rightStr);
			}
			else{
				aslt.updateKeyToValue(sid,leftStr,lineStr);
			}
		}
	}
	
}


	/*if(isLeft){
				if(leftStr.contains(",")){            //判断经过该站点的是否只有一条线路
					String[] temp = leftStr.split(",");
					if(temp[temp.length] = lid){
						leftStr = leftStr.replace(lid,"");
					}
					else{
						leftStr = leftStr.replace(lid+",","");
					}
				}
				else{                                 //若该站点只有一条线路
					leftStr = "";
				}
				aslt.updateKeyToValue(sid,leftStr,rightStr);
			}
			else{
				if(rightStr.contains(",")){            //判断经过该站点的是否只有一条线路
					String[] temp = rightStr.split(".");
					if(temp[temp.length] = lid){
						rightStr = rightStr.replace(lid,"");
					}
					else{
						rightStr = rightStr.replace(lid+",","");
					}
				}
				else{                                 //若该站点只有一条线路
					rightStr = "";
				}
					aslt.updateKeyToValue(sid,leftStr,rightStr);
			}*/


