package com.snail.traffic.control;

import java.sql.Connection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class SelectTransit {
	private Connection con = null;
	
	// ���캯��
	public SelectTransit(Connection con) {
		this.con = con;
	}
	
	/**
	 * ���˲�ѯ
	 * ���ȼ���Ƿ�ֱ��
	 * ����Ƿ����һ�λ���
	 * @param start
	 * 			��ʼ�ϳ�վ��
	 * @param end
	 * 			�����³�վ��
	 * @return
	 */
	public Vector<TransitSchemeStruct> query(String start, String end) {
		Vector<TransitSchemeStruct> schemes = new Vector<TransitSchemeStruct>();// ��������
		
		
		
		return schemes;
	}
	
	
	
	
	
	
	/**
	 *  �󽻼�
	 *  ������һ�ŵ�������
	 *  �ж�������е�Ԫ���Ƿ���������
	 *  ��������������Set������
	 * @param arr1
	 * @param arr2
	 * @return 
	 */
    public static String[] intersect(String[] arr1, String[] arr2) {
        List<String> list = new LinkedList<String>();	// ����
        Set<String> common = new HashSet<String>(); 
        
        for (String str:arr1)
            if (!list.contains(str))
            	list.add(str);

        for (String str:arr2)
            if (list.contains(str))
                common.add(str);

        String[] result = {};
        return common.toArray(result);
    }
}
