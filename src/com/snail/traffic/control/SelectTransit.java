package com.snail.traffic.control;

import java.sql.Connection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class SelectTransit {
	private Connection con = null;
	
	// 构造函数
	public SelectTransit(Connection con) {
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
	public Vector<TransitSchemeStruct> query(String start, String end) {
		Vector<TransitSchemeStruct> schemes = new Vector<TransitSchemeStruct>();// 方案向量
		
		
		
		return schemes;
	}
	
	
	
	
	
	
	/**
	 *  求交集
	 *  把数组一放到链表中
	 *  判断数组二中的元素是否在链表中
	 *  若包含，保存至Set集合中
	 * @param arr1
	 * @param arr2
	 * @return 
	 */
    public static String[] intersect(String[] arr1, String[] arr2) {
        List<String> list = new LinkedList<String>();	// 链表
        Set<String> common = new HashSet<String>(); 
        
        for (String str:arr1)
            if (!list.contains(str))
            	list.add(str);

        for (String str:arr2)
            if (list.contains(str))
                common.add(str);

        String[] result = {};
        return common.toArray(result);
    }
}
