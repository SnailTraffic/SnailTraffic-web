package com.snail.traffic.persistence;

public class TwoLongStruct {
	
	private String leftStr = null;	// ����ַ�
	private String rightStr = null;	// �ұ��ַ�
	
	/**
	 * �����ַ���Ϣ
	 * @param left
	 * 			�Ƿ�Ϊ���
	 * @param str
	 * 			�ַ���
	 */
	public void put(Boolean left, String str) {
		if(left)
			leftStr = str;
		else
			rightStr = str;
	}
	
	/**
	 * ��ȡ������Ϣ
	 * @param left
	 * 			�Ƿ�Ϊ���
	 * @return �ַ���
	 */
	public String get(Boolean left) {
		if(left)
			return leftStr;
		else
			return rightStr;
	}
	
	
}
