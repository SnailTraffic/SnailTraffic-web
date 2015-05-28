package com.snail.traffic.control;

import java.sql.Connection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

import com.snail.traffic.persistence.SelectOperated;

/**
 * ��ʱ�����ȶ���
 * @author weiliu
 *
 */
public class TotalTimePriorityQueue {
	
	
	//����Comparatorʵ��
    public Comparator<TransitSchemeStruct> timeComparator = new Comparator<TransitSchemeStruct>() {
        @Override
        public int compare(TransitSchemeStruct c1, TransitSchemeStruct c2) {
            return (int) (c1.time - c2.time);
        }
    };
	
    // �������ȶ���
    private Queue<TransitSchemeStruct> transitPriorityQueue = new PriorityQueue<>(5, timeComparator);
	
    /**
     * ��ָ����Ԫ�ز�������ȼ����С�
     * @param t
     * @return
     */
    public boolean add(TransitSchemeStruct t) {    	
    	return transitPriorityQueue.add(t);
    }
    
    /**
     * ��ȡ���Ƴ��˶��е�ͷ������˶���Ϊ�գ��򷵻� null��
     * @return
     */
    public TransitSchemeStruct poll() {
    	return transitPriorityQueue.poll();
    }
    
    /**
     * ���ش� collection �е�Ԫ������
     * @return
     */
    public int size() {
    	return transitPriorityQueue.size();
    }
    
    /**
     * �����ȶ����е�ǰ5�����в�ȫ·����Ϣ
     */
    public Vector<TransitSchemeStruct> getAdditionalRoute(Connection con) {
    	SelectOperated selOper = new SelectOperated(con);	// �������ݿ�
    	
    	Vector<TransitSchemeStruct> v = new Vector<TransitSchemeStruct>();	// ���������
    	TransitSchemeStruct scheme = null;
    	TransitSToEStruct stoe = null;
    	int len = 0;
    	int queueSize = size();
    	
    	for(int i = 0; i < queueSize; i++) {
    		scheme = this.transitPriorityQueue.poll();
    		len = scheme.transitLine.size();
    		for (int j = 0; j < len; j++) {
    			stoe = scheme.transitLine.get(j);
    			stoe.route = selOper.getRoute(stoe.startSite, stoe.endSite, stoe.lineName, stoe.isLeft);
    		}
    		v.add(scheme);
    		if (i == 5)	// ��5������������
    			break;
    	}
    	
    	return v;
    }
}
