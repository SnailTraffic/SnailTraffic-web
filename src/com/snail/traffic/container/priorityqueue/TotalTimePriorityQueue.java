package com.snail.traffic.container.priorityqueue;

import java.sql.Connection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

import com.snail.traffic.container.transfer.TransitScheme;
import com.snail.traffic.container.transfer.TransitSection;

public class TotalTimePriorityQueue {

	// Comparator
    public Comparator<TransitScheme> timeComparator = new Comparator<TransitScheme>() {
        @Override
        public int compare(TransitScheme c1, TransitScheme c2) {
            return (int) (c1.time - c2.time);
        }
    };

    private Queue<TransitScheme> transitPriorityQueue = new PriorityQueue<>(5, timeComparator);
	
    /**
     * insert into PriorityQueue¡£
     * @param t
     * @return
     */
    public boolean add(TransitScheme t) {
    	return transitPriorityQueue.add(t);
    }
    
    /**
     * get and delete the front of queue,
     * @return
     */
    public TransitScheme poll() {
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
