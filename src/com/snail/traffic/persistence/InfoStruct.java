package com.snail.traffic.persistence;

/**
 * 两个字符串数组的结构体
 * @author weiliu
 *
 */
public class InfoStruct {
	
	private String [] leftStrs 	= null;	// 左行数组
	private String [] rightStrs	= null;	// 右行数组
	
	/**
	 * 保存数组信息
	 * @param left
	 * 			是否为左边数组
	 * @param str
	 * 			字符串数组
	 */
	public void put(Boolean left, String[] str) {
		if(left)
			this.leftStrs = str;
		else
			this.rightStrs = str;
	}
	
	/**
	 * 获取数组信息
	 * @param left
	 * 			是否为左边数组
	 * @return 字符串数组
	 */
	public String[] get(Boolean left) {
		if(left)
			return leftStrs;
		else
			return rightStrs;
	}
	
	
}
