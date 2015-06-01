package com.snail.traffic.container.transfer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.snail.traffic.container.access.DirectAccessStruct;
import com.snail.traffic.container.priorityqueue.TimePriorityQueue;

public class DirectAccessVector extends BaseAccessVector {
	
	/**
	 * ��ֱ���վ������
	 */
	public DirectAccessVector() {
		relateVector = new ArrayList<DirectAccessStruct>();
	}
		
	/**
	 *  ��ֱ�������л�ȡ����sitename�ķ���
	 */
	public TimePriorityQueue getVectorTo(String name) {
		TimePriorityQueue transitVecter = new TimePriorityQueue();
		DirectAccessStruct direct = null;
		TransitSection transit = null;
		
		int size = relateVector.size();
		String str = null;
		// ѭ������Ŀ��վ��
		for (int i = 0; i < size; i++) {
			direct = (DirectAccessStruct) relateVector.get(i);
			str = direct.siteName;
			// ���ҵ�Ŀ��ʱ
			if (str.equals(name)) {
				transit = new TransitSection();	// ����������һ���µ��ڴ�����
				transit.startSite = this.relateSite;
				transit.endSite = name;
				transit.lineName = direct.lineName;
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
			siteSet.add(direct.siteName);
		}
		return siteSet;
	}

}
