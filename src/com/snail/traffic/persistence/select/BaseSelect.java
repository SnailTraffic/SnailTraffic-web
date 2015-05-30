package com.snail.traffic.persistence.select;

import java.sql.Connection;

/**
 * Created by weiliu on 2015/5/29.
 */
public abstract class BaseSelect {

    protected Connection con = null;

    /**
     * initialize PreparedStatement
     */
    protected abstract void initPreparedStatement();
}
