package com.snail.traffic.container.transfer;

import java.util.Vector;

import com.snail.traffic.container.info.CovertToJsonObject;
import net.sf.json.JSONObject;

public class TransitSection extends BaseTransitStruct implements CovertToJsonObject {

	public String startSite = null;
	public String endSite	= null;
	public String lineName	= null;
	public int isLeft = 0;
	public Vector<String> route = new Vector<>();// route

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
