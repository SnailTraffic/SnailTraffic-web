 package com.snail.traffic.control;

import com.snail.traffic.persistence.InfoStruct;


abstract class SelectBase {
	
	/**
	 * ��ѯվ����Ϣ����
	 * 
	 * @param input
	 * 			�����ַ���
	 * @return
	 */
	public abstract InfoStruct query(String input);
	
}
