package com.snail.traffic.control;

import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.snail.traffic.persistence.CovertToJsonObject;

/**
 * �����ṹ��
 * @author weiliu
 *
 */
public class TransitSchemeStruct implements CovertToJsonObject {
	public Vector<TransitSToEStruct> transitLine = new Vector<TransitSToEStruct>();// ������·����
	public int time = 0;	// ��������ʱ��
	public int distance = 0;	// ��������
	
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
