package com.snail.traffic.control;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class TimePriorityQueue {
	
	//匿名Comparator实现
    public Comparator<TransitSToEStruct> timeComparator = new Comparator<TransitSToEStruct>() {
        @Override
        public int compare(TransitSToEStruct c1, TransitSToEStruct c2) {
            return (int) (c1.time - c2.time);
        }
    };
	
    // 公交优先队列
    private Queue<TransitSToEStruct> transitPriorityQueue = new PriorityQueue<>(5, timeComparator);
	
    /**
     * 将指定的元素插入此优先级队列。
     * @param t
     * @return
     */
    public boolean add(TransitSToEStruct t) {    	
    	return transitPriorityQueue.add(t);
    }
    
    /**
     * 获取并移除此队列的头，如果此队列为空，则返回 null。
     * @return
     */
    public TransitSToEStruct poll() {
    	return transitPriorityQueue.poll();
    }
    
    /**
     * 返回此 collection 中的元素数。
     * @return
     */
    public int size() {
    	return transitPriorityQueue.size();
    }
}
