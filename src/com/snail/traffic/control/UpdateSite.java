package com.snail.traffic.control;

import java.sql.Connection;

import com.snail.traffic.persistence.AdminSiteTable;

public class UpdateSite{
	private Connection con = null;	// �������ݿ�����
	private AdminSiteTable ast;
	
	public UpdateSite( Connection con){
		this.con = con;
		ast = new AdminSiteTable(con);
	}
	
	public boolean confirmUpdateSite(String sitename,String newname){
		if(ast.getId(sitename) == 0){
			/*ǰ����ʾ�޸�վ��*/
			return false;
		}
		else{
			ast.updateSite(sitename,newname);
			return true;
		}
	}
}