package com.snail.traffic.persistence;

import net.sf.json.JSONObject;

/**
 * 线路的所有信息结构体
 * @author weiliu
 *
 */
public class LineAllInfoStruct extends InfoStruct{
	
	private String [] leftStrs 	= null;	// 左行数组
	private String [] rightStrs	= null;	// 右行数组
	public String lineName		= null;	// 线路名
	public String lineRange		= null; // 线路区间
	public String firstOpen		= null; // 首站开班
	public String lastOpen		= null; // 末站开班
	public String firstClose	= null; // 首站收班
	public String lastClose 	= null;	// 末站收班
	public String price 		= null;	// 票价
	public String cardPrice		= null; // 刷卡票价
	public String company		= null; // 所属公司
	public String remark		= null;	// 备注
	
	
	/**
	 * 保存数组信息
	 * @param left
	 * 			是否为左边数组
	 * @param str
	 * 			字符串数组
	 */
	public void put(Boolean left, String[] str) {
		if(left)
			this.leftStrs = str;
		else
			this.rightStrs = str;
	}	
	
	/**
	 * 获取数组信息
	 * @param left
	 * 			是否为左边数组
	 * @return 字符串数组
	 */
	public String[] get(Boolean left) {
		if(left)
			return leftStrs;
		else
			return rightStrs;
	}
	
	/**
	 * 设置线路区间
	 * @param lineRange
	 */
	public void setLine(String linename, String lineRange) {
		this.lineName = linename;
		this.lineRange = lineRange;
	}
	
	/**
	 * 设置线路时间
	 * @param firstOpen
	 * @param lastOpen
	 * @param firstClose
	 * @param lastClose
	 */
	public void setTime(String firstOpen
						, String lastOpen
						, String firstClose
						, String lastClose) {
		this.firstOpen 	= firstOpen;
		this.lastOpen 	= lastOpen;
		this.firstClose = firstClose;
		this.lastClose 	= lastClose;
	}
	
	/**
	 * 设置价格
	 * @param price
	 * @param cardPrice
	 */
	public void setPrice(String price, String cardPrice) {
		this.price		= price;
		this.cardPrice 	= cardPrice;	
	}
	
	/**
	 * 设置其他信息
	 * @param company
	 * @param remark
	 */
	public void setOther(String company, String remark) {
		this.company = company;
		this.remark = remark;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		StringBuilder sBuilder = new StringBuilder();
		
		// Format to description
		sBuilder
			.append(leftStrs[0])
			.append(" ")
			.append(firstOpen)
			.append("-")
			.append(firstClose);
		
		sBuilder.append("\n")
			.append(rightStrs[0])
			.append(" ")
			.append(lastOpen)
			.append("-")
			.append(lastClose);
		
		sBuilder.append("\n")
			.append("票价 ")
			.append(price)
			.append(" | 刷卡 ")
			.append(cardPrice);
		
		sBuilder.append("\n")
			.append(company);
		
		ret.put("title", lineName);
		ret.put("description", sBuilder.toString());
		ret.put("left", leftStrs);
		ret.put("right", rightStrs);
		ret.put("remark", remark);
		
		return ret;
	}
}
