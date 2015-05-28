package com.snail.traffic.persistence;

import net.sf.json.*;

/**
 * �����ַ�������Ľṹ��
 * @author weiliu
 *
 */
public abstract class InfoStruct implements CovertToJsonObject {

	public abstract JSONObject toJSONObject();
	
	/**
	 * ����������Ϣ
	 * @param left
	 * 			�Ƿ�Ϊ�������
	 * @param str
	 * 			�ַ�������
	 */
	public abstract void put(Boolean left, String[] str);
	
	/**
	 * ��ȡ������Ϣ
	 * @param left
	 * 			�Ƿ�Ϊ�������
	 * @return �ַ�������
	 */
	public abstract String[] get(Boolean left);
	
	public abstract void setName(String name);
	
}
