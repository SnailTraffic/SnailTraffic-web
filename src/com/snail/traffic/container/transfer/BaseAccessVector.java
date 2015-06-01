package com.snail.traffic.container.transfer;

import com.snail.traffic.container.priorityqueue.TimePriorityQueue;

import java.util.ArrayList;
import java.util.Set;

public abstract class BaseAccessVector {
	
	public String relateSite = null;// 关系站点

	public ArrayList<?> relateVector;// 向量
	
	/**
	 *  从向量中获取到达sitename的方案
	 */
	public abstract TimePriorityQueue getVectorTo(String name);
	
	/**
	 * 把向量转换成集合
	 */
	public abstract Set<String> getSiteSetFromVector();	// 把向量转换成集合
	
}
