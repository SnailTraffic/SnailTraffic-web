package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * �����ϵ�������
 * @author weiliu
 *
 */
public abstract class AdminRelationTableBase {

	protected Connection con = null;	// ���ݿ�����
	protected PreparedStatement pre_insert = null;	// ��������Ԥ����
	protected PreparedStatement pre_update = null;	// ��������Ԥ����	
	protected PreparedStatement pre_delete = null;	// ɾ������Ԥ����	
	
	// ��ʼ��Ԥ�������
	protected abstract void initPreparedStatement();
	
	// ����
	public abstract void addKeyToValue(int key, String left, String right);
	
	// ɾ��
	public abstract void deleteKey(int key);
	
	// ����
	public abstract void updateKeyToValue(int key, String left, String right);
	
	
}
