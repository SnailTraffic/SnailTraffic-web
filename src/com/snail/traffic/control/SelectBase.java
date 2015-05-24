 package com.snail.traffic.control;

import com.snail.traffic.persistence.InfoStruct;


abstract class SelectBase {
	
	/**
	 * 查询站点信息函数
	 * 
	 * @param input
	 * 			输入字符串
	 * @return
	 */
	public abstract InfoStruct query(String input);
	
}
