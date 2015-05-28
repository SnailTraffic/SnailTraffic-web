package com.snail.traffic.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;

import com.snail.traffic.control.TransitSToEStruct;

/**
 * 查询数据库类
 * 
 * 此类中只做最基本的数据库视图查询
 * 
 * 只需要定义好查询的API,至于查询后干什么用，查询后得到什么格式
 * 
 * 我都不管，啦啦啦
 * 
 * @author weiliu
 *
 */
public class SelectOperated {
	private Connection con = null;	// 声明数据库连接
	
	private PreparedStatement pre_viewSiteLine = null;	// 查询站点线路视图预编译
	private PreparedStatement pre_viewLineSite = null;	// 查询线路站点视图预编译
	private CallableStatement pre_DirectAccess = null;	// 直达站点查询预编译
	private CallableStatement pre_BeDirectAccess = null;// 直达站点查询预编译
	private CallableStatement pre_StartToEnd = null;	// 起点到中转站点序列 
	
	private ResultSet rs = null;
	
	public SelectOperated(Connection con) {
		this.con = con;
		initPreparedStatement();	// 初始化预编译
	}
	
	/**
	 * 初始化预编译语句
	 */
	private void initPreparedStatement() {
		try {
			String viewSiteLine = "SELECT llidseq, rlidseq FROM　View_SiteLine　 WHERE sname = ?";	
			String viewLineSite = "SELECT lsidseq, rsidseq FROM　View_LineSite　 WHERE lname = ?";

			pre_viewSiteLine = con.prepareStatement(viewSiteLine);
			pre_viewLineSite = con.prepareStatement(viewLineSite);	
			pre_DirectAccess = con.prepareCall("BEGIN DIRECTACCESS(?, ?); END;");
			pre_BeDirectAccess = con.prepareCall("BEGIN BEDIRECTACCESS(?, ?, ?); END;");
			pre_StartToEnd = con.prepareCall("BEGIN ROUTE(?, ?, ?, ?, ?); END;");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 【查视图】获取左行与右行站点线路序列字符串API
	 * @param sitename
	 * 				站点名
	 * @return siteline
	 * 				站点线路(包括左边，右边)
	 */
	public TwoLongStruct getSiteLineSeq(String sitename) {
		
		TwoLongStruct siteline = new TwoLongStruct();	// 两边站点线路序列字符串
		
		try {
			pre_viewSiteLine.setString(1, sitename);
			
			ResultSet rs = pre_viewSiteLine.executeQuery();
			
			if(rs.next()) {
				siteline.put(true, rs.getString(1));	// 保存左边线路集合字符串
				siteline.put(false, rs.getString(2));	// 保存右边线路集合字符串
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		return siteline;
	}
	
	/**
	 * 获取线路站点表左行站点序列与右行站点序列
	 * @param lname
	 * 			线路名
	 * @return linesite
	 * 				线路站点枚举map（左边、右边）
	 */
	public TwoLongStruct getLineSiteSeq(String lname) {
		
		TwoLongStruct linesite = new TwoLongStruct();	// 获取两边站点线路序列字符串
		
		try {
			pre_viewLineSite.setString(1, lname);
			
			ResultSet rs = pre_viewLineSite.executeQuery();
			
			if (rs.next()) {
				linesite.put(true, rs.getString(1));	// 保存左边站点集合字符串
				linesite.put(false, rs.getString(2));	// 保存右边站点集合字符串
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		return linesite;
	}
	
	/**
	 * 通过起始站点名与线路名获得下个站点名与到达下个站点的时间与距离
	 * @param start
	 * 			起始站点
	 * @param line
	 * 			线路名
	 * @return
	 * 
	 */
//	public TransitSToEStruct getNextSite(String start, String line) {
//		TransitSToEStruct ste = new TransitSToEStruct();
//		CallableStatement proc = null;
//	      try {
//			proc = con.prepareCall("{ call G7.SELECTNEXTSITE(?,?,?,?,?,?) }");
//		    proc.setString(1, start);
//		    proc.setString(2, line);
//		    
//		    proc.registerOutParameter(3, Types.INTEGER);
//		    proc.registerOutParameter(4, Types.VARCHAR);
//		    proc.registerOutParameter(5, Types.INTEGER);
//		    proc.registerOutParameter(6, Types.INTEGER);
//		    proc.execute();
//		    
//		    ste.isLeft = proc.getInt(3);
//		    ste.endSite = proc.getString(4);	// 下一个站点
//		    ste.time = proc.getInt(5);
//		    ste.distance = proc.getInt(6);
//		    
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		ste.startSite = start;
//		ste.lineName = line;
//		return ste;
//	}
	
	/**
	 * 获取起始站点的可直达站点向量
	 * @param startSite
	 * 				起始站点
	 * @return directSites
	 * 				可直达站点的信息向量
	 */
	public Vector<DirectAccessStruct> getDirectAccessSites(String startSite) {
		Vector<DirectAccessStruct> directSites = new Vector<DirectAccessStruct>();// 可直达站点向量
		DirectAccessStruct newElement;
		try {
			pre_DirectAccess.setString(1, startSite);
			pre_DirectAccess.registerOutParameter(2, OracleTypes.CURSOR);
			pre_DirectAccess.execute();
			rs = ((OracleCallableStatement)pre_DirectAccess).getCursor(2);
			
			while (rs.next()) {
				newElement = new DirectAccessStruct();
				newElement.sname = rs.getString("SNAME");
				newElement.lname = rs.getString("LNAME");
				newElement.runLeft = rs.getInt("RUNLEFT");
				newElement.runTime = rs.getInt("RUNTIME");
				newElement.distance = rs.getInt("DISTANCE");
				// System.out.println(newElement.sname);
				directSites.add(newElement);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		for(int i = 0; i < directSites.size(); i++)
//			System.out.println(directSites.get(i).sname);
		return directSites;
	}
	
	/**
	 * 获取终止站点的可被直达站点向量
	 * 注意：由于数据库中的临时表是会话级的，从连接成功开始保留数据，直到数据连接断开，
	 * 因此临时表中包含上一次的站点信息，
	 * 因此，需要用上一次的站点数来排除之前的内容
	 * @param endSite
	 * @param direct_len
	 * @return
	 */
	public Vector<BeDirectAccessStruct> get_Be_DirectAccessSites(String endSite,int direct_len) {
		Vector<BeDirectAccessStruct> directSites = new Vector<BeDirectAccessStruct>();// 可直达站点向量
		BeDirectAccessStruct newElement = null;
		
		try {
			pre_BeDirectAccess.setString(1, endSite);
			pre_BeDirectAccess.setInt(2, direct_len);
			pre_BeDirectAccess.registerOutParameter(3, OracleTypes.CURSOR);
			pre_BeDirectAccess.execute();
			rs = ((OracleCallableStatement)pre_BeDirectAccess).getCursor(3);
			
			while (rs.next()) {
				newElement = new BeDirectAccessStruct();
				newElement.sname = rs.getString("SNAME");
				newElement.lname = rs.getString("LNAME");
				newElement.runLeft = rs.getInt("RUNLEFT");
				directSites.add(newElement);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return directSites;
	}
	
	public Vector<String> getRoute(String start, String end, String line, int runleft) {
		Vector<String> route = new Vector<String>();
		String newElement = null;
		try {
			pre_StartToEnd.setString(1, start);
			pre_StartToEnd.setString(2, end);
			pre_StartToEnd.setString(3, line);
			pre_StartToEnd.setInt(4, runleft);
			pre_StartToEnd.registerOutParameter(5, OracleTypes.CURSOR);
			
			pre_StartToEnd.execute();
			rs = ((OracleCallableStatement)pre_StartToEnd).getCursor(5);
	
			while (rs.next()) {
				newElement = new String();
				newElement = rs.getString("SNAME");
				route.add(newElement);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		for(int i = 0; i < route.size(); i++)
//			System.out.println(route.get(i));
//		
		return route;
	}
	
	
	public static void main(String[] args) {
		OracleBase oracle = new OracleBase();
		Connection con = oracle.getConnection();
		SelectOperated aa = new SelectOperated(con);
		//Vector<DirectAccessStruct>  n = aa.getDirectAccessSites("建设大道双墩");
		aa.getRoute("建设大道双墩", "解放大道仁寿路", "1路", 1);
		//aa.get_Be_DirectAccessSites("汉黄路岱家山", n.size());
	}
	
}
