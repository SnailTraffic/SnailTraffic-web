package com.snail.traffic.control;

import java.sql.Connection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

import com.snail.traffic.persistence.SelectOperated;

/**
 * 总时间优先队列
 * @author weiliu
 *
 */
public class TotalTimePriorityQueue {
	
	
	//匿名Comparator实现
    public Comparator<TransitSchemeStruct> timeComparator = new Comparator<TransitSchemeStruct>() {
        @Override
        public int compare(TransitSchemeStruct c1, TransitSchemeStruct c2) {
            return (int) (c1.time - c2.time);
        }
    };
	
    // 公交优先队列
    private Queue<TransitSchemeStruct> transitPriorityQueue = new PriorityQueue<>(5, timeComparator);
	
    /**
     * 将指定的元素插入此优先级队列。
     * @param t
     * @return
     */
    public boolean add(TransitSchemeStruct t) {    	
    	return transitPriorityQueue.add(t);
    }
    
    /**
     * 获取并移除此队列的头，如果此队列为空，则返回 null。
     * @return
     */
    public TransitSchemeStruct poll() {
    	return transitPriorityQueue.poll();
    }
    
    /**
     * 返回此 collection 中的元素数。
     * @return
     */
    public int size() {
    	return transitPriorityQueue.size();
    }
    
    /**
     * 对优先队列中的前5条进行补全路径信息
     */
    public Vector<TransitSchemeStruct> getAdditionalRoute(Connection con) {
    	SelectOperated selOper = new SelectOperated(con);	// 连接数据库
    	
    	Vector<TransitSchemeStruct> v = new Vector<TransitSchemeStruct>();	// 输出的向量
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
    		if (i == 5)	// 有5个方案即结束
    			break;
    	}
    	
    	return v;
    }
}
