package com.snail.traffic.control;

import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.snail.traffic.persistence.CovertToJsonObject;

/**
 * 方案结构体
 * @author weiliu
 *
 */
public class TransitSchemeStruct implements CovertToJsonObject {
	public Vector<TransitSToEStruct> transitLine = new Vector<TransitSToEStruct>();// 换乘线路向量
	public int time = 0;	// 方案所需时间
	public int distance = 0;	// 方案距离
	
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		JSONArray a = new JSONArray();
		
		try {
			for (int i = 0; i < transitLine.size(); i++) {
				a.add(transitLine.get(i).toJSONObject());
			}
	
			o.put("scheme", a);
			o.put("time", time);
			o.put("distance", distance);
		} catch (Exception e){
			e.printStackTrace();
			o = null;
		}
		
		return o;
	}
}
