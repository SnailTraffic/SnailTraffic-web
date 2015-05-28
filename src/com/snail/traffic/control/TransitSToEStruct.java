package com.snail.traffic.control;

import java.util.Vector;

import com.snail.traffic.persistence.CovertToJsonObject;

import net.sf.json.JSONObject;

/**
 * ��ͨ���ϳ�վ�㵽�³�վ��
 * @author weiliu
 *
 */
public class TransitSToEStruct implements CovertToJsonObject {
	public String startSite = null;	// �ϳ�վ����	
	public String endSite	= null;	// �³�վ����
	public Vector<String> route = new Vector<String>();	// ·��
	public int isLeft		= 0;	// �Ƿ�����
	public String lineName	= null;	// ��·��
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
