package com.snail.traffic.control;

import java.sql.Connection;

import com.snail.traffic.persistence.AdminSiteTable;

public class UpdateSite{
	private Connection con = null;	// 声明数据库连接
	private AdminSiteTable ast;
	
	public UpdateSite( Connection con){
		this.con = con;
		ast = new AdminSiteTable(con);
	}
	
	public boolean confirmUpdateSite(String sitename,String newname){
		if(ast.getId(sitename) == 0){
			/*前端显示无该站点*/
			return false;
		}
		else{
			ast.updateSite(sitename,newname);
			return true;
		}
	}
}