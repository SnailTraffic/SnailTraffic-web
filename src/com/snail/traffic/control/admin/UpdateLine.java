package com.snail.traffic.control.admin;

import java.sql.Connection;

import com.snail.traffic.persistence.admin.AdminLineToSiteTable;
import com.snail.traffic.persistence.admin.AdminLineTable;
import com.snail.traffic.persistence.admin.AdminNextSiteTable;
import com.snail.traffic.persistence.admin.AdminSiteToLineTable;
import com.snail.traffic.persistence.admin.AdminSiteTable;
import com.snail.traffic.container.info.InfoLineStruct;
import com.snail.traffic.persistence.select.SelectOperated;
import com.snail.traffic.container.data.TwoStringStruct;

/*
 *1.������·��
 *2.������·վ���
 *3.����վ����·��
 */
public class UpdateLine{
	
	private Connection con = null;	// �������ݿ�����
	private AdminLineTable alt;	//վ����·�����
	private AdminSiteToLineTable aslt;
	private AdminSiteTable ast;
	private AdminLineToSiteTable alst;//��·վ������
	private SelectOperated so;
	private AdminNextSiteTable anst;
	private TwoStringStruct tlsLine,tlsSite;
	
	private UpdateLine ul;
	private InfoLineStruct lais;
	
	public UpdateLine(Connection con){
		this.con = con;
		alt = new AdminLineTable(con);
		aslt =  new AdminSiteToLineTable(con);
		ast = new AdminSiteTable(con);            //վ������
		alst =  new AdminLineToSiteTable(con);
		so =  new SelectOperated(con);
		anst = new AdminNextSiteTable(con);
		tlsLine = new TwoStringStruct();
		tlsSite = new TwoStringStruct();
	}
	
	
	public InfoLineStruct displayLineInfo(String linename){
		
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
										
		if(newname == null || newname.equals("")){
			return false;
		}
		
		/*������·����Ϣ*/
		
		alt.updateLine(linename,newname,linterval,lfirstopen,llastopen,lfirstclose,llastclose,lprice,lcardprice,lcompany,remark);
		linename = newname;
		
		int lid = alt.getId(linename);	
		
		tlsSite = so.getLineSiteSeq(linename);
		
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
		
		
		String sidseqBefore = null;     
		String sidseqAfter = "";            //���º�վ��id����
		
		sidseqBefore = tlsSite.get(isLeft);                      //��ȡ��·��Ӧ��վ������
		String[] sidBefore = sidseqBefore.split(",");            //��ȡ֮ǰ��·�ϵ�����sid��ɵ�����
 		
		//��Ҫ������·��������վ��
		for(int i = 0 ; i < site.length ; i++){
			
			int sid = ast.getId(site[i]);
			tlsLine = so.getSiteLineSeq(site[i]);
			String lidseqBefore = tlsLine.get(isLeft);           //��ȡ����ǰվ������Ӧ����·��ɵ�����
			String lidStr = lidseqBefore;
			String lidLeftBefore =  tlsLine.get(true);
			String lidRightBefore = tlsLine.get(false);
			
			//�����վ���Ӧ����·id�д��ڸ��µ���·id,��֤����վ��δ�ı�,�����վ��������վ��,��Ҫ�ڸ�վ���Ӧ��վ��
			//��·����������·��Ϣ
			
			//��ɸ��º��վ��id�ַ���
			if(sidseqAfter.equals("")){
				sidseqAfter = sid + "";
			}
			else{
				sidseqAfter = sidseqAfter + "," + sid;
			}
			
			
			//�ж�lid���ַ����е�λ��
			//1.lid�ڿ�ͷ�ͽ�β����Ҫ�ж��׸�id�Ƿ��lid��ȣ�����λ����Ҫ�жϼ���ǰ����������
			
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
		
		//��Ҫ������·��ɾ����վ��
		//�ж�֮ǰ��վ��id�Ƿ��������վ��id������
		for(int i = 0; i < sidBefore.length; i++){
			int sid = Integer.parseInt(sidBefore[i]);
			tlsLine = so.getSiteLineSeq(ast.getName(sid));   //�õ���վ���Ӧ����·id��ɵ��ַ���
			
			String lidseqBefore = tlsLine.get(isLeft);           //��ȡ����ǰվ������Ӧ����·��ɵ�����
			String lidStr = lidseqBefore;
			String[] temp = lidStr.split(",");
			String lidLeftBefore =  tlsLine.get(true);
			String lidRightBefore = tlsLine.get(false);
			
			//���֮ǰվ��Ҳ�ڸ��º�վ����,�򲻸ı�,������Ҫɾ����Ӧվ���е�lid,��Ҫ�ж�lid���ַ����е�λ��
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
				//lid����·�ַ��������λ�ú�����λ��
				//int a = Integer.parseInt(temp[temp.length - 1]);
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
	
	/*
	
	 *1.��Ҫ������·������վ��,ɾ��վ������
	 *2.ɾ��վ��ʱ��Ҫ�ҵ�ɾ�����ĸ�վ��,���޸�վ����·��
	 
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
		
	}*/
	
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