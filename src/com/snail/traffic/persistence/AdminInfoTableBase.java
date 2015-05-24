package com.snail.traffic.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 管理信息表抽象类
 * @author weiliu
 *
 */
public abstract class AdminInfoTableBase {

	protected Connection con = null;	// 数据库连接
	protected PreparedStatement pre_insert = null;	// 插入数据预编译
	protected PreparedStatement pre_update = null;	// 更新数据预编译	
	protected PreparedStatement pre_delete = null;	// 删除数据预编译	
	protected PreparedStatement pre_getId  = null;	// 获取id预编译

	// 初始化预编译语句
	protected abstract void initPreparedStatement();
	
	// 删除一行
	public abstract void delete(Object input);
	
	// 获得id
	public abstract int getId(Object input);
	
	// 得到表格记录数
	public abstract int getNumber();
	
	// 获得最新的编号
	public abstract int getNewId();
	
	// 获取名字
	public abstract String getName(int id);
}
