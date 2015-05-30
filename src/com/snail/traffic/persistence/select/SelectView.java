package com.snail.traffic.persistence.select;

import com.snail.traffic.container.data.TwoStringStruct;

import java.sql.PreparedStatement;

/**
 * Created by weiliu on 2015/5/29.
 */
public abstract class SelectView extends BaseSelect {

    protected PreparedStatement pre_View = null;

    /**
     * get two long String from view
     * @param name
     * @return
     */
    public abstract TwoStringStruct getSeq(String name);


}
