package com.snail.traffic.control;

/**
 * վ����·���нṹ��
 * @author weiliu
 *
 */
public class SiteLineClass {
	private String Llidseq = null;	// ����·����
	private String Rlidseq = null;	// ����·����
	
	/**
	 * ������������·
	 * @param lid
	 * 			��·id
	 */
	public void setALlid(Integer lid) {	
		if (this.Llidseq == null)
			this.Llidseq = "" + lid;
		else
			this.Llidseq += ( "," + lid);
	}
	
	/**
	 * ������������·
	 * @param lid
	 * 			��·id
	 */
	public void setARlid(Integer lid) {
		if (this.Rlidseq == null)
			this.Rlidseq = "" + lid;
		else
			this.Rlidseq += ("," + lid);
	}
	
	/**
	 * ��ȡ������·����
	 * @return
	 */
	public String getALlid() {
		return Llidseq;
	}
	
	/**
	 * ��ȡ������·����
	 * @return
	 */
	public String getARlid() {
		return Rlidseq;
	}
}
