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
 *1.��ǰ�˶�����Ҫɾ������·��
 *2.�ж��Ƿ���ڸ���·��
 *3.ɾ����·���и�����·�����е���Ϣ(��·��)
 *4.��ȡ��·վ����е�վ����Ϣ
 *5.���ݲ���5,�޸Ļ�ȡ��վ���Ӧ��վ����·��
 *6.ɾ����Ӧ����·վ�����Ϣ
 */


public class DeleteLine{
	
	private Connection con = null;	// �������ݿ�����
	private AdminLineTable alt;	//վ����·�����
	private AdminSiteLineTable aslt;
	private AdminSiteTable ast;
	private AdminLineSiteTable alst;//��·վ������
	private SelectOperated so;
	private AdminNextSiteTable anst;
	private TwoLongStruct tls;
	
	
	public DeleteLine(Connection con){
		this.con = con;
		alt = new AdminLineTable(con);            //��·�����
	    aslt = new AdminSiteLineTable(con);   //վ����·�����
	    ast = new AdminSiteTable(con);            //վ������
	    alst = new AdminLineSiteTable(con);   //��·վ������
	    so = new SelectOperated(con);
	    tls = new TwoLongStruct();
	   
	}
	
	//private DeleteLine dl = new DeleteLine(con);;
	
	/*ǰ����Ҫ�����Ƿ�true��false��������ʾ*/
	public boolean confirmDeleteLineInfo(String lineName){
		int lid = alt.getId(lineName);
		if(lid == 0){
			/*ǰ����ʾ������·������*/
			return false;
		}
		else{
			
			/*�޸Ļ�ȡ��վ���Ӧ��վ����·��*/
		    editSiteLineTable(lineName);
		    
			/*ɾ����·���ж�Ӧ����Ϣ*/
			alt.delete(lineName);
			
			/*ɾ����Ӧ����·վ�����Ϣ*/
			alst.deleteKey(lid);
			
			/*ǰ����ʾ������·�ѱ�ɾ��*/
			return true;
		}
	}
		
	/*�޸Ļ�ȡ��վ���Ӧ��վ����·��*/
	private void  editSiteLineTable(String lineName){
		int lid = alt.getId(lineName);
		tls = so.getLineSiteSeq(lineName);
		String leftStr = tls.get(true);
		String rightStr = tls.get(false);
		
		String[] leftSite = leftStr.split(",");         //�õ�����վ��sid��ɵ�����
		String[] rightSite = rightStr.split(",");
		
		editLine(lid,leftSite,true);
		editLine(lid,rightSite,false);
		
	}
	
	private void editLine(int lid,String[] site,boolean isLeft){
		for(int i = 0; i < site.length; i++){
			int sid = Integer.parseInt(site[i]);      //�õ�վ���sid
			String siteName = ast.getName(sid);
			
			tls = so.getSiteLineSeq(siteName);
			String lineStr = tls.get(isLeft);
			String leftStr =  tls.get(true);
			String rightStr = tls.get(false);
	
			if(lineStr.contains(",")){            //�жϾ�����վ����Ƿ�ֻ��һ����·
				String[] temp = lineStr.split(",");
				
				int last = Integer.parseInt(temp[temp.length-1]);
				if(last == lid){
					lineStr = lineStr.replace(","+lid+"","");
				}
				else{
					lineStr = lineStr.replace(lid+",","");
				}
			}
			else{                                 //����վ��ֻ��һ����·
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
				if(leftStr.contains(",")){            //�жϾ�����վ����Ƿ�ֻ��һ����·
					String[] temp = leftStr.split(",");
					if(temp[temp.length] = lid){
						leftStr = leftStr.replace(lid,"");
					}
					else{
						leftStr = leftStr.replace(lid+",","");
					}
				}
				else{                                 //����վ��ֻ��һ����·
					leftStr = "";
				}
				aslt.updateKeyToValue(sid,leftStr,rightStr);
			}
			else{
				if(rightStr.contains(",")){            //�жϾ�����վ����Ƿ�ֻ��һ����·
					String[] temp = rightStr.split(".");
					if(temp[temp.length] = lid){
						rightStr = rightStr.replace(lid,"");
					}
					else{
						rightStr = rightStr.replace(lid+",","");
					}
				}
				else{                                 //����վ��ֻ��һ����·
					rightStr = "";
				}
					aslt.updateKeyToValue(sid,leftStr,rightStr);
			}*/


