package com.snail.traffic.container.data;

public class SiteToLineStruct {
	private String leftLidSeq = null;
	private String rightLidSeq = null;
	
	/**
	 * add a new left lid
	 * @param lid
	 */
	public void setLeftLid(Integer lid) {
		if (this.leftLidSeq == null)
			this.leftLidSeq = "" + lid;
		else
			this.leftLidSeq += ( "," + lid);
	}
	
	/**
	 * add a new right lid
	 * @param lid
	 */
	public void setRightLid(Integer lid) {
		if (this.rightLidSeq == null)
			this.rightLidSeq = "" + lid;
		else
			this.rightLidSeq += ("," + lid);
	}
	
	/**
	 * get a left lidSeq
	 * @return
	 */
	public String getLeftLid() {
		return this.leftLidSeq;
	}
	
	/**
	 * get a right lidSeq
	 * @return
	 */
	public String getRightLid() {
		return this.rightLidSeq;
	}
}
