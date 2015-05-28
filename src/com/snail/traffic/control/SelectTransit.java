package com.snail.traffic.control;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.snail.traffic.persistence.DirectAccessStruct;
import com.snail.traffic.persistence.InfoStruct;
import com.snail.traffic.persistence.OracleBase;
import com.snail.traffic.persistence.SelectOperated;
import com.snail.traffic.persistence.TransitStruct;

public class SelectTransit {
	private SelectOperated selOper = null;
	private Connection con = null;
	
	// ���캯��
	public SelectTransit(Connection con) {
		this.con = con;
		this.selOper = new SelectOperated(con);
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
    public String[] intersect(String[] arr1, String[] arr2) {
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
		
		// ���봦��
		start = start.trim();
		end = end.trim();
		
		// ����ʼվ������ֹվ���ظ�ʱ
		if (start.equals(end)) 
			return null;
		
		TotalTimePriorityQueue schemes = new TotalTimePriorityQueue();// ��������
		
		DirectVectorBase directV = new DirectVector();
		DirectVectorBase be_directV = new Be_DirectVector();
		
		// ���ù�ϵվ����
		directV.relateSite = start;

		// �����ʼվ������п�ֱ��վ��
		directV.relateVector = this.selOper.getDirectAccessSites(start);
		
		TransitStruct ret = new TransitStruct();
		
		// ������������3�������ɷ��ػ��˲�ѯ���
		if (through(schemes, directV, end) >= 3) {
			ret.schemes = schemes.getAdditionalRoute(con);
		}
		// ����ȡֱ�﷽���󣬷�����С��3��
		else {
			int len = directV.relateVector.size();
			be_directV.relateSite = end;
			be_directV.relateVector = this.selOper.get_Be_DirectAccessSites(end, len);
			Set<String> setIntersection = intersection(directV.getSiteSetFromVector(), be_directV.getSiteSetFromVector());
			
			if (onceTransfer(schemes, directV, be_directV, setIntersection) >= 3)
				ret.schemes = schemes.getAdditionalRoute(con);
			else 
				ret.schemes = schemes.getAdditionalRoute(con);
		}
		
		return (InfoStruct)ret;
	}
	
	/**
	 * ����Ŀ��վ���ȡֱ�﷽��
	 * @param Scheme
	 * 			��������
	 * @param vector
	 * 			��ʼվ������
	 * @param end
	 * 			Ŀ��վ��
	 * @return size
	 * 			������
	 */
	private int through(TotalTimePriorityQueue allScheme, DirectVectorBase direct, String end) {
		TimePriorityQueue transitVecter = direct.getVectorTo(end); // ���ֱ�﷽��
		TransitSchemeStruct newScheme = null;	// ���˷���Ԫ��
		TransitSToEStruct stoe = null;
		
		int size = transitVecter.size();
		// һ��ֱ��˳���������һ������
		for (int i = 0; i < size; i++) {
			newScheme = new TransitSchemeStruct();
			stoe = transitVecter.poll();	// ������ȶ����е�ʱ������Ԫ��
			newScheme.transitLine.add(stoe);
			newScheme.time = stoe.time;
			newScheme.distance = stoe.distance;
			allScheme.add(newScheme); // ��Ԫ�ؼ��뵽������
		}
		System.out.println("ֱ�" + allScheme.size());
		return allScheme.size();
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
		Set<String> setIntersection = new HashSet<String>();
        String s = "";
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
							, DirectVectorBase direct
							, DirectVectorBase be_direct
							, Set<String> interset) {
		
		TimePriorityQueue frontTransit 	= null;	// ǰ��ι�������ʱ����
		TimePriorityQueue lastTransit 	= null;	// ���ι�������ʱ����
		TransitSchemeStruct newScheme 	= null;	// �µķ���
		TransitSToEStruct frontstoe 	= null;	// ǰ�γ˳���
		TransitSToEStruct laststoe 		= null;	// ��γ˳���
		
		for (String s: interset) {
			System.out.println(s);
			
			frontTransit = direct.getVectorTo(s);
			lastTransit = be_direct.getVectorTo(s);
			frontstoe = frontTransit.poll();
			laststoe = lastTransit.poll();
			
			System.out.println(frontstoe.lineName);
			System.out.println(laststoe.lineName);
			
			// ������Ԫ��
			newScheme = new TransitSchemeStruct();
			newScheme.transitLine.add(frontstoe);
			newScheme.transitLine.add(laststoe);
			newScheme.time = frontstoe.time + laststoe.time;
			newScheme.distance = frontstoe.distance + laststoe.distance;
			
			
			//System.out.println(newScheme.transitLine.get(0).lineName + "," + newScheme.transitLine.get(1).lineName);
			
			allScheme.add(newScheme);
		}
		System.out.println("ֱ��+һ�λ���" + allScheme.size());
		return allScheme.size();
	}
	
	
	/*
	
	public static void main(String[] args) {
		OracleBase oracle = new OracleBase();
		Connection con = oracle.getConnection();
		SelectTransit a = new SelectTransit(con);
		Vector<TransitSchemeStruct> v = a.query("������˫��", "����·᷼�ɽ");
		TransitSchemeStruct tt = null;
		TransitSToEStruct rr = null;
		
	}
	
    */
}
