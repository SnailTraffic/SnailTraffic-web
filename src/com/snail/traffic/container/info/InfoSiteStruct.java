package com.snail.traffic.container.info;

import net.sf.json.JSONObject;

public class InfoSiteStruct extends InfoStruct {

	public String name;
	public String [] left;
	public String [] right;

	@Override
	public JSONObject toJSONObject() {
		JSONObject ret = new JSONObject();
		
		try {
			if (this.left == null || this.right == null) {
				return null;
			}
			
			ret.put("title", this.name);
			ret.put("description", "");
			ret.put("left", this.left);
			ret.put("right", this.right);
			ret.put("remark", "");
		} catch (Exception e) {
			ret = null;
		}
		return ret;
	}
}
