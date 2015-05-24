package com.snail.traffic.control;

import java.util.Vector;

/**
 * 方案结构体
 * @author weiliu
 *
 */
public class TransitSchemeStruct {
	private Vector<TransitSToEStruct> transitLine = new Vector<TransitSToEStruct>();// 换乘线路向量
	public String time = null;	// 方案所需时间
	public String distance = null;	// 方案距离
}
