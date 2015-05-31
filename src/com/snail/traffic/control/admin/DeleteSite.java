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
 *1.��ǰ�˻�ȡҪɾ����վ����
 *2.�ж�վ���Ƿ����
 *3.�޸���·վ�����Ϣ
 *4.ɾ����Ӧ��վ�����Ϣ,���ڼ�����ϵ����ɾ����Ӧ��վ����·��
 */
public class DeleteSite{
	private Connection con = null;	// �������ݿ�����
	private AdminLineTable alt;	//վ����·�����
	private AdminSiteToLineTable aslt;
	private AdminSiteTable ast;
	private AdminLineToSiteTable alst;//��·վ������
	//private SelectOperated so;
	private SelectLineToSiteView siteView;
	private SelectSiteToLineView lineView;
	private AdminNextSiteTable anst;
	private TwoStringStruct tlsLine,tlsSite;
	private DeleteSite ds;
	
	public DeleteSite(Connection con){
		this.con = con;
		alt = new AdminLineTable(con);            //��·�����
	    aslt = new AdminSiteToLineTable(con);   //վ����·�����
	    ast = new AdminSiteTable(con);            //վ������
	    alst = new AdminLineToSiteTable(con);   //��·վ������
	   // so = new SelectOperated(con);
	    siteView = new SelectLineToSiteView(con);
	    lineView = new SelectSiteToLineView(con);
	    tlsLine = new TwoStringStruct();
	    tlsSite = new TwoStringStruct();
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
		//tlsLine = so.getSiteLineSeq(sitename);  //����վ������ȡ������·����
		tlsLine = lineView.getSeq(sitename);	
		String lineSeq = tlsLine.get(isLeft);   
		String[] lidseq = lineSeq.split(",");
		int sid = ast.getId(sitename);
		
		for(int i = 0 ; i < lidseq.length; i++){
			int lid = Integer.parseInt(lidseq[i]);
			String linename = alt.getName(lid);    //��ȡ��Ӧ��·id��Ӧ����·��
			//tlsSite = so.getLineSiteSeq(linename); //������·����ȡ����վ������
			tlsSite = siteView.getSeq(linename);
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