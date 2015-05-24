package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * 站点表管理类
 * @author weiliu
 */
public class AdminSiteTable extends AdminInfoTableBase {
	private PreparedStatement pre_selSiteName = null;	// 查询站点名预编译
	private PreparedStatement pre_selFuzzyResult = null;	// 模糊查询预编译
	
	public AdminSiteTable(Connection con) {
		this.con = con;	// 数据库连接成员变量初始化
		initPreparedStatement();
	}
	
	/**
	 * 初始化预编译语句
	 */
	protected void initPreparedStatement() {
		try {
			String insertsql = "insert into SITEINFO(sid,sname) values(?,?)";		
			String updatesql = "update SITEINFO SET sname = ? WHERE sname = ?";		
			String deletesql = "delete FROM SITEINFO WHERE sname = ?";
			String getsidsql = "SELECT sid FROM　SITEINFO　WHERE sname = ?";
			String getSiteName = "SELECT sname FROM　SiteInfo　 WHERE sid = ?";
			String getFuzzyResult = "SELECT sname FROM　SiteInfo　 WHERE sname LIKE ?";
			
			pre_insert = con.prepareStatement(insertsql);	
			pre_update = con.prepareStatement(updatesql);		
			pre_delete = con.prepareStatement(deletesql);
			pre_getId = con.prepareStatement(getsidsql);
			pre_selSiteName = con.prepareStatement(getSiteName);
			pre_selFuzzyResult = con.prepareStatement(getFuzzyResult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 增加站点信息
	 * @param sid
	 * 			站点id
	 * @param sitename
	 * 			站点名
	 */
	public void addSiteInfo(int sid, String sitename) {
		try {
			pre_insert.setInt(1, sid);	
			pre_insert.setString(2, sitename);			
			pre_insert.executeUpdate();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新站点名
	 * @param sitename
	 * @param newname
	 */
	public void updateSite(String sitename, String newname) {
		try {
			pre_update.setString(1, newname);
			pre_update.setString(2, sitename);
			pre_update.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过站点名删除站点
	 * @param sitename
	 * 				站点名
	 */
	public void delete(Object sitename) {
		try {
			pre_delete.setString(1, (String)sitename);
			pre_delete.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过站点名获取站点编号
	 * @param sitename
	 * @return
	 */
	public int getId(Object sitename) {
		int sid = 0;	
		try {
			pre_getId.setString(1, (String)sitename);
			ResultSet rs = pre_getId.executeQuery();
			if(rs.next())
				sid = rs.getInt(1);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sid;
	}

	/**
	 * 获取站点表中站点总数
	 * @return
	 */
	public int getNumber() {
		int siteN = 0;
		String countsql = "select count(*) from SITEINFO";
		try {
			Statement count = con.createStatement();
			ResultSet rs = count.executeQuery(countsql);	// 执行查询
			
			if(rs.next())
				siteN = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return siteN;
	}

	/**
	 * 获得一个新的站点编号
	 * @return
	 */
	public int getNewId() {
		int newsid = 0;
		String newsidsql = "select max(sid) from SITEINFO";
		try {
			Statement maxsid = con.createStatement();
			ResultSet rs = maxsid.executeQuery(newsidsql);	// 执行查询
			
			if(rs.next())
				newsid = rs.getInt(1) + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newsid;
	}

	/**
	 * 通过站点id获取站点名
	 */
	public String getName(int id) {
		String sitename = null;
		try {
			pre_selSiteName.setInt(1, id);		
			ResultSet rs = pre_selSiteName.executeQuery();
			
			if(rs.next())
				sitename = rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sitename;	
	}
	
	/**
	 * 获取模糊查询结果
	 * @param input
	 * @return
	 */
	public Vector<String> getFuzzyResult(String input) {
		Vector<String> fuzzyResult = new Vector<String>();
		try {
			input = "%" + input + "%";
			pre_selFuzzyResult.setString(1, input);	
			ResultSet rs = pre_selFuzzyResult.executeQuery();	// 得到结果集
			
			while(rs.next()) {
				fuzzyResult.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fuzzyResult;
	}
}