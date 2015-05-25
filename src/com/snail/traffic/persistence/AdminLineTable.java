package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 线路表管理类
 * @author weiliu
 *
 */
public class AdminLineTable extends AdminInfoTableBase {
	
	private PreparedStatement pre_selLineName = null;	// 查询线路名预编译
	private PreparedStatement pre_selLineInfo = null;	// 查询线路信息的预编译
	/**
	 * 构造函数
	 * @param con
	 * 			数据库连接
	 */
	public AdminLineTable(Connection con) {
		this.con = con;	// 数据库连接成员变量初始化		
		initPreparedStatement();
	}
	
	/**
	 * 初始化预编译语句
	 */
	protected void initPreparedStatement() {
		try {
			String insertsql = "insert into LINEINFO values(?,?,?,?,?,?,?,?,?,?,?)";
			String deletesql = "delete FROM LINEINFO WHERE lname = ?";
			String getlidsql = "SELECT lid FROM　LINEINFO　WHERE lname = ?";
			String updatesql = "update LINEINFO SET sname=?,linterval=?,lfirstopen=?"
							+ ",llastopen=?,lfirstclose=?,llastclose=?,lprice=?,lcardprice=?,lcompany=?, remark=?"
							+ " WHERE sname = ?";

			String selLineName = "SELECT lname FROM　LineInfo　 WHERE lid = ?";
			String selLineInfo = "SELECT lname, LINTERVAL"
								+ ", LFIRSTOPEN, LLASTOPEN, LFIRSTCLOSE, LLASTCLOSE"
								+ ", LPRICE, LCARDPRICE, LCOMPANY, remark"
								+ " FROM　LineInfo"
								+ " WHERE lname = ?";
			
			pre_insert = con.prepareStatement(insertsql);
			pre_update = con.prepareStatement(updatesql);	
			pre_delete = con.prepareStatement(deletesql);
			pre_getId =  con.prepareStatement(getlidsql);		
			pre_selLineName = con.prepareStatement(selLineName);
			pre_selLineInfo = con.prepareStatement(selLineInfo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 增加线路信息
	 * @param lid
	 * 			线路id
	 * @param linename
	 * 			线路名
	 * @param linterval
	 * 			线路区间
	 * @param lfirstopen
	 * 			首站开出时间
	 * @param llastopen
	 * 			末站开出时间
	 * @param lfirstclose
	 * 			首站结束时间
	 * @param llastclose
	 * 			末站结束时间
	 * @param lprice
	 * 			价格
	 * @param lcardprice
	 * 			刷卡价格
	 * @param lcompany
	 * 			所属公司
	 * @param remark
	 * 			备注
	 */
	public void addLineInfo(int lid
							, String linename
							, String linterval
							, String lfirstopen
							, String llastopen
							, String lfirstclose
							, String llastclose
							, String lprice
							, String lcardprice
							, String lcompany
							, String remark) {
		try {
			pre_insert.setInt(1, lid);
			pre_insert.setString(2, linename);
			pre_insert.setString(3, linterval);
			pre_insert.setString(4, lfirstopen);
			pre_insert.setString(5, llastopen);
			pre_insert.setString(6, lfirstclose);
			pre_insert.setString(7, llastclose);
			pre_insert.setString(8, lprice);
			pre_insert.setString(9, lcardprice);
			pre_insert.setString(10, lcompany);
			pre_insert.setString(11, remark);
			pre_insert.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 更新线路信息
	 * @param linename
	 * @param newName
	 * @param linterval
	 * @param lfirstopen
	 * @param llastopen
	 * @param lfirstclose
	 * @param llastclose
	 * @param lprice
	 * @param lcardprice
	 * @param lcompany
	 */
	public void updateLine(String linename
							, String newName
							, String linterval
							, String lfirstopen
							, String llastopen
							, String lfirstclose
							, String llastclose
							, String lprice
							, String lcardprice
							, String lcompany
							, String remark) {
		try {
			pre_update.setString(1, newName);
			pre_update.setString(2, linterval);
			pre_update.setString(3, lfirstopen);
			pre_update.setString(4, llastopen);
			pre_update.setString(5, lfirstclose);
			pre_update.setString(6, llastclose);
			pre_update.setString(7, lprice);
			pre_update.setString(8, lcardprice);
			pre_update.setString(9, lcompany);
			pre_update.setString(10, linename);
			pre_update.setString(11, remark);
			pre_update.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据线路名删除线路信息
	 * @param lineName
	 * 				线路名
	 */
	public void delete(Object lineName) {
		try {
			pre_delete.setString(1, (String)lineName);
			pre_delete.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据线路名获取线路id
	 * @param lineName
	 * 				线路名
	 */
	public int getId(Object lineName) {
		int lid = 0;	
		try {
			pre_getId.setString(1, (String)lineName);
			ResultSet rs = pre_getId.executeQuery();
			
			if(rs.next())
				lid = rs.getInt(1);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lid;
	}

	/**
	 * 获取线路表记录数
	 */
	public int getNumber() {
		int lineN = 0;
		String countsql = "select count(*) from LINEINFO";
		try {
			Statement count = con.createStatement();
			ResultSet rs = count.executeQuery(countsql);	// 执行查询
			
			if(rs.next())
				lineN = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lineN;
	}

	/**
	 * 获取新的id
	 */
	public int getNewId() {
		int newlid = 0;
		String newlidsql = "select max(lid) from LINEINFO";
		try {
			Statement maxsid = con.createStatement();
			ResultSet rs = maxsid.executeQuery(newlidsql);	// 执行查询
			
			if(rs.next())
				newlid = rs.getInt(1) + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newlid;
	}

	/**
	 * 通过线路id获得线路名
	 */
	public String getName(int lid) {
		String linename = null;	// 线路名
		try {
			pre_selLineName.setInt(1, lid);	// 设置参数		
			ResultSet rs = pre_selLineName.executeQuery();
			
			if(rs.next())
				linename = rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return linename;	
	}

	/**
	 * 根据线路名获取线路信息
	 * @param linename
	 * @return
	 */
	public LineAllInfoStruct getInfo(String linename) {
		LineAllInfoStruct lineinfo = new LineAllInfoStruct();		
		try {
			pre_selLineInfo.setString(1, linename);	
			ResultSet rs = pre_selLineInfo.executeQuery();
			
			if(rs.next()) {
				lineinfo.setName(rs.getString(1));
				lineinfo.setLineRange(rs.getString(2));
				lineinfo.setTime(rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
				lineinfo.setPrice(rs.getString(7), rs.getString(8));
				lineinfo.setOther(rs.getString(9), rs.getString(10));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lineinfo;
	}
}
