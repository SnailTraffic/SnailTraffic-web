package com.snail.traffic.container.info;

import net.sf.json.*;

/**
 * �����ַ�������Ľṹ��
 * @author weiliu
 *
 */
public abstract class InfoStruct implements CovertToJsonObject {

	public abstract JSONObject toJSONObject();
}
