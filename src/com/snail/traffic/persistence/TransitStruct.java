package com.snail.traffic.persistence;

import java.util.Vector;

import com.snail.traffic.control.TransitSchemeStruct;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TransitStruct extends InfoStruct {
	public Vector<TransitSchemeStruct> schemes;
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		JSONArray a = new JSONArray();
		try {
			for (int i = 0; i < schemes.size(); i++) {
				a.add(schemes.get(i).toJSONObject());
			}
			
			o.put("schemes", a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return o;
	}

	@Override
	public void put(Boolean left, String[] str) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] get(Boolean left) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

}
