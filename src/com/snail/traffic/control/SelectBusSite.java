package com.snail.traffic.control;

import java.sql.Connection;
import java.util.Arrays;

import com.snail.traffic.persistence.AdminInfoTableBase;
import com.snail.traffic.persistence.AdminLineTable;
import com.snail.traffic.persistence.InfoStruct;
import com.snail.traffic.persistence.SelectOperated;
import com.snail.traffic.persistence.TwoLongStruct;
/**
 * վ���ѯ����
 * 2.����վ����,�����ݿ��л�ȡ��վ����������������·��ɵ���·����(ע��ȥ�кͻ���)
 * 3.����һ������վ����Ŵ�С�������е���·��������
 */
class SelectBusSite extends SelectBase {
	SelectOperated seloper = null;	// ���ݿ��ѯ����
	AdminInfoTableBase adminLine = null; 
	/**
	 * ���캯��
	 * @param con
	 * 			���ݿ�����
	 */
	public SelectBusSite(Connection con) {
		seloper = new SelectOperated(con);
		adminLine = new AdminLineTable(con);
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
		
		InfoStruct siteinfo = new InfoStruct();	// ��������վ����Ϣ����
		
		siteinfo.put(true, getLines(lineSeq.get(true)));	// ����ߵ���·������ŵ����
		siteinfo.put(false, getLines(lineSeq.get(false)));	
		
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
	 * @param partname
	 * 				վ����
	 * @return fullname
	 * 				վ��ȫ��
	 */
	public String[] getFullName(String partname) {
		String[] fullname = null;
		/*���ݿ����:����partName����ƥ���fullName����*/
		
		return fullname;	
	}
	

}
