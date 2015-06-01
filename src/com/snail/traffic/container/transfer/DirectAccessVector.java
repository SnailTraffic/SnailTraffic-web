package com.snail.traffic.container.transfer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.snail.traffic.container.access.DirectAccessStruct;
import com.snail.traffic.container.priorityqueue.TimePriorityQueue;

public class DirectAccessVector extends BaseAccessVector {
	
	/**
	 * 可直达的站点向量
	 */
	public DirectAccessVector() {
		relateVector = new ArrayList<DirectAccessStruct>();
	}
		
	/**
	 *  从直达向量中获取到达sitename的方案
	 */
	public TimePriorityQueue getVectorTo(String name) {
		TimePriorityQueue transitVecter = new TimePriorityQueue();
		DirectAccessStruct direct = null;
		TransitSection transit = null;
		
		int size = relateVector.size();
		String str = null;
		// 循环查找目标站点
		for (int i = 0; i < size; i++) {
			direct = (DirectAccessStruct) relateVector.get(i);
			str = direct.siteName;
			// 当找到目标时
			if (str.equals(name)) {
				transit = new TransitSection();	// 必须先申请一个新的内存区域
				transit.startSite = this.relateSite;
				transit.endSite = name;
				transit.lineName = direct.lineName;
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
			siteSet.add(direct.siteName);
		}
		return siteSet;
	}

}
