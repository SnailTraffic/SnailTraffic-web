package com.snail.traffic.container.transfer;

import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.snail.traffic.container.info.CovertToJsonObject;

/**
 * Struct of Scheme
 * @author weiliu
 */
public class TransitScheme extends BaseTransitStruct implements CovertToJsonObject {
	public Vector<TransitSection> transitSections = new Vector<>();
	public int time = 0;
	public int distance = 0;

	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		JSONArray a = new JSONArray();

		String title = "";
		String t = this.time + "";
		String d = this.distance + "";
		try {
			for (int i = 0; i < transitSections.size(); i++) {
				a.add(transitSections.get(i).toJSONObject());
				if (i > 0) { title += " - "; }
				title += transitSections.get(i).lineName;
			}

			o.put("title", title);
			o.put("sections", a);
			o.put("time", t);
			o.put("distance", d);
		} catch (Exception e){
			e.printStackTrace();
			o = null;
		}
		
		return o;
	}
}
