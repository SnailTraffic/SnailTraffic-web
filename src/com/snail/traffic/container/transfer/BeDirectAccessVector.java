package com.snail.traffic.container.transfer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.snail.traffic.container.access.BaseAccessStruct;
import com.snail.traffic.container.access.BeDirectAccessStruct;
import com.snail.traffic.container.priorityqueue.TimePriorityQueue;

public class BeDirectAccessVector extends BaseAccessVector {

	/**
	 * ��ֱ���վ������
	 */
	public BeDirectAccessVector() {
		relateVector = new ArrayList<BeDirectAccessStruct>();
	}

	/**
	 *  �ӱ�ֱ�������л�ȡ����sitename�ķ���
	 */
	public TimePriorityQueue getVectorTo(String name) {
		TimePriorityQueue transitVector = new TimePriorityQueue();
		BeDirectAccessStruct direct = null;
		TransitSection transit = null;
		
		int size = relateVector.size();
		String str = null;
		// ѭ������Ŀ��վ��
		for (int i = 0; i < size; i++) {
			direct = (BeDirectAccessStruct) relateVector.get(i);
			str = direct.siteName;
			// ���ҵ�Ŀ��ʱ
			if (str.equals(name)) {
				transit = new TransitSection();	// ����������һ���µ��ڴ�����
				transit.startSite = name;
				transit.endSite = this.relateSite;
				transit.lineName = direct.lineName;
				transit.isLeft = direct.runLeft;
				transitVector.add(transit);	// ����������
			}
		}
		return transitVector;
	}

	/**
	 * �ѱ�ֱ������ת���ɼ���
	 */
	public Set<String> getSiteSetFromVector() {
		Set<String> siteSet = new HashSet<>();
		BeDirectAccessStruct direct = null;
		
		int size = this.relateVector.size();
		for (int i = 0; i < size; i++) {
			direct = (BeDirectAccessStruct) relateVector.get(i);
			siteSet.add(direct.siteName);
		}
		return siteSet;
	}
}
