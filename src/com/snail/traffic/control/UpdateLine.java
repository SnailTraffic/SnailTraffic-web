package com.snail.traffic.control;

import java.sql.Connection;

import com.snail.traffic.persistence.AdminLineSiteTable;
import com.snail.traffic.persistence.AdminLineTable;
import com.snail.traffic.persistence.AdminNextSiteTable;
import com.snail.traffic.persistence.AdminSiteLineTable;
import com.snail.traffic.persistence.AdminSiteTable;
import com.snail.traffic.persistence.LineAllInfoStruct;
import com.snail.traffic.persistence.SelectOperated;
import com.snail.traffic.persistence.TwoLongStruct;

/*
 *1.更新线路表
 *2.更新线路站点表
 *3.更新站点线路表
 */
public class UpdateLine{
	
	private Connection con = null;	// 声明数据库连接
	private AdminLineTable alt;	//站点线路表对象
	private AdminSiteLineTable aslt;
	private AdminSiteTable ast;
	private AdminLineSiteTable alst;//线路站点表对象
	private SelectOperated so;
	private AdminNextSiteTable anst;
	private TwoLongStruct tlsLine,tlsSite;
	
	private UpdateLine ul;
	private LineAllInfoStruct lais;
	
	public UpdateLine(Connection con){
		this.con = con;
		alt = new AdminLineTable(con);
		aslt =  new AdminSiteLineTable(con);
		ast = new AdminSiteTable(con);            //站点表对象
		alst =  new AdminLineSiteTable(con);
		so =  new SelectOperated(con);
		anst = new AdminNextSiteTable(con);
		tlsLine = new TwoLongStruct();
		tlsSite = new TwoLongStruct();
	}
	
	
	public LineAllInfoStruct displayLineInfo(String linename){
		
		lais = alt.getInfo(linename);
		return lais;
	}
	
	/*前端需要根据是否true和false来进行显示*/
	public boolean confirmUpdateLineInfo(String linename,
	                                    String newname,
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
										
		/*更新线路表信息*/
		alt.updateLine(linename,newname,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany,remark);
		
		if(newname != null){
			linename = newname;
		}
		int lid = alt.getId(linename);	
		
		/*更新站点线路表*/
		updateSiteLine(lid,leftSite,true);
		updateSiteLine(lid,rightSite,false);
		
		/*更新线路站点表*/
		updateLineSite(lid,leftSite,true);
		updateLineSite(lid,rightSite,false);
		
		return true;
	}
	
	/*
	 *1.需要分析线路中增加站点,删除站点的情况
	 *2.删除站点时需要找到删除的哪个站点,并修改站点线路表
	 */
	private void updateSiteLine(int lid,String[] site,boolean isLeft){
		
		String lidseq = null;
		String siteseqBefote = null;
		String sidAfter = "";
		String lineStr = null;
		String leftStr = null;
		String rightStr = null;
		
		tlsSite = so.getLineSiteSeq(alt.getName(lid));
		siteseqBefote = tlsSite.get(isLeft);                      //获取线路对应的站点序列
		String[] siteBefore = siteseqBefote.split(",");
		
		for(int i = 0 ; i < site.length; i++){
			int sid  = ast.getId(site[i]);            
			tlsLine = so.getSiteLineSeq(site[i]);
			lidseq = tlsLine.get(isLeft);           //获取站点所对应的线路组成的序列
			if(lidseq.contains(lid+"")){
				continue;                       
			}
			else{
				lidseq = lidseq + "," + lid;    //主要是线路中增加站点的情况
			}
			if(sidAfter.equals("")){
				sidAfter = sid+"";
			}
			else{
				sidAfter = sidAfter + "," + sid;
			}
			
		}
		//判断是否有更新线路时有没有站点删除,如果有,则在站点线路表中删除对应的lid
		for(int i1 = 0; i1 < siteBefore.length; i1++) {
			if( sidAfter.contains(siteBefore[i1])){
				break;
			}
			else{
				String siteName = ast.getName(Integer.parseInt(siteBefore[i1]));
			
				tlsLine = so.getSiteLineSeq(siteName);
				lineStr = tlsLine.get(isLeft);
				leftStr =  tlsLine.get(true);
				rightStr = tlsLine.get(false);
				
				if(lineStr.contains(",")){            //判断经过该站点的是否只有一条线路
					String[] temp = lineStr.split(",");
					int last = Integer.parseInt(temp[temp.length-1]);
					if( last == lid){
						lineStr = lineStr.replace("," + lid,"");
					}
					else{
						lineStr = lineStr.replace(lid+",","");
					}
				}
				else{                                 //若该站点只有一条线路
					lineStr = "";
				}
				
			}
			
		}
		
		for(int j = 0; j < site.length; j++){
			int sid  = ast.getId(site[j]);
			if(isLeft){
				aslt.updateKeyToValue(sid,lineStr,rightStr);
			}
			else{
				aslt.updateKeyToValue(sid,leftStr,lineStr);
			}
		}
		
	}
	
	private void updateLineSite(int lid,String[] site,boolean isLeft){
		
		tlsSite = so.getLineSiteSeq(alt.getName(lid));        //获得线路所对应的站点序列
		String sidLeft = tlsSite.get(true);
		String sidRight = tlsSite.get(false);
		String sidseq = "";
		
		//获取对应的更新后的站点序列
		for(int i = 0 ; i < site.length; i++){
			
            //String sidseqLeft = ;                          
			//String sidseqRight = ;
            int sid  = ast.getId(site[i]);  			
			if(sidseq.equals("")){
				sidseq = sid+"";
			}
			else{
				sidseq = sidseq + "," + sid;
			}
		
		}
		if(isLeft){
			alst.updateKeyToValue(lid,sidseq,sidRight);
		}
		else{
			alst.updateKeyToValue(lid,sidLeft,sidseq);
		}
	}
	
}