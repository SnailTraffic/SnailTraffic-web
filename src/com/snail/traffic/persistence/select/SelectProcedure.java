package com.snail.traffic.persistence.select;

import java.sql.CallableStatement;

/**
 * Created by weiliu on 2015/5/29.
 */
public abstract class SelectProcedure extends BaseSelect {

    protected CallableStatement pre_Procedure = null;   // call Procedure

}
