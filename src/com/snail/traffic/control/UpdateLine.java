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
 *1.������·��
 *2.������·վ���
 *3.����վ����·��
 */
public class UpdateLine{
	
	private Connection con = null;	// �������ݿ�����
	private AdminLineTable alt;	//վ����·�����
	private AdminSiteLineTable aslt;
	private AdminSiteTable ast;
	private AdminLineSiteTable alst;//��·վ������
	private SelectOperated so;
	private AdminNextSiteTable anst;
	private TwoLongStruct tlsLine,tlsSite;
	
	private UpdateLine ul;
	private LineAllInfoStruct lais;
	
	public UpdateLine(Connection con){
		this.con = con;
		alt = new AdminLineTable(con);
		aslt =  new AdminSiteLineTable(con);
		ast = new AdminSiteTable(con);            //վ������
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
	
	/*ǰ����Ҫ�����Ƿ�true��false��������ʾ*/
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
										String[] leftSite,  //����վ����ɵ�����
										String[] rightSite){
										
		/*������·����Ϣ*/
		alt.updateLine(linename,newname,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany,remark);
		
		if(newname != null){
			linename = newname;
		}
		int lid = alt.getId(linename);	
		
		/*����վ����·��*/
		updateSiteLine(lid,leftSite,true);
		updateSiteLine(lid,rightSite,false);
		
		/*������·վ���*/
		updateLineSite(lid,leftSite,true);
		updateLineSite(lid,rightSite,false);
		
		return true;
	}
	
	/*
	 *1.��Ҫ������·������վ��,ɾ��վ������
	 *2.ɾ��վ��ʱ��Ҫ�ҵ�ɾ�����ĸ�վ��,���޸�վ����·��
	 */
	private void updateSiteLine(int lid,String[] site,boolean isLeft){
		
		String lidseq = null;
		String siteseqBefote = null;
		String sidAfter = "";
		String lineStr = null;
		String leftStr = null;
		String rightStr = null;
		
		tlsSite = so.getLineSiteSeq(alt.getName(lid));
		siteseqBefote = tlsSite.get(isLeft);                      //��ȡ��·��Ӧ��վ������
		String[] siteBefore = siteseqBefote.split(",");
		
		for(int i = 0 ; i < site.length; i++){
			int sid  = ast.getId(site[i]);            
			tlsLine = so.getSiteLineSeq(site[i]);
			lidseq = tlsLine.get(isLeft);           //��ȡվ������Ӧ����·��ɵ�����
			if(lidseq.contains(lid+"")){
				continue;                       
			}
			else{
				lidseq = lidseq + "," + lid;    //��Ҫ����·������վ������
			}
			if(sidAfter.equals("")){
				sidAfter = sid+"";
			}
			else{
				sidAfter = sidAfter + "," + sid;
			}
			
		}
		//�ж��Ƿ��и�����·ʱ��û��վ��ɾ��,�����,����վ����·����ɾ����Ӧ��lid
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
				
				if(lineStr.contains(",")){            //�жϾ�����վ����Ƿ�ֻ��һ����·
					String[] temp = lineStr.split(",");
					int last = Integer.parseInt(temp[temp.length-1]);
					if( last == lid){
						lineStr = lineStr.replace("," + lid,"");
					}
					else{
						lineStr = lineStr.replace(lid+",","");
					}
				}
				else{                                 //����վ��ֻ��һ����·
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
		
		tlsSite = so.getLineSiteSeq(alt.getName(lid));        //�����·����Ӧ��վ������
		String sidLeft = tlsSite.get(true);
		String sidRight = tlsSite.get(false);
		String sidseq = "";
		
		//��ȡ��Ӧ�ĸ��º��վ������
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