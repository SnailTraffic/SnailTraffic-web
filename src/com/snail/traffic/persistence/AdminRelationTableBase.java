package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 管理关系表抽象类
 * @author weiliu
 *
 */
public abstract class AdminRelationTableBase {

	protected Connection con = null;	// 数据库连接
	protected PreparedStatement pre_insert = null;	// 插入数据预编译
	protected PreparedStatement pre_update = null;	// 更新数据预编译	
	protected PreparedStatement pre_delete = null;	// 删除数据预编译	
	
	// 初始化预编译语句
	protected abstract void initPreparedStatement();
	
	// 增加
	public abstract void addKeyToValue(int key, String left, String right);
	
	// 删除
	public abstract void deleteKey(int key);
	
	// 更新
	public abstract void updateKeyToValue(int key, String left, String right);
	
	
}
