package com.snail.traffic.container.info;

import net.sf.json.*;

/**
 * 两个字符串数组的结构体
 * @author weiliu
 *
 */
public abstract class InfoStruct implements CovertToJsonObject {

	public abstract JSONObject toJSONObject();
}
