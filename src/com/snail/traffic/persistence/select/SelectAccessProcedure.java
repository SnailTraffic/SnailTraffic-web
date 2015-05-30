package com.snail.traffic.persistence.select;

import java.util.Vector;

/**
 * Created by weiliu on 2015/5/29.
 */
public abstract class SelectAccessProcedure extends SelectProcedure {

    /**
     * get all of Access Sites
     * @return
     */
    public abstract Vector<?> getAccessSites(String name);
}
