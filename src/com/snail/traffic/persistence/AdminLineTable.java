package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ��·�������
 * @author weiliu
 *
 */
public class AdminLineTable extends AdminInfoTableBase {
	
	private PreparedStatement pre_selLineName = null;	// ��ѯ��·��Ԥ����
	private PreparedStatement pre_selLineInfo = null;	// ��ѯ��·��Ϣ��Ԥ����
	/**
	 * ���캯��
	 * @param con
	 * 			���ݿ�����
	 */
	public AdminLineTable(Connection con) {
		this.con = con;	// ���ݿ����ӳ�Ա������ʼ��		
		initPreparedStatement();
	}
	
	/**
	 * ��ʼ��Ԥ�������
	 */
	protected void initPreparedStatement() {
		try {
			String insertsql = "insert into LINEINFO values(?,?,?,?,?,?,?,?,?,?,?)";
			String deletesql = "delete FROM LINEINFO WHERE lname = ?";
			String getlidsql = "SELECT lid FROM��LINEINFO��WHERE lname = ?";
			String updatesql = "update LINEINFO SET sname=?,linterval=?,lfirstopen=?"
							+ ",llastopen=?,lfirstclose=?,llastclose=?,lprice=?,lcardprice=?,lcompany=?, remark=?"
							+ " WHERE sname = ?";

			String selLineName = "SELECT lname FROM��LineInfo�� WHERE lid = ?";
			String selLineInfo = "SELECT lname, LINTERVAL"
								+ ", LFIRSTOPEN, LLASTOPEN, LFIRSTCLOSE, LLASTCLOSE"
								+ ", LPRICE, LCARDPRICE, LCOMPANY, remark"
								+ " FROM��LineInfo"
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
	 * ������·��Ϣ
	 * @param lid
	 * 			��·id
	 * @param linename
	 * 			��·��
	 * @param linterval
	 * 			��·����
	 * @param lfirstopen
	 * 			��վ����ʱ��
	 * @param llastopen
	 * 			ĩվ����ʱ��
	 * @param lfirstclose
	 * 			��վ����ʱ��
	 * @param llastclose
	 * 			ĩվ����ʱ��
	 * @param lprice
	 * 			�۸�
	 * @param lcardprice
	 * 			ˢ���۸�
	 * @param lcompany
	 * 			������˾
	 * @param remark
	 * 			��ע
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
	 * ������·��Ϣ
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
	 * ������·��ɾ����·��Ϣ
	 * @param lineName
	 * 				��·��
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
	 * ������·����ȡ��·id
	 * @param lineName
	 * 				��·��
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
	 * ��ȡ��·���¼��
	 */
	public int getNumber() {
		int lineN = 0;
		String countsql = "select count(*) from LINEINFO";
		try {
			Statement count = con.createStatement();
			ResultSet rs = count.executeQuery(countsql);	// ִ�в�ѯ
			
			if(rs.next())
				lineN = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lineN;
	}

	/**
	 * ��ȡ�µ�id
	 */
	public int getNewId() {
		int newlid = 0;
		String newlidsql = "select max(lid) from LINEINFO";
		try {
			Statement maxsid = con.createStatement();
			ResultSet rs = maxsid.executeQuery(newlidsql);	// ִ�в�ѯ
			
			if(rs.next())
				newlid = rs.getInt(1) + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newlid;
	}

	/**
	 * ͨ����·id�����·��
	 */
	public String getName(int lid) {
		String linename = null;	// ��·��
		try {
			pre_selLineName.setInt(1, lid);	// ���ò���		
			ResultSet rs = pre_selLineName.executeQuery();
			
			if(rs.next())
				linename = rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return linename;	
	}

	/**
	 * ������·����ȡ��·��Ϣ
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
