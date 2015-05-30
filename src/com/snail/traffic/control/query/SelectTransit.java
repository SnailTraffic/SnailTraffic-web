package com.snail.traffic.control.query;

import com.snail.traffic.container.info.InfoStruct;
import com.snail.traffic.persistence.admin.AdminLineTable;
import com.snail.traffic.persistence.admin.AdminSiteTable;
import com.snail.traffic.persistence.select.SelectView;

/**
 * Created by weiliu on 2015/5/29.
 */
public abstract class SelectTransit {

    protected SelectView selectView = null;
    protected AdminLineTable adminLine = null;
    protected AdminSiteTable adminSite = null;

    public abstract InfoStruct query(String name);
    /**
     * get elements from a long String
     * @param longString
     * @return String[]
     */
    protected abstract String[] getElements(String longString);
}
