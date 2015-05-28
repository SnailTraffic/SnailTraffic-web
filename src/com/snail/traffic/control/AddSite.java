package com.snail.traffic.control;

import java.sql.Connection;

import com.snail.traffic.persistence.AdminLineTable;
import com.snail.traffic.persistence.AdminSiteTable;

public class AddSite{
	private Connection con = null;	// 声明数据库连接
	private AdminSiteTable ast;
	
	public AddSite(Connection con){
		this.con = con;
		ast = new AdminSiteTable(con);
	}
	
	/*若为false前端显示该站点已经存在,否则显示新增完成*/
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