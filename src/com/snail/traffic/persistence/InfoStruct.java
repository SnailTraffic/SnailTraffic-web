package com.snail.traffic.persistence;

/**
 * �����ַ�������Ľṹ��
 * @author weiliu
 *
 */
public class InfoStruct {
	
	private String [] leftStrs 	= null;	// ��������
	private String [] rightStrs	= null;	// ��������
	
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
	
	
}
