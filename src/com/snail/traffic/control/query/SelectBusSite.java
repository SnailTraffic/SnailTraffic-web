package com.snail.traffic.control.query;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Vector;

import com.snail.traffic.persistence.admin.AdminLineTable;
import com.snail.traffic.persistence.admin.AdminSiteTable;
import com.snail.traffic.container.info.InfoStruct;
import com.snail.traffic.container.info.InfoSiteStruct;
import com.snail.traffic.container.data.TwoStringStruct;
import com.snail.traffic.persistence.select.SelectSiteToLineView;

class SelectBusSite extends SelectTransit {
	
	private Vector<String> fuzzyResult = new Vector<>();
	private String lastInput = null;

	/**
	 * constructor
	 * @param con
	 */
	public SelectBusSite(Connection con) {
		this.selectView = new SelectSiteToLineView(con);
		this.adminLine = new AdminLineTable(con);
		this.adminSite = new AdminSiteTable(con);
	}

	/**
	 * query site information
	 * @param siteName
	 * @return siteInfo
	 */
	@Override
	public InfoStruct query(String siteName) {
		TwoStringStruct lineSeq = this.selectView.getSeq(siteName);
		InfoSiteStruct siteInfo = new InfoSiteStruct();

		siteInfo.name = siteName;
		siteInfo.left =  getElements(lineSeq.get(true));
		siteInfo.right = getElements(lineSeq.get(false));
		return siteInfo;
	}

	/**
	 * get elements from a long String
	 *
	 * @param lidSeq
	 * @return String[]
	 */
	@Override
	protected String[] getElements(String lidSeq) {
		if(lidSeq == null)
			return null;
		String[] lines = lidSeq.split(",");

		// replace original array
		for(int i = 0; i < lines.length; i++)
			lines[i] = adminLine.getName(Integer.parseInt(lines[i]));

		Arrays.sort(lines);
		return lines;
	}

	/**
	 * fuzzySearch
	 * @param partName
	 * @return fuzzyResult
	 */
	public Vector<String> fuzzySearch(String partName) {
		if (partName.equals(""))
			return null;

		// exist letter
		if (partName.matches("(?i)[^a-z]*[a-z]+[^a-z]*"))
			return null;
		
		// whether prefix
		if (this.lastInput != null && partName.startsWith(this.lastInput)) {
			Vector<String> tempResult = new Vector<>();
			int lenResult = fuzzyResult.size();
			String regex = null;
			String ret = null;

			for (int i = 0; i < lenResult; i++) {
				regex = ".*" + partName + ".*";
				ret = fuzzyResult.get(i);
				if (ret.matches(regex))
					tempResult.add(ret);
			}
			this.fuzzyResult.clear();
			this.fuzzyResult = tempResult;
		}
		else	// get fuzzy result from DB
			fuzzyResult = adminSite.getFuzzyResult(partName);
		
		lastInput = partName;
		return fuzzyResult;	
	}
}
