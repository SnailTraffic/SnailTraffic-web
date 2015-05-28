package com.snail.traffic.control;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.snail.traffic.persistence.BeDirectAccessStruct;
import com.snail.traffic.persistence.DirectAccessStruct;

public class Be_DirectVector extends DirectVectorBase{

	/**
	 * ��ֱ���վ������
	 */
	public Be_DirectVector() {
		relateVector = new Vector<BeDirectAccessStruct>();
	}

	/**
	 *  �ӱ�ֱ�������л�ȡ����sitename�ķ���
	 */
	public TimePriorityQueue getVectorTo(String name) {
		TimePriorityQueue transitVecter = new TimePriorityQueue();
		BeDirectAccessStruct direct = null;
		TransitSToEStruct transit = null;
		
		int size = relateVector.size();
		String str = null;
		// ѭ������Ŀ��վ��
		for (int i = 0; i < size; i++) {
			direct = (BeDirectAccessStruct) relateVector.get(i);
			str = direct.sname;
			// ���ҵ�Ŀ��ʱ
			if (str.equals(name)) {
				transit = new TransitSToEStruct();	// ����������һ���µ��ڴ�����
				transit.startSite = name;
				transit.endSite = this.relateSite;
				transit.lineName = direct.lname;
				transit.isLeft = direct.runLeft;
				transitVecter.add(transit);	// ����������
			}
		}
		return transitVecter;
	}

	/**
	 * �ѱ�ֱ������ת���ɼ���
	 */
	public Set<String> getSiteSetFromVector() {
		Set<String> siteSet = new HashSet<String>(); 
		BeDirectAccessStruct direct = null;
		
		int size = this.relateVector.size();
		for (int i = 0; i < size; i++) {
			direct = (BeDirectAccessStruct) relateVector.get(i);
			siteSet.add(direct.sname);
		}
		return siteSet;
	}

}
