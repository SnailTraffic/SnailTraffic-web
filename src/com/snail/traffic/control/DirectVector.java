package com.snail.traffic.control;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.snail.traffic.persistence.DirectAccessStruct;

public class DirectVector extends DirectVectorBase{
	
	/**
	 * ��ֱ���վ������
	 */
	public DirectVector() {
		relateVector = new Vector<DirectAccessStruct>();
	}
		
	/**
	 *  ��ֱ�������л�ȡ����sitename�ķ���
	 */
	public TimePriorityQueue getVectorTo(String name) {	
		TimePriorityQueue transitVecter = new TimePriorityQueue();
		DirectAccessStruct direct = null;
		TransitSToEStruct transit = null;
		
		int size = relateVector.size();
		String str = null;
		// ѭ������Ŀ��վ��
		for (int i = 0; i < size; i++) {
			direct = (DirectAccessStruct) relateVector.get(i);
			str = direct.sname;
			// ���ҵ�Ŀ��ʱ
			if (str.equals(name)) {
				transit = new TransitSToEStruct();	// ����������һ���µ��ڴ�����
				transit.startSite = this.relateSite;
				transit.endSite = name;
				transit.lineName = direct.lname;
				transit.isLeft = direct.runLeft;
				transit.time = direct.runTime;
				transit.distance = direct.distance;	
				transitVecter.add(transit);	// ����������
			}
		}
		return transitVecter;
	}

	/**
	 * ��ֱ������ת���ɼ���
	 */
	public Set<String> getSiteSetFromVector() {
		Set<String> siteSet = new HashSet<String>(); 
		DirectAccessStruct direct = null;
		
		int size = this.relateVector.size();
		for (int i = 0; i < size; i++) {
			direct = (DirectAccessStruct) relateVector.get(i);
			siteSet.add(direct.sname);
		}
		return siteSet;
	}

}
