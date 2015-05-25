package com.snail.traffic.control;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Vector;

import com.snail.traffic.persistence.AdminInfoTableBase;
import com.snail.traffic.persistence.AdminLineTable;
import com.snail.traffic.persistence.AdminSiteTable;
import com.snail.traffic.persistence.InfoStruct;
import com.snail.traffic.persistence.SelectOperated;
import com.snail.traffic.persistence.SiteInfoStruct;
import com.snail.traffic.persistence.TwoLongStruct;
/**
 * վ���ѯ����
 * 2.����վ����,�����ݿ��л�ȡ��վ����������������·��ɵ���·����(ע��ȥ�кͻ���)
 * 3.����һ������վ����Ŵ�С�������е���·��������
 */
class SelectBusSite extends SelectBase {
	private SelectOperated seloper = null;	// ���ݿ��ѯ����
	private AdminInfoTableBase adminLine = null;
	private AdminSiteTable adminSite = null;
	
	private Vector<String> fuzzyResult = new Vector<String>();	// ģ�������
	
	private String lastInput = null;	// ��һ�ε�����
	
	
	/**
	 * ���캯��
	 * @param con
	 * 			���ݿ�����
	 */
	public SelectBusSite(Connection con) {
		seloper = new SelectOperated(con);
		adminLine = new AdminLineTable(con);
		adminSite = new AdminSiteTable(con);
	}

	/**
	 * ��ѯվ����Ϣ����
	 * @param input
	 * 			��ѯ�ؼ���
	 * @return siteinfo
	 * 				վ����Ϣ
	 */
	public InfoStruct query(String input) {
		
		TwoLongStruct lineSeq = seloper.getSiteLineSeq(input);	// ��ȡ���������ַ�
		
		InfoStruct siteinfo = new SiteInfoStruct();	// ��������վ����Ϣ����
		
		siteinfo.put(true, getLines(lineSeq.get(true)));	// ����ߵ���·������ŵ����
		siteinfo.put(false, getLines(lineSeq.get(false)));	
		siteinfo.setName(input);
		
		return siteinfo;
	}
	
	
	/**
	 * ����һ������վ����Ŵ�С�������е���·��������
	 * @param sitename
	 * 				վ����
	 * @return lineSort(lines)
	 * 				����õ��ַ�������
	 */
	private String[] getLines(String lidseq) {
		
		if(lidseq == null)	// ��·����Ϊ��ʱ������
			return null;
		
		String[] lines = lidseq.split(",");	// ����վ�������·
		
		for(int i = 0; i < lines.length; i++)
			lines[i] = adminLine.getName(Integer.parseInt(lines[i])); // ����·���滻��·�����ַ���
		
		Arrays.sort(lines);	// ������·
		return lines;
	}
	
	/**
	 * ģ����ѯ�и��ݲ�����Ϣ,ƥ����ϵ�ȫ��(�����ж��Ƿ���ƥ����)
	 * @param input
	 * 			������Ϣ
	 * @return fuzzyResult
	 * 				ģ�������
	 */
	public Vector<String> fuzzySearch(String input) {
		if (input.equals(""))
			return null;
		
		if (input.matches("(?i)[^a-z]*[a-z]+[^a-z]*"))
			return null;
		
		// ��ǰ��������һ������Ϊǰ׺ʱ
		if (this.lastInput != null && input.startsWith(this.lastInput)) {	// ��Vector��ȡ
			
			Vector<String> tempResult = new Vector<String>();	// ��ʱģ�������
			
			int lenResult = fuzzyResult.size();
			
			String regex = null;
			
			for (int i = 0; i < lenResult; i++) {
				regex = ".*" + input + ".*";	// ����ʽ
				
				if (fuzzyResult.get(i).matches(regex))
					tempResult.add(fuzzyResult.get(i));
			}
			this.fuzzyResult = tempResult;
		}
		else
			fuzzyResult = adminSite.getFuzzyResult(input);	//�����ݿ���ȡ
		
		lastInput = input;
		return fuzzyResult;	
	}
}
