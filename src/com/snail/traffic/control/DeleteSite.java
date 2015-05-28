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
 *1.��ǰ�˻�ȡҪɾ����վ����
 *2.�ж�վ���Ƿ����
 *3.�޸���·վ�����Ϣ
 *4.ɾ����Ӧ��վ�����Ϣ,���ڼ�����ϵ����ɾ����Ӧ��վ����·��
 */
public class DeleteSite{
	private Connection con = null;	// �������ݿ�����
	private AdminLineTable alt;	//վ����·�����
	private AdminSiteLineTable aslt;
	private AdminSiteTable ast;
	private AdminLineSiteTable alst;//��·վ������
	private SelectOperated so;
	private AdminNextSiteTable anst;
	private TwoLongStruct tlsLine,tlsSite;
	private DeleteSite ds;
	
	public DeleteSite(Connection con){
		this.con = con;
		alt = new AdminLineTable(con);            //��·�����
	    aslt = new AdminSiteLineTable(con);   //վ����·�����
	    ast = new AdminSiteTable(con);            //վ������
	    alst = new AdminLineSiteTable(con);   //��·վ������
	    so = new SelectOperated(con);
	    tlsLine = new TwoLongStruct();
	    tlsSite = new TwoLongStruct();
	}
	
	/*������ֵΪfalse��ǰ����ʾ�����ڸ�վ��,������ʾɾ�����*/
	public boolean confirmDeleteSite(String sitename) {
		if(ast.getId(sitename) == 0){
			return false;
		}
		//ɾ��վ����·��ʱ��Ҫ�ȴ���·վ���
		else{
			//������·վ���
			editLineSite(sitename,true);
			editLineSite(sitename,false);
			
			//ɾ��վ����վ����·��
			ast.delete(sitename);
			
			return true;
		}
	}
	
	private void editLineSite(String sitename,boolean isLeft){
		tlsLine = so.getSiteLineSeq(sitename);  //����վ������ȡ������·����
			
		String lineSeq = tlsLine.get(isLeft);   
		String[] lidseq = lineSeq.split(",");
		int sid = ast.getId(sitename);
		
		for(int i = 0 ; i < lidseq.length; i++){
			int lid = Integer.parseInt(lidseq[i]);
			String linename = alt.getName(lid);    //��ȡ��Ӧ��·id��Ӧ����·��
			tlsSite = so.getLineSiteSeq(linename); //������·����ȡ����վ������
			String siteSeq = tlsSite.get(isLeft);  //siteSeq��վ����ɵ��ַ���
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