package com.snail.traffic.control.query;

import java.sql.Connection;
import java.util.*;

import com.snail.traffic.container.info.InfoStruct;
import com.snail.traffic.container.priorityqueue.TimePriorityQueue;
import com.snail.traffic.container.priorityqueue.TotalTimePriorityQueue;
import com.snail.traffic.container.transfer.*;
import com.snail.traffic.persistence.select.*;
import com.snail.traffic.container.info.InfoTransferStruct;

public class SelectTransfer {
	Connection con = null;
	SelectAccessProcedure selAccess = null;

	// 构造函数
	public SelectTransfer(Connection con) {
		this.con = con;
	}

	/**
	 * 换乘查询
	 * 首先检查是否直达
	 * 检查是否存在一次换乘
	 * @param start
	 * 			起始上车站点
	 * @param end
	 * 			最终下车站点
	 * @return
	 */
	public InfoStruct query(String start, String end) {
		if (start == null || end == null)
			return null;

		// 当起始站点与终止站点重复时
		if (start.equals(end)) 
			return null;
		
		TotalTimePriorityQueue allSchemes = new TotalTimePriorityQueue();// 最终方案优先队列
		selAccess = new SelectDirectAccessProcedure(con);

		BaseAccessVector directV = new DirectAccessVector();	// 直达站点集合
		directV.relateSite = start;
		directV.relateVector = selAccess.getAccessSites(start);

		InfoTransferStruct ret = new InfoTransferStruct();
		
		// 若方案数大于3个
		if (through(allSchemes, directV, end) >= 3) {
			System.out.println("可直达线路达三条");
		}
		// 若获取直达方案后，方案数小于3个
		else {
			BaseAccessVector be_directV = new BeDirectAccessVector();
			selAccess = new SelectBeDirectAccessProcedure(con);
			be_directV.relateSite = end;
			be_directV.relateVector = selAccess.getAccessSites(end);
			Set<String> setIntersection = intersection(directV.getSiteSetFromVector(), be_directV.getSiteSetFromVector());
			
			if (onceTransfer(allSchemes, directV, be_directV, setIntersection) >= 3)
				System.out.println("直达与可一次换乘的线路达三条");
			else
				System.out.println("开展二次换乘运算");
		}
		ret.schemes = getPriorityRoute(allSchemes);
		return ret;
	}
	
	/**
	 * 根据目标站点获取直达方案
	 * 一个直达乘车方案就是一个方案
	 * @param Scheme
	 * 			方案向量
	 * @param vector
	 * 			起始站点向量
	 * @param end
	 * 			目标站点
	 * @return size
	 * 			方案数
	 */
	private int through(TotalTimePriorityQueue allScheme, BaseAccessVector direct, String end) {
		TimePriorityQueue transitVector = direct.getVectorTo(end); // 获得能直达终点的所有线路
		TransitScheme newScheme = null;
		TransitSection section = null;
		int size = transitVector.size();

		for (int i = 0; i < size; i++) {
			newScheme = new TransitScheme();
			section = transitVector.poll();	// 获得优先队列中的时间最优元素
			newScheme.transitSections.add(section);
			newScheme.time = section.time;
			newScheme.distance = section.distance;
			allScheme.add(newScheme);
		}
		// System.out.println("直达：" + allScheme.size());
		return allScheme.size();
	}

	/**
	 * get 5 Schemes from  Priority queue
	 * @param allSchemes
	 * @return
	 */
	private Vector<TransitScheme> getPriorityRoute(TotalTimePriorityQueue allSchemes) {
		Vector<TransitScheme> v = new Vector<>();
		SelectRouteProcedure selRoute = new SelectRouteProcedure(this.con);
		TransitScheme scheme = null;
		TransitSection section = null;
		int sectionSize = 0;
		int queueSize = allSchemes.size();

		for (int i = 0; i < queueSize; i++) {
			scheme = allSchemes.poll();
			sectionSize = scheme.transitSections.size();
			// get route in the section
			for (int j = 0; j < sectionSize; j++) {
				section = scheme.transitSections.get(j);
				section.route = selRoute.getRoute(section.startSite, section.endSite, section.lineName, section.isLeft);
			}
			v.add(scheme);
			if (i == 4)    // 有5个方案即结束
				break;
		}
		return v;
	}

	/**
	 * 对两个集合求交集(准确)
	 * @param setA
	 * 			A集合
	 * @param setB
	 * 			B集合
	 * @return
	 */
	private Set<String> intersection(Set<String> setA, Set<String> setB) {
		Set<String> setIntersection = new HashSet<>();
		String s = null;
		Iterator<String> iterA = setA.iterator();
		while (iterA.hasNext()) {
			s = iterA.next();
			if(setB.contains(s))
				setIntersection.add(s);
		}
		return setIntersection;
	}

	/**
	 * 一次换乘查询
	 * @param allScheme
	 * 			所有方案
	 * @param direct
	 * 			起始站点直达站点
	 * @param be_direct
	 * 			终止站点被直达站点
	 * @param interset
	 * 			交集（可换乘站点）
	 * @return 当前方案数
	 */
	private int onceTransfer(TotalTimePriorityQueue allScheme
							, BaseAccessVector direct
							, BaseAccessVector be_direct
							, Set<String> interSet) {
		TransitScheme newScheme 				= null;	// 新的方案
		TimePriorityQueue firstSectionQueue 	= null;	// 第一段公交的所有可选方案优先队列
		TimePriorityQueue secondSectionQueue 	= null;	// 第二段公交的所有可选方案优先队列
		TransitSection firstSection 			= null;	// 第一段公交
		TransitSection secondSection 			= null;	// 第二段公交

		// 循环可换乘站点
		for (String s: interSet) {
			System.out.println(s);

			firstSectionQueue = direct.getVectorTo(s);
			secondSectionQueue = be_direct.getVectorTo(s);
			firstSection = firstSectionQueue.poll();
			secondSection = secondSectionQueue.poll();
			
			System.out.println(firstSection.lineName);
			System.out.println(secondSection.lineName);
			
			// 构造新方案元素
			newScheme = new TransitScheme();
			newScheme.transitSections.add(firstSection);
			newScheme.transitSections.add(secondSection);
			newScheme.time = firstSection.time + secondSection.time;
			newScheme.distance = firstSection.distance + secondSection.distance;
			allScheme.add(newScheme);
		}
		System.out.println("直达+一次换乘" + allScheme.size());
		return allScheme.size();
	}

	/*
	
	public static void main(String[] args) {
		OracleBase oracle = new OracleBase();
		Connection con = oracle.getConnection();
		SelectTransfer a = new SelectTransfer(con);
		Vector<TransitScheme> v = a.query("建设大道双墩", "汉黄路岱家山");
		TransitScheme tt = null;
		TransitSection rr = null;
		
	}
	
    */
}
