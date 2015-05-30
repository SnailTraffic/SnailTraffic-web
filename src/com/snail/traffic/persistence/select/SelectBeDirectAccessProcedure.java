package com.snail.traffic.persistence.select;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;

import com.snail.traffic.container.access.BeDirectAccessStruct;

/**
 * Created by weiliu on 2015/5/29.
 */
public class SelectBeDirectAccessProcedure extends SelectAccessProcedure {

    /**
     * constructor
     * @param con
     */
    public SelectBeDirectAccessProcedure(Connection con) {
        this.con = con;
        initPreparedStatement();
    }

    /**
     * initialize PreparedStatement
     */
    @Override
    protected void initPreparedStatement() {
        try {
            this.pre_Procedure = con.prepareCall("BEGIN BEDIRECTACCESS(?, ?); END;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * get all of Access Sites
     *
     * @param name
     * @return
     */
    @Override
    public Vector<BeDirectAccessStruct> getAccessSites(String endSite) {
        Vector<BeDirectAccessStruct> directSites = null;// 可直达站点向量
        BeDirectAccessStruct newElement = null;
        try {
            this.pre_Procedure.setString(1, endSite);
            this.pre_Procedure.registerOutParameter(2, OracleTypes.CURSOR);
            this.pre_Procedure.execute();
            ResultSet rs = ((OracleCallableStatement)this.pre_Procedure).getCursor(2);

            directSites = new Vector<BeDirectAccessStruct>();
            while (rs.next()) {
                newElement = new BeDirectAccessStruct();
                newElement.siteName = rs.getString("SNAME");
                newElement.lineName = rs.getString("LNAME");
                newElement.runLeft = rs.getInt("RUNLEFT");
                directSites.add(newElement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return directSites;
    }
}
