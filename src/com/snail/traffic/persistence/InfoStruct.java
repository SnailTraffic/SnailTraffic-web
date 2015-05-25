package com.snail.traffic.persistence;

import net.sf.json.*;

/**
 * �����ַ�������Ľṹ��
 * @author weiliu
 *
 */
public abstract class InfoStruct implements CovertToJson {

	public abstract JSONObject toJSON();
	
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
