package com.snail.traffic.persistence.select;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;

/**
 * Created by weiliu on 2015/5/29.
 */
public class SelectRouteProcedure extends SelectProcedure {

    /**
     * constructor
     * @param con
     */
    public SelectRouteProcedure(Connection con) {
        this.con = con;
        initPreparedStatement();
    }

    /**
     * initialize PreparedStatement
     */
    @Override
    protected void initPreparedStatement() {
        try {
            this.pre_Procedure = con.prepareCall("BEGIN ROUTE(?, ?, ?, ?, ?); END;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * get route from start to end
     * @param startName
     * @param endName
     * @param lineName
     * @param runLeft
     * @return
     */
    public Vector<String> getRoute(String startName, String endName, String lineName, int runLeft) {
        Vector<String> route = null;
        try {
            this.pre_Procedure.setString(1, startName);
            this.pre_Procedure.setString(2, endName);
            this.pre_Procedure.setString(3, lineName);
            this.pre_Procedure.setInt(4, runLeft);
            this.pre_Procedure.registerOutParameter(5, OracleTypes.CURSOR);

            this.pre_Procedure.execute();
            ResultSet rs = ((OracleCallableStatement)this.pre_Procedure).getCursor(5);

            route = new Vector<>();
            while (rs.next()) {
                String newElement = rs.getString("SNAME");
                route.add(newElement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return route;
    }
}
