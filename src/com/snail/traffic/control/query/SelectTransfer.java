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
	SelectAccessProcedure selBeAccess = null;
	/**
	 * constructor
	 * @param con
	 */
	public SelectTransfer(Connection con) {
		this.con = con;
	}

	/**
	 * 换乘查询
	 * 首先检查是否直达
	 * 检查是否存在一次换乘
	 * @param start
	 * 	起始上车站点
	 * @param end
	 * 	最终下车站点
	 * @return
	 */
	public InfoStruct query(String start, String end) {
		if (start == null || end == null)
			return null;

		// 当起始站点与终止站点重复时
		if (start.equals(end)) 
			return null;
		
		TotalTimePriorityQueue allSchemes = new TotalTimePriorityQueue();// 锟斤拷锟秸凤拷锟斤拷锟斤拷锟饺讹拷锟斤拷
		selAccess = new SelectDirectAccessProcedure(con);

		BaseAccessVector directV = new DirectAccessVector();
		directV.relateSite = start;
		directV.relateVector = selAccess.getAccessSites(start);

		InfoTransferStruct ret = new InfoTransferStruct();
		ret.start = start;
		ret.end = end;

		// 若方案数大于3个
		if (through(allSchemes, directV, end) >= 3) {
			System.out.println("3 lines");
		}
		else {
			BaseAccessVector be_directV = new BeDirectAccessVector();
			selBeAccess = new SelectBeDirectAccessProcedure(con);
			be_directV.relateSite = end;
			be_directV.relateVector = selBeAccess.getAccessSites(end);
			
			if (onceTransfer(allSchemes, directV, be_directV) >= 3)
				System.out.println("has 3 lines");
			else {
			    twiceTransfer(allSchemes, directV, be_directV, end);
			}	
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
		TransitScheme newScheme = null;
		TransitSection section = null;
		TimePriorityQueue transitVector = direct.getVectorTo(end);
		int size = transitVector.size();

		for (int i = 0; i < size; i++) {
			newScheme = new TransitScheme();
			section = transitVector.poll();	
			newScheme.transitSections.add(section);
			newScheme.time = section.time;
			newScheme.distance = section.distance;
			allScheme.add(newScheme);
		}
		return allScheme.size();
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
				, BaseAccessVector be_direct) {
	    TransitScheme newScheme 		= null;	// new scheme
	    TimePriorityQueue firstSectionQueue 	= null;	
	    TimePriorityQueue secondSectionQueue 	= null;	
	    TransitSection firstSection 		= null;
	    TransitSection secondSection 		= null;

	    Set<String> setIntersection = intersection(direct.getSiteSetFromVector(), be_direct.getSiteSetFromVector());
	    // 循环可换乘站点
	    for (String s: setIntersection) {
		System.out.println(s);

		firstSectionQueue = direct.getVectorTo(s);
		secondSectionQueue = be_direct.getVectorTo(s);
		firstSection = firstSectionQueue.poll();
		secondSection = secondSectionQueue.poll();
			
		System.out.println(firstSection.lineName);
		System.out.println(secondSection.lineName);
			
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

	/**
	 * 二次换乘
	 * @param allScheme
	 * @param direct
	 * @param be_direct
	 * @param end
	 * @return
	 */
	private int twiceTransfer(TotalTimePriorityQueue allScheme
				, BaseAccessVector direct
				, BaseAccessVector be_direct
				, String end) {
	    Set<String> A = direct.getSiteSetFromVector();
	    Set<String> B = be_direct.getSiteSetFromVector();
	    B.add(end);
	    Set<String> diff = difference(A, B);	// 差集，剩下的就是不能直达和不能一次换乘的站点
	    
	    TransitSection firstSection = null;
	    TransitSection secondSection = null;
	    TransitSection thirdSection = null;
	    TransitScheme newScheme = null;	// new scheme
	    TimePriorityQueue firstSectionQueue = null;	
	    TimePriorityQueue secondSectionQueue = null;	
	    TimePriorityQueue thirdSectionQueue = null;	
	    BaseAccessVector tempDirectV = null;
	    Set<String> tempSetDiff = null;
	    Set<String> tempSetInter = null;

	    // 遍历并扩展差集中的站点
	    for (String firstSite: diff) {
		tempDirectV = new DirectAccessVector();
		tempDirectV.relateSite = firstSite;
		tempDirectV.relateVector = selAccess.getAccessSites(firstSite);// 获取s可直达的站点
		tempSetDiff = tempDirectV.getSiteSetFromVector();
		tempSetDiff = difference(tempSetDiff, diff);	// 获得新可到达站点集合中的差集，排除已经知道不可以一次换乘的站点
		tempSetInter = intersection(tempSetDiff, B);	// 差集与可到达B站点的集合求交集
		
		for (String secondSite: tempSetInter) {
		    
		    firstSectionQueue = direct.getVectorTo(firstSite);
		    secondSectionQueue = tempDirectV.getVectorTo(secondSite);
		    thirdSectionQueue = be_direct.getVectorTo(secondSite);
		    firstSection = firstSectionQueue.poll();
		    secondSection = secondSectionQueue.poll();
		    thirdSection = thirdSectionQueue.poll();
		    
		    newScheme = new TransitScheme();
		    newScheme.transitSections.add(firstSection);
		    newScheme.transitSections.add(secondSection);
		    newScheme.transitSections.add(thirdSection);
		    newScheme.time = firstSection.time + secondSection.time + thirdSection.time;
		    newScheme.distance = firstSection.distance + secondSection.distance + thirdSection.distance;
		    allScheme.add(newScheme);
		}
	    }
	    System.out.println("直达+一次换乘 + 二次换乘：" + allScheme.size());
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
			if (i == 4)
			    break;
		}
		return v;
	}

	/**
	 * 对两个集合求交集(准确)
	 * @param setA
	 * 	A集合
	 * @param setB
	 * 	B集合
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
	 * 求差集，setA - setB
	 * @param setA
	 * @param setB
	 * 	a set without same elements
	 * @return
	 */
	private Set<String> difference(Set<String> setA, Set<String> setB) {
	    Set<String> setDifference = new HashSet<>();
	    for(String s: setA) {
		if(!setB.contains(s))
		    setDifference.add(s);
	    }
	    return setDifference;
	}
	
	
	
	
	/*
	public static void main(String[] args) {
		OracleBase oracle = new OracleBase();
		Connection con = oracle.getConnection();
		SelectTransfer a = new SelectTransfer(con);
		Vector<TransitScheme> v = a.query("锟斤拷锟斤拷锟斤拷双锟斤拷", "锟斤拷锟斤拷路岱硷拷山");
		TransitScheme tt = null;
		TransitSection rr = null;
		
	}
	
    */
}
