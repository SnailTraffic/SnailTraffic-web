package com.snail.traffic.control;

import java.sql.Connection;

import com.snail.traffic.persistence.AdminLineTable;
import com.snail.traffic.persistence.InfoStruct;
import com.snail.traffic.persistence.LineAllInfoStruct;
import com.snail.traffic.persistence.SelectOperated;
import com.snail.traffic.persistence.TwoLongStruct;

/*
 * ��·��ѯ
 * 1.��ǰ�˻�ȡ��·����
 * 2.������·��,�����ݿ��л�ȡ��·�ϵ�վ��(ע��ȥ�кͻ���)��ɵ�����
 * 3.���ز���2������
 */
public class SelectBusLine extends SelectBase {
	SelectOperated seloper = null;	// ���ݿ��ѯ����
	AdminLineTable adminLine = null;	// ������·��Ϣ����
	
	/**
	 * ���캯��
	 * @param con
	 * 			���ݿ�����
	 */
	public SelectBusLine(Connection con) {
		this.seloper = new SelectOperated(con);
		this.adminLine = new AdminLineTable(con);
	}
	
	/**
	 * ��ѯ��·��Ϣ����
	 * @param input
	 * 			��ѯ�ؼ���
	 * @return lineinfo
	 * 				վ����Ϣ
	 */
	public InfoStruct query(String input) {
		LineAllInfoStruct lineinfo = adminLine.getInfo(input);		// ��ȡ��·������Ϣ
		TwoLongStruct sidSeq = seloper.getLineSiteSeq(input);	// ��ȡ���������ַ�
		
		lineinfo.put(true, getSites(sidSeq.get(true)));	// ����ߵ���·������ŵ����
		lineinfo.put(false, getSites(sidSeq.get(false)));	
		
		return lineinfo;
	}
	
	private String[] getSites(String sidseq) {
		if(sidseq == null)	// վ������Ϊ��ʱ������
			return null;
		
		String[] sites = sidseq.split(",");	// ����վ�������·
		
		for(int i = 0; i < sites.length; i++)
			sites[i] = adminLine.getName(Integer.parseInt(sites[i])); // ����·���滻��·�����ַ���
		
		return sites;
	}
}