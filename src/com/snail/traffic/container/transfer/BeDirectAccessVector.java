package com.snail.traffic.container.transfer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.snail.traffic.container.access.BaseAccessStruct;
import com.snail.traffic.container.access.BeDirectAccessStruct;
import com.snail.traffic.container.priorityqueue.TimePriorityQueue;

public class BeDirectAccessVector extends BaseAccessVector {

	/**
	 * 被直达的站点向量
	 */
	public BeDirectAccessVector() {
		relateVector = new ArrayList<BeDirectAccessStruct>();
	}

	/**
	 *  从被直达向量中获取到达sitename的方案
	 */
	public TimePriorityQueue getVectorTo(String name) {
		TimePriorityQueue transitVector = new TimePriorityQueue();
		BeDirectAccessStruct direct = null;
		TransitSection transit = null;
		
		int size = relateVector.size();
		String str = null;
		// 循环查找目标站点
		for (int i = 0; i < size; i++) {
			direct = (BeDirectAccessStruct) relateVector.get(i);
			str = direct.siteName;
			// 当找到目标时
			if (str.equals(name)) {
				transit = new TransitSection();	// 必须先申请一个新的内存区域
				transit.startSite = name;
				transit.endSite = this.relateSite;
				transit.lineName = direct.lineName;
				transit.isLeft = direct.runLeft;
				transitVector.add(transit);	// 插入向量中
			}
		}
		return transitVector;
	}

	/**
	 * 把被直达向量转换成集合
	 */
	public Set<String> getSiteSetFromVector() {
		Set<String> siteSet = new HashSet<>();
		BeDirectAccessStruct direct = null;
		
		int size = this.relateVector.size();
		for (int i = 0; i < size; i++) {
			direct = (BeDirectAccessStruct) relateVector.get(i);
			siteSet.add(direct.siteName);
		}
		return siteSet;
	}
}
