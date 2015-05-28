package com.snail.traffic.control;

import java.sql.Connection;

import com.snail.traffic.persistence.AdminLineTable;
import com.snail.traffic.persistence.AdminSiteTable;

public class AddSite{
	private Connection con = null;	// �������ݿ�����
	private AdminSiteTable ast;
	
	public AddSite(Connection con){
		this.con = con;
		ast = new AdminSiteTable(con);
	}
	
	/*��Ϊfalseǰ����ʾ��վ���Ѿ�����,������ʾ�������*/
	public boolean confirmAddSite(String siteName){
		
		if(ast.getId(siteName) == 0){
			
			int sid = ast.getNewId();
			ast.addSiteInfo(sid,siteName);
			return true;
		}
		else{
			return false;
		}
	}
}