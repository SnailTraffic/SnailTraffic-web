package com.snail.traffic.container.data;

public class TwoStringStruct {
	
	private String leftStr = null;
	private String rightStr = null;
	
	/**
	 * save string
	 * @param left
	 * @param str
	 */
	public void put(Boolean left, String str) {
		if(left)
			this.leftStr = str;
		else
			this.rightStr = str;
	}
	
	/**
	 * get String
	 * @param left
	 * @return string
	 */
	public String get(Boolean left) {
		if(left)
			return this.leftStr;
		else
			return this.rightStr;
	}
}
