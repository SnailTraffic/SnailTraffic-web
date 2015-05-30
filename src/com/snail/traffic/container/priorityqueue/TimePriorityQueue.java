package com.snail.traffic.container.priorityqueue;

import com.snail.traffic.container.transfer.TransitSection;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class TimePriorityQueue {
	
	//Comparator
    public Comparator<TransitSection> timeComparator = new Comparator<TransitSection>() {
        @Override
        public int compare(TransitSection c1, TransitSection c2) {
            return (int)(c1.time - c2.time);
        }
    };

    private Queue<TransitSection> transitPriorityQueue = new PriorityQueue<>(5, timeComparator);
	
    /**
     * insert into PriorityQueue
     * @param t
     * @return
     */
    public boolean add(TransitSection t) {
    	return transitPriorityQueue.add(t);
    }
    
    /**
     * get and delete the front of queue,
     * @return
     */
    public TransitSection poll() {
    	return transitPriorityQueue.poll();
    }
    
    /**
     * return size
     * @return
     */
    public int size() {
    	return transitPriorityQueue.size();
    }
}
