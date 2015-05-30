package com.snail.traffic.persistence.admin;

import java.sql.PreparedStatement;

public abstract class BaseAdminInfoTable extends BaseAdminTable {
	protected PreparedStatement pre_getId  = null;	// get id
	protected PreparedStatement pre_getName = null; // get name

	/**
	 * get id on the table according to input
	 * @param input
	 * @return id
	 */
	public abstract int getId(Object input);

	/**
	 * get count of this table
	 * @return count
	 */
	public abstract int getNumber();

	/**
	 * get a new id
	 * @return newid
	 */
	public abstract int getNewId();

	/**
	 * get name of id
	 * @param id
	 * @return name
	 */
	public abstract String getName(int id);
}
