package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * ������Ϣ�������
 * @author weiliu
 *
 */
public abstract class AdminInfoTableBase {

	protected Connection con = null;	// ���ݿ�����
	protected PreparedStatement pre_insert = null;	// ��������Ԥ����
	protected PreparedStatement pre_update = null;	// ��������Ԥ����	
	protected PreparedStatement pre_delete = null;	// ɾ������Ԥ����	
	protected PreparedStatement pre_getId  = null;	// ��ȡidԤ����

	// ��ʼ��Ԥ�������
	protected abstract void initPreparedStatement();
	
	// ɾ��һ��
	public abstract void delete(Object input);
	
	// ���id
	public abstract int getId(Object input);
	
	// �õ�����¼��
	public abstract int getNumber();
	
	// ������µı��
	public abstract int getNewId();
	
	// ��ȡ����
	public abstract String getName(int id);
}
