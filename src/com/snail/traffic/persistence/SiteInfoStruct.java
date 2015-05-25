package com.snail.traffic.persistence;

import net.sf.json.JSONObject;

public class SiteInfoStruct extends InfoStruct {
	private String stationName 	= null;	// վ����
	private String [] leftStrs 	= null;	// ��������
	private String [] rightStrs	= null;	// ��������
	
	public void setName(String name) {
		this.stationName = name;
	}
	/**
	 * ����������Ϣ
	 * @param left
	 * 			�Ƿ�Ϊ�������
	 * @param str
	 * 			�ַ�������
	 */
	public void put(Boolean left, String[] str) {
		if(left)
			this.leftStrs = str;
		else
			this.rightStrs = str;
	}	
	
	/**
	 * ��ȡ������Ϣ
	 * @param left
	 * 			�Ƿ�Ϊ�������
	 * @return �ַ�������
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
