package com.snail.traffic.persistence.admin;

public abstract class BaseAdminRelationTable extends BaseAdminTable {

    /**
     * add a key to value
     * @param key
     * @param left
     * @param right
     */
	public abstract boolean addKeyToValue(int key, String left, String right);

    /**
     * update value of key
     * @param key
     * @param left
     * @param right
     */
	public abstract boolean updateKeyToValue(int key, String left, String right);

}
