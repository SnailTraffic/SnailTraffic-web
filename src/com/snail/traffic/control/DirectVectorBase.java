package com.snail.traffic.control;

import java.util.Set;
import java.util.Vector;


public abstract class DirectVectorBase {
	
	public String relateSite = null;	// 关系站点
	
	public Vector<?> relateVector;	// 向量
	
	/**
	 *  从向量中获取到达sitename的方案
	 */
	public abstract TimePriorityQueue getVectorTo(String name); // 从向量中获取到达sitename的方案
	
	/**
	 * 把向量转换成集合
	 */
	public abstract Set<String> getSiteSetFromVector();	// 把向量转换成集合
	
}
