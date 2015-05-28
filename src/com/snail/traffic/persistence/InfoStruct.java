package com.snail.traffic.persistence;

import net.sf.json.*;

/**
 * 两个字符串数组的结构体
 * @author weiliu
 *
 */
public abstract class InfoStruct implements CovertToJsonObject {

	public abstract JSONObject toJSONObject();
	
	/**
	 * 保存数组信息
	 * @param left
	 * 			是否为左边数组
	 * @param str
	 * 			字符串数组
	 */
	public abstract void put(Boolean left, String[] str);
	
	/**
	 * 获取数组信息
	 * @param left
	 * 			是否为左边数组
	 * @return 字符串数组
	 */
	public abstract String[] get(Boolean left);
	
	public abstract void setName(String name);
	
}
