package com.snail.traffic.control.query;

import java.sql.Connection;
import java.util.*;

import com.snail.traffic.container.info.InfoStruct;
import com.snail.traffic.container.priorityqueue.TimePriorityQueue;
import com.snail.traffic.container.priorityqueue.TotalTimePriorityQueue;
import com.snail.traffic.container.transfer.*;
import com.snail.traffic.persistence.select.*;
import com.snail.traffic.container.info.InfoTransferStruct;

public class SelectTransfer {
	Connection con = null;
	SelectAccessProcedure selAccess = null;

	// ���캯��
	public SelectTransfer(Connection con) {
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
	public InfoStruct query(String start, String end) {
		if (start == null || end == null)
			return null;

		// ����ʼվ������ֹվ���ظ�ʱ
		if (start.equals(end)) 
			return null;
		
		TotalTimePriorityQueue allSchemes = new TotalTimePriorityQueue();// ���շ������ȶ���
		selAccess = new SelectDirectAccessProcedure(con);

		BaseAccessVector directV = new DirectAccessVector();	// ֱ��վ�㼯��
		directV.relateSite = start;
		directV.relateVector = selAccess.getAccessSites(start);

		InfoTransferStruct ret = new InfoTransferStruct();
		ret.start = start;
		ret.end = end;

		// ������������3��
		if (through(allSchemes, directV, end) >= 3) {
			System.out.println("��ֱ����·������");
		}
		// ����ȡֱ�﷽���󣬷�����С��3��
		else {
			BaseAccessVector be_directV = new BeDirectAccessVector();
			selAccess = new SelectBeDirectAccessProcedure(con);
			be_directV.relateSite = end;
			be_directV.relateVector = selAccess.getAccessSites(end);
			Set<String> setIntersection = intersection(directV.getSiteSetFromVector(), be_directV.getSiteSetFromVector());
			
			if (onceTransfer(allSchemes, directV, be_directV, setIntersection) >= 3)
				System.out.println("ֱ�����һ�λ��˵���·������");
			else
				System.out.println("��չ���λ�������");
		}
		ret.schemes = getPriorityRoute(allSchemes);
		return ret;
	}
	
	/**
	 * ����Ŀ��վ���ȡֱ�﷽��
	 * һ��ֱ��˳���������һ������
	 * @param Scheme
	 * 			��������
	 * @param vector
	 * 			��ʼվ������
	 * @param end
	 * 			Ŀ��վ��
	 * @return size
	 * 			������
	 */
	private int through(TotalTimePriorityQueue allScheme, BaseAccessVector direct, String end) {
		TimePriorityQueue transitVector = direct.getVectorTo(end); // �����ֱ���յ��������·
		TransitScheme newScheme = null;
		TransitSection section = null;
		int size = transitVector.size();

		for (int i = 0; i < size; i++) {
			newScheme = new TransitScheme();
			section = transitVector.poll();	// ������ȶ����е�ʱ������Ԫ��
			newScheme.transitSections.add(section);
			newScheme.time = section.time;
			newScheme.distance = section.distance;
			allScheme.add(newScheme);
		}
		// System.out.println("ֱ�" + allScheme.size());
		return allScheme.size();
	}

	/**
	 * get 5 Schemes from  Priority queue
	 * @param allSchemes
	 * @return
	 */
	private Vector<TransitScheme> getPriorityRoute(TotalTimePriorityQueue allSchemes) {
		Vector<TransitScheme> v = new Vector<>();
		SelectRouteProcedure selRoute = new SelectRouteProcedure(this.con);
		TransitScheme scheme = null;
		TransitSection section = null;
		int sectionSize = 0;
		int queueSize = allSchemes.size();

		for (int i = 0; i < queueSize; i++) {
			scheme = allSchemes.poll();
			sectionSize = scheme.transitSections.size();
			// get route in the section
			for (int j = 0; j < sectionSize; j++) {
				section = scheme.transitSections.get(j);
				section.route = selRoute.getRoute(section.startSite, section.endSite, section.lineName, section.isLeft);
			}
			v.add(scheme);
			if (i == 4)    // ��5������������
				break;
		}
		return v;
	}

	/**
	 * �����������󽻼�(׼ȷ)
	 * @param setA
	 * 			A����
	 * @param setB
	 * 			B����
	 * @return
	 */
	private Set<String> intersection(Set<String> setA, Set<String> setB) {
		Set<String> setIntersection = new HashSet<>();
		String s = null;
		Iterator<String> iterA = setA.iterator();
		while (iterA.hasNext()) {
			s = iterA.next();
			if(setB.contains(s))
				setIntersection.add(s);
		}
		return setIntersection;
	}

	/**
	 * һ�λ��˲�ѯ
	 * @param allScheme
	 * 			���з���
	 * @param direct
	 * 			��ʼվ��ֱ��վ��
	 * @param be_direct
	 * 			��ֹվ�㱻ֱ��վ��
	 * @param interset
	 * 			�������ɻ���վ�㣩
	 * @return ��ǰ������
	 */
	private int onceTransfer(TotalTimePriorityQueue allScheme
							, BaseAccessVector direct
							, BaseAccessVector be_direct
							, Set<String> interSet) {
		TransitScheme newScheme 				= null;	// �µķ���
		TimePriorityQueue firstSectionQueue 	= null;	// ��һ�ι��������п�ѡ�������ȶ���
		TimePriorityQueue secondSectionQueue 	= null;	// �ڶ��ι��������п�ѡ�������ȶ���
		TransitSection firstSection 			= null;	// ��һ�ι���
		TransitSection secondSection 			= null;	// �ڶ��ι���

		// ѭ���ɻ���վ��
		for (String s: interSet) {
			System.out.println(s);

			firstSectionQueue = direct.getVectorTo(s);
			secondSectionQueue = be_direct.getVectorTo(s);
			firstSection = firstSectionQueue.poll();
			secondSection = secondSectionQueue.poll();
			
			System.out.println(firstSection.lineName);
			System.out.println(secondSection.lineName);
			
			// �����·���Ԫ��
			newScheme = new TransitScheme();
			newScheme.transitSections.add(firstSection);
			newScheme.transitSections.add(secondSection);
			newScheme.time = firstSection.time + secondSection.time;
			newScheme.distance = firstSection.distance + secondSection.distance;
			allScheme.add(newScheme);
		}
		System.out.println("ֱ��+һ�λ���" + allScheme.size());
		return allScheme.size();
	}

	/*
	
	public static void main(String[] args) {
		OracleBase oracle = new OracleBase();
		Connection con = oracle.getConnection();
		SelectTransfer a = new SelectTransfer(con);
		Vector<TransitScheme> v = a.query("������˫��", "����·᷼�ɽ");
		TransitScheme tt = null;
		TransitSection rr = null;
		
	}
	
    */
}
