package com.snail.traffic.persistence;

/**
 * 线路的所有信息结构体
 * @author weiliu
 *
 */
public class LineAllInfoStruct extends InfoStruct{
	
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
	public void setCompany(String company) {
		this.company = company;
	}
}
