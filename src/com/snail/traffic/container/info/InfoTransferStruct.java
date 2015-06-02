package com.snail.traffic.container.info;

import java.util.Vector;

import com.snail.traffic.container.transfer.TransitScheme;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class InfoTransferStruct extends InfoStruct {
	public Vector<TransitScheme> schemes;
	public String start;
	public String end;

	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		JSONArray a = new JSONArray();
		try {
			for (int i = 0; i < schemes.size(); i++) {
				a.add(schemes.get(i).toJSONObject());
			}

			o.put("start", start);
			o.put("end", end);
			o.put("schemes", a);
		} catch (Exception e) {
			e.printStackTrace();
			o = null;
		}
		
		return o;
	}


}
