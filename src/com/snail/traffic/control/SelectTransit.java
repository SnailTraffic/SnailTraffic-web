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
	
	// 构造函数
	public SelectTransit(Connection con) {
		this.con = con;
		this.selOper = new SelectOperated(con);
	}
	
	/**
	 *  求交集
	 *  把数组一放到链表中
	 *  判断数组二中的元素是否在链表中
	 *  若包含，保存至Set集合中
	 * @param arr1
	 * @param arr2
	 * @return 
	 */
    public String[] intersect(String[] arr1, String[] arr2) {
        List<String> list = new LinkedList<String>();	// 链表
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
	 * 换乘查询
	 * 首先检查是否直达
	 * 检查是否存在一次换乘
	 * @param start
	 * 			起始上车站点
	 * @param end
	 * 			最终下车站点
	 * @return
	 */
	public InfoStruct query(String start, String end) {
		if (start == null || end == null)
			return null;
		
		// 输入处理
		start = start.trim();
		end = end.trim();
		
		// 当起始站点与终止站点重复时
		if (start.equals(end)) 
			return null;
		
		TotalTimePriorityQueue schemes = new TotalTimePriorityQueue();// 方案向量
		
		DirectVectorBase directV = new DirectVector();
		DirectVectorBase be_directV = new Be_DirectVector();
		
		// 设置关系站点名
		directV.relateSite = start;

		// 获得起始站点的所有可直达站点
		directV.relateVector = this.selOper.getDirectAccessSites(start);
		
		TransitStruct ret = new TransitStruct();
		
		// 若方案数大于3个，即可返回换乘查询结果
		if (through(schemes, directV, end) >= 3) {
			ret.schemes = schemes.getAdditionalRoute(con);
		}
		// 若获取直达方案后，方案数小于3个
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
	 * 根据目标站点获取直达方案
	 * @param Scheme
	 * 			方案向量
	 * @param vector
	 * 			起始站点向量
	 * @param end
	 * 			目标站点
	 * @return size
	 * 			方案数
	 */
	private int through(TotalTimePriorityQueue allScheme, DirectVectorBase direct, String end) {
		TimePriorityQueue transitVecter = direct.getVectorTo(end); // 获得直达方案
		TransitSchemeStruct newScheme = null;	// 换乘方案元素
		TransitSToEStruct stoe = null;
		
		int size = transitVecter.size();
		// 一个直达乘车方案就是一个方案
		for (int i = 0; i < size; i++) {
			newScheme = new TransitSchemeStruct();
			stoe = transitVecter.poll();	// 获得优先队列中的时间最优元素
			newScheme.transitLine.add(stoe);
			newScheme.time = stoe.time;
			newScheme.distance = stoe.distance;
			allScheme.add(newScheme); // 把元素加入到向量中
		}
		System.out.println("直达：" + allScheme.size());
		return allScheme.size();
	}
	

	/**
	 * 对两个集合求交集(准确)
	 * @param setA
	 * 			A集合
	 * @param setB
	 * 			B集合
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
	 * 一次换乘查询
	 * @param allScheme
	 * 			所有方案
	 * @param direct
	 * 			起始站点直达站点
	 * @param be_direct
	 * 			终止站点被直达站点
	 * @param interset
	 * 			交集（可换乘站点）
	 * @return 当前方案数
	 */
	private int onceTransfer(TotalTimePriorityQueue allScheme
							, DirectVectorBase direct
							, DirectVectorBase be_direct
							, Set<String> interset) {
		
		TimePriorityQueue frontTransit 	= null;	// 前半段公交的临时变量
		TimePriorityQueue lastTransit 	= null;	// 后半段公交的临时变量
		TransitSchemeStruct newScheme 	= null;	// 新的方案
		TransitSToEStruct frontstoe 	= null;	// 前段乘车段
		TransitSToEStruct laststoe 		= null;	// 后段乘车段
		
		for (String s: interset) {
			System.out.println(s);
			
			frontTransit = direct.getVectorTo(s);
			lastTransit = be_direct.getVectorTo(s);
			frontstoe = frontTransit.poll();
			laststoe = lastTransit.poll();
			
			System.out.println(frontstoe.lineName);
			System.out.println(laststoe.lineName);
			
			// 构造新元素
			newScheme = new TransitSchemeStruct();
			newScheme.transitLine.add(frontstoe);
			newScheme.transitLine.add(laststoe);
			newScheme.time = frontstoe.time + laststoe.time;
			newScheme.distance = frontstoe.distance + laststoe.distance;
			
			
			//System.out.println(newScheme.transitLine.get(0).lineName + "," + newScheme.transitLine.get(1).lineName);
			
			allScheme.add(newScheme);
		}
		System.out.println("直达+一次换乘" + allScheme.size());
		return allScheme.size();
	}
	
	
	/*
	
	public static void main(String[] args) {
		OracleBase oracle = new OracleBase();
		Connection con = oracle.getConnection();
		SelectTransit a = new SelectTransit(con);
		Vector<TransitSchemeStruct> v = a.query("建设大道双墩", "汉黄路岱家山");
		TransitSchemeStruct tt = null;
		TransitSToEStruct rr = null;
		
	}
	
    */
}
