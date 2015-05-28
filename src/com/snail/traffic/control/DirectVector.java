package com.snail.traffic.control;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.snail.traffic.persistence.DirectAccessStruct;

public class DirectVector extends DirectVectorBase{
	
	/**
	 * 可直达的站点向量
	 */
	public DirectVector() {
		relateVector = new Vector<DirectAccessStruct>();
	}
		
	/**
	 *  从直达向量中获取到达sitename的方案
	 */
	public TimePriorityQueue getVectorTo(String name) {	
		TimePriorityQueue transitVecter = new TimePriorityQueue();
		DirectAccessStruct direct = null;
		TransitSToEStruct transit = null;
		
		int size = relateVector.size();
		String str = null;
		// 循环查找目标站点
		for (int i = 0; i < size; i++) {
			direct = (DirectAccessStruct) relateVector.get(i);
			str = direct.sname;
			// 当找到目标时
			if (str.equals(name)) {
				transit = new TransitSToEStruct();	// 必须先申请一个新的内存区域
				transit.startSite = this.relateSite;
				transit.endSite = name;
				transit.lineName = direct.lname;
				transit.isLeft = direct.runLeft;
				transit.time = direct.runTime;
				transit.distance = direct.distance;	
				transitVecter.add(transit);	// 插入向量中
			}
		}
		return transitVecter;
	}

	/**
	 * 把直达向量转换成集合
	 */
	public Set<String> getSiteSetFromVector() {
		Set<String> siteSet = new HashSet<String>(); 
		DirectAccessStruct direct = null;
		
		int size = this.relateVector.size();
		for (int i = 0; i < size; i++) {
			direct = (DirectAccessStruct) relateVector.get(i);
			siteSet.add(direct.sname);
		}
		return siteSet;
	}

}
