package com.snail.traffic.persistence;

/**
 * ��·��������Ϣ�ṹ��
 * @author weiliu
 *
 */
public class LineAllInfoStruct extends InfoStruct{
	
	public String lineName		= null;	// ��·��
	public String lineRange		= null; // ��·����
	public String firstOpen		= null; // ��վ����
	public String lastOpen		= null; // ĩվ����
	public String firstClose	= null; // ��վ�հ�
	public String lastClose 	= null;	// ĩվ�հ�
	public String price 		= null;	// Ʊ��
	public String cardPrice		= null; // ˢ��Ʊ��
	public String company		= null; // ������˾
	public String remark		= null;	// ��ע
	
	/**
	 * ������·����
	 * @param lineRange
	 */
	public void setLine(String linename, String lineRange) {
		this.lineName = linename;
		this.lineRange = lineRange;
	}
	
	/**
	 * ������·ʱ��
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
	 * ���ü۸�
	 * @param price
	 * @param cardPrice
	 */
	public void setPrice(String price, String cardPrice) {
		this.price		= price;
		this.cardPrice 	= cardPrice;	
	}
	
	/**
	 * ����������Ϣ
	 * @param company
	 * @param remark
	 */
	public void setCompany(String company) {
		this.company = company;
	}
}
