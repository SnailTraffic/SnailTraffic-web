package com.snail.traffic.control;

/**
 * 站点线路序列结构体
 * @author weiliu
 *
 */
public class SiteLineClass {
	private String Llidseq = null;	// 左线路集合
	private String Rlidseq = null;	// 右线路集合
	
	/**
	 * 加入左行新线路
	 * @param lid
	 * 			线路id
	 */
	public void setALlid(Integer lid) {	
		if (this.Llidseq == null)
			this.Llidseq = "" + lid;
		else
			this.Llidseq += ( "," + lid);
	}
	
	/**
	 * 加入右行新线路
	 * @param lid
	 * 			线路id
	 */
	public void setARlid(Integer lid) {
		if (this.Rlidseq == null)
			this.Rlidseq = "" + lid;
		else
			this.Rlidseq += ("," + lid);
	}
	
	/**
	 * 获取左行线路集合
	 * @return
	 */
	public String getALlid() {
		return Llidseq;
	}
	
	/**
	 * 获取右行线路集合
	 * @return
	 */
	public String getARlid() {
		return Rlidseq;
	}
}
