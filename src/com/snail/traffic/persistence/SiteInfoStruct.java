package com.snail.traffic.persistence;

import net.sf.json.JSONObject;

public class SiteInfoStruct extends InfoStruct {
	private String stationName 	= null;	// 站点名
	private String [] leftStrs 	= null;	// 左行数组
	private String [] rightStrs	= null;	// 右行数组
	
	public void setName(String name) {
		this.stationName = name;
	}
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

	@Override
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		
		try {
			if (leftStrs == null || rightStrs == null) {
				return null;
			}
			
			ret.put("title", stationName);
			ret.put("description", "");
			ret.put("left", leftStrs);
			ret.put("right", rightStrs);
			ret.put("remark", "");
		} catch (Exception e) {
			ret = null;
		}
		
		return ret;
	}
}
