package com.snail.traffic.control.admin;

import java.sql.Connection;

import com.snail.traffic.persistence.admin.AdminLineToSiteTable;
import com.snail.traffic.persistence.admin.AdminLineTable;
import com.snail.traffic.persistence.admin.AdminNextSiteTable;
import com.snail.traffic.persistence.admin.AdminSiteToLineTable;
import com.snail.traffic.persistence.admin.AdminSiteTable;
import com.snail.traffic.persistence.select.SelectLineToSiteView;
import com.snail.traffic.persistence.select.SelectSiteToLineView;
import com.snail.traffic.container.info.InfoLineStruct;
import com.snail.traffic.container.data.TwoStringStruct;

/*
 *1.更新线路表
 *2.更新线路站点表
 *3.更新站点线路表
 */
public class UpdateLine{
	
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
	
	private UpdateLine ul;
	private InfoLineStruct lais;
	
	public UpdateLine(Connection con){
		this.con = con;
		alt = new AdminLineTable(con);
		aslt =  new AdminSiteToLineTable(con);
		ast = new AdminSiteTable(con);            //站点表对象
		alst =  new AdminLineToSiteTable(con);
		//so =  new SelectOperated(con);
		siteView = new SelectLineToSiteView(con);
		lineView = new SelectSiteToLineView(con);
		anst = new AdminNextSiteTable(con);
		tlsLine = new TwoStringStruct();
		tlsSite = new TwoStringStruct();
	}
	
	
	public InfoLineStruct displayLineInfo(String linename){
		
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
										
		if(newname == null || newname.equals("")){
			return false;
		}
		
		/*更新线路表信息*/
		
		alt.updateLine(linename,newname,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany,remark);
		linename = newname;
		
		int lid = alt.getId(linename);	
		
		//tlsSite = so.getLineSiteSeq(linename);
		tlsSite = siteView.getSeq(linename);
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
		
		
		String sidseqBefore = null;     
		String sidseqAfter = "";            //更新后站点id序列
		
		sidseqBefore = tlsSite.get(isLeft);                      //获取线路对应的站点序列
		String[] sidBefore = sidseqBefore.split(",");            //获取之前线路上的所有sid组成的数组
		
		manageLineAdd(lid, site, isLeft, sidseqAfter);
		manageLineDelete(lid, sidBefore, isLeft, sidseqAfter);
 		
		//主要处理线路中新增的站点
		/*for(int i = 0 ; i < site.length ; i++){
			
			int sid = ast.getId(site[i]);
			//tlsLine = so.getSiteLineSeq(site[i]);
			tlsLine = lineView.getSeq(site[i]);
			String lidseqBefore = tlsLine.get(isLeft);           //获取更新前站点所对应的线路组成的序列
			String lidStr = lidseqBefore;
			String lidLeftBefore =  tlsLine.get(true);
			String lidRightBefore = tlsLine.get(false);
			
			//如果该站点对应的线路id中存在更新的线路id,则证明该站点未改变,否则该站点是新增站点,需要在该站点对应的站点
			//线路表上增加线路信息
			
			//组成更新后的站点id字符串
			if(sidseqAfter.equals("")){
				sidseqAfter = sid + "";
			}
			else{
				sidseqAfter = sidseqAfter + "," + sid;
			}
			
			
			//判断lid在字符串中的位置
			//1.lid在开头和结尾是需要判断首个id是否和lid相等，其他位置需要判断加上前后两个逗号
			
			if(lidStr.startsWith(lid + ",")){
				continue;
			}
			else if(lidStr.endsWith(","+lid)){
				continue;
			}
			else if(lidStr.contains("," + lid + ",")){
				continue;
			}
			else{
				lidStr = lidStr + "," +lid;
				if(isLeft){
					aslt.updateKeyToValue(sid,lidStr,lidRightBefore);
				}
				else{
					aslt.updateKeyToValue(sid,lidLeftBefore,lidStr);
				}
			}
			
			
			
		}*/
		
		//主要处理线路中删除的站点
		//判断之前的站点id是否存在于新站点id序列中
		/*for(int i = 0; i < sidBefore.length; i++){
			int sid = Integer.parseInt(sidBefore[i]);
			//tlsLine = so.getSiteLineSeq(ast.getName(sid));   //得到该站点对应的线路id组成的字符串
			tlsLine = lineView.getSeq(ast.getName(sid));
			String lidseqBefore = tlsLine.get(isLeft);           //获取更新前站点所对应的线路组成的序列
			String lidStr = lidseqBefore;
			String[] temp = lidStr.split(",");
			String lidLeftBefore =  tlsLine.get(true);
			String lidRightBefore = tlsLine.get(false);
			
			//如果之前站点也在更新后站点中,则不改变,否则需要删除对应站点中的lid,需要判断lid在字符串中的位置
			if(sidseqAfter.startsWith(sid+",")){
				continue;
			}
			else if(sidseqAfter.endsWith("," + sid)){
				continue;
			}
			else if( sidseqAfter.contains(","+ sid + ",")){
				continue;
			}
			else{
				//lidLocation(lidStr,lid);
				if(lidStr.contains("," + lid + ",")){
					lidStr = lidStr.replace("," + lid + ",",",");
				}
				else if(lidStr.startsWith(lid + ",")){
			    	
					lidStr = lidStr.replaceFirst( lid + ",","");
				}
				else if(lidStr.endsWith("," + lid)){
					String str = "," + lid;
					int endIndex = lidStr.length() - str.length();
					lidStr = lidStr.substring(0, endIndex);
				}
				if(isLeft){
					aslt.updateKeyToValue(sid,lidStr,lidRightBefore);
				}
				else{
					aslt.updateKeyToValue(sid,lidLeftBefore,lidStr);
				}
			}
			
		}*/
	}
	
	private void manageLineAdd(int lid,String[] site,boolean isLeft,String sidseqAfter){
	    for(int i = 0 ; i < site.length ; i++){
		
		int sid = ast.getId(site[i]);
		//tlsLine = so.getSiteLineSeq(site[i]);
		tlsLine = lineView.getSeq(site[i]);
		String lidseqBefore = tlsLine.get(isLeft);           //获取更新前站点所对应的线路组成的序列
		String lidStr = lidseqBefore;
		String lidLeftBefore =  tlsLine.get(true);
		String lidRightBefore = tlsLine.get(false);
		
		//如果该站点对应的线路id中存在更新的线路id,则证明该站点未改变,否则该站点是新增站点,需要在该站点对应的站点
		//线路表上增加线路信息
		
		//组成更新后的站点id字符串
		if(sidseqAfter.equals("")){
			sidseqAfter = sid + "";
		}
		else{
			sidseqAfter = sidseqAfter + "," + sid;
		}
		
		
		//判断lid在字符串中的位置
		//1.lid在开头和结尾是需要判断首个id是否和lid相等，其他位置需要判断加上前后两个逗号
		
		if(lidStr.startsWith(lid + ",")){
			continue;
		}
		else if(lidStr.endsWith(","+lid)){
			continue;
		}
		else if(lidStr.contains("," + lid + ",")){
			continue;
		}
		else{
			lidStr = lidStr + "," +lid;
			if(isLeft){
				aslt.updateKeyToValue(sid,lidStr,lidRightBefore);
			}
			else{
				aslt.updateKeyToValue(sid,lidLeftBefore,lidStr);
			}
		}
		
		
		
	    }
	}
	
	private void manageLineDelete(int lid,String[] sidBefore,boolean isLeft,String sidseqAfter){
	    for(int i = 0; i < sidBefore.length; i++){
		int sid = Integer.parseInt(sidBefore[i]);
		//tlsLine = so.getSiteLineSeq(ast.getName(sid));   //得到该站点对应的线路id组成的字符串
		tlsLine = lineView.getSeq(ast.getName(sid));
		String lidseqBefore = tlsLine.get(isLeft);           //获取更新前站点所对应的线路组成的序列
		String lidStr = lidseqBefore;
		String[] temp = lidStr.split(",");
		String lidLeftBefore =  tlsLine.get(true);
		String lidRightBefore = tlsLine.get(false);
		
		//如果之前站点也在更新后站点中,则不改变,否则需要删除对应站点中的lid,需要判断lid在字符串中的位置
		if(sidseqAfter.startsWith(sid+",")){
			continue;
		}
		else if(sidseqAfter.endsWith("," + sid)){
			continue;
		}
		else if( sidseqAfter.contains(","+ sid + ",")){
			continue;
		}
		else{
			//lidLocation(lidStr,lid);
			if(lidStr.contains("," + lid + ",")){
				lidStr = lidStr.replace("," + lid + ",",",");
			}
			else if(lidStr.startsWith(lid + ",")){
		    	
				lidStr = lidStr.replaceFirst( lid + ",","");
			}
			else if(lidStr.endsWith("," + lid)){
				String str = "," + lid;
				int endIndex = lidStr.length() - str.length();
				lidStr = lidStr.substring(0, endIndex);
			}
			if(isLeft){
				aslt.updateKeyToValue(sid,lidStr,lidRightBefore);
			}
			else{
				aslt.updateKeyToValue(sid,lidLeftBefore,lidStr);
			}
		}
		
	}
	}
	
	private void updateLineSite(int lid,String[] site,boolean isLeft){
		
		//tlsSite = so.getLineSiteSeq(alt.getName(lid));        //获得线路所对应的站点序列
	        tlsSite = siteView.getSeq(alt.getName(lid));
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