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
	public String time = null;
	public String distance = null;

	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		JSONArray a = new JSONArray();

		time = "";
		distance = "";

		String title = "";

		try {
			for (int i = 0; i < transitSections.size(); i++) {
				a.add(transitSections.get(i).toJSONObject());

				if (i > 0) { title += " - "; }
				title += transitSections.get(i).lineNumber;
			}

			o.put("title", title);
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
