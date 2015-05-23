package com.snail.traffic.persistence;

public class TwoLongStruct {
	
	private String leftStr = null;	// 左边字符
	private String rightStr = null;	// 右边字符
	
	/**
	 * 保存字符信息
	 * @param left
	 * 			是否为左边
	 * @param str
	 * 			字符串
	 */
	public void put(Boolean left, String str) {
		if(left)
			leftStr = str;
		else
			rightStr = str;
	}
	
	/**
	 * 获取数组信息
	 * @param left
	 * 			是否为左边
	 * @return 字符串
	 */
	public String get(Boolean left) {
		if(left)
			return leftStr;
		else
			return rightStr;
	}
	
	
}
