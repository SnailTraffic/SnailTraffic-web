package com.snail.traffic.container.transfer;

import com.snail.traffic.container.priorityqueue.TimePriorityQueue;

import java.util.Set;
import java.util.Vector;

public abstract class BaseAccessVector {
	
	public String relateSite = null;// ��ϵվ��

	public Vector<?> relateVector;	// ����
	
	/**
	 *  �������л�ȡ����sitename�ķ���
	 */
	public abstract TimePriorityQueue getVectorTo(String name);
	
	/**
	 * ������ת���ɼ���
	 */
	public abstract Set<String> getSiteSetFromVector();	// ������ת���ɼ���
	
}