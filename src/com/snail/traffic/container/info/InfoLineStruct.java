package com.snail.traffic.container.info;

import net.sf.json.JSONObject;


public class InfoLineStruct extends InfoStruct{

	public String name;
	public String [] left;
	public String [] right;

	public String lineRange		= null;
	public String firstOpen		= null;
	public String lastOpen		= null;
	public String firstClose	= null;
	public String lastClose 	= null;
	public String price 		= null;
	public String cardPrice		= null;
	public String company		= null;
	public String remark		= null;

	/**
	 * set line range
	 * @param lineRange
	 */
	public void setLineRange(String lineRange) {
		this.lineRange = lineRange;
	}

	/**
	 * set time
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
	 * set price
	 * @param price
	 * @param cardPrice
	 */
	public void setPrice(String price, String cardPrice) {
		this.price		= price;
		this.cardPrice 	= cardPrice;	
	}
	
	/**
	 * set other information
	 * @param company
	 * @param remark
	 */
	public void setOther(String company, String remark) {
		this.company = company;
		this.remark = remark;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject ret = new JSONObject();
		StringBuilder sBuilder = new StringBuilder();
		
		try {
			if (this.left == null || this.right == null) {
				return null;
			}
			
			// Format to description
			sBuilder
				.append(this.left[0])
				.append(" ")
				.append(firstOpen)
				.append("-")
				.append(firstClose);
			
			sBuilder.append("\n")
				.append(this.right[0])
				.append(" ")
				.append(lastOpen)
				.append("-")
				.append(lastClose);
			
			sBuilder.append("\n")
				.append("Æ±¼Û ")
				.append(price)
				.append(" | Ë¢¿¨ ")
				.append(cardPrice);
			
			sBuilder.append("\n")
				.append(company);
			
			ret.put("title", this.name);
			ret.put("description", sBuilder.toString());
			ret.put("left", this.left);
			ret.put("right", this.right);
			ret.put("remark", this.remark);
			
		} catch (Exception e) {
			ret = null;
		}
		
		return ret;
	}
}
