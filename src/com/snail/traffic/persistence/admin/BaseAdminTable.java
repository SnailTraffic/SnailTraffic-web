package com.snail.traffic.persistence.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by weiliu on 2015/5/29.
 */
public abstract class BaseAdminTable {
    protected Connection con = null;
    protected PreparedStatement pre_insert = null;	// insert
    protected PreparedStatement pre_update = null;	// update
    protected PreparedStatement pre_delete = null;	// delete

    /**
     * initialize PreparedStatement
     */
    protected abstract void initPreparedStatement();

    /**
     * delete a record
     * @param input
     */
    public abstract boolean delete(Object input);

}
