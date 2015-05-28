package com.snail.traffic.control;

import java.util.Vector;

import com.snail.traffic.persistence.CovertToJsonObject;

import net.sf.json.JSONObject;

/**
 * 交通的上车站点到下车站点
 * @author weiliu
 *
 */
public class TransitSToEStruct implements CovertToJsonObject {
	public String startSite = null;	// 上车站点名	
	public String endSite	= null;	// 下车站点名
	public Vector<String> route = new Vector<String>();	// 路径
	public int isLeft		= 0;	// 是否左线
	public String lineName	= null;	// 线路名
	public int time 		= 0;
	public int distance 	= 0;
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		
		try {
			o.put("name", lineName);
			o.put("start", startSite);
			o.put("end", endSite);
			o.put("time", time);
			o.put("distance", distance);
			o.put("route", route);
		} catch (Exception e) {
			e.printStackTrace();
			o = null;
		}
		
		return o;	
	}
}
