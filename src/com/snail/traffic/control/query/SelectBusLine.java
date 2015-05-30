package com.snail.traffic.control.query;

import java.sql.Connection;

import com.snail.traffic.container.info.InfoLineStruct;
import com.snail.traffic.persistence.admin.AdminLineTable;
import com.snail.traffic.persistence.admin.AdminSiteTable;
import com.snail.traffic.container.info.InfoStruct;
import com.snail.traffic.persistence.select.SelectLineToSiteView;
import com.snail.traffic.container.data.TwoStringStruct;

public class SelectBusLine extends SelectTransit {

	/**
	 * constructor
	 * @param con
	 */
	public SelectBusLine(Connection con) {
		this.selectView = new SelectLineToSiteView(con);
		this.adminLine = new AdminLineTable(con);
		this.adminSite = new AdminSiteTable(con);
	}
	
	/**
	 * query line information
	 * @param lineName
	 * @return lineInfo
	 *
	 */
	@Override
	public InfoStruct query(String lineName) {
		lineName = inputOptimize(lineName);
		InfoLineStruct lineInfo = adminLine.getInfo(lineName);
		if (lineInfo == null)
			return null;

		TwoStringStruct sidSeq = this.selectView.getSeq(lineName);
		lineInfo.left = getElements(sidSeq.get(true));
		lineInfo.right = getElements(sidSeq.get(false));
		return lineInfo;
	}
	
	/**
	 * add a suffix
	 * @param input
	 * @return
	 */
	private String inputOptimize(String input) {
		if (!input.endsWith("·"))
			input = input.concat("·");
		return input;
	}

	/**
	 * get elements from a long String
	 *
	 * @param sidSeq
	 * @return String[]
	 */
	@Override
	protected String[] getElements(String sidSeq) {
		if(sidSeq == null)
			return null;

		String[] sites = sidSeq.split(",");
		// replace original array
		for(int i = 0; i < sites.length; i++)
			sites[i] = adminSite.getName(Integer.parseInt(sites[i]));
		return sites;
	}
}
