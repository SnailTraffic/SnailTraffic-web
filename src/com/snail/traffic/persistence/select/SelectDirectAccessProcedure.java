package com.snail.traffic.persistence.select;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;

import com.snail.traffic.container.access.DirectAccessStruct;

/**
 * Created by weiliu on 2015/5/29.
 */
public class SelectDirectAccessProcedure extends SelectAccessProcedure {

    /**
     * constructor
     * @param con
     */
    public SelectDirectAccessProcedure(Connection con) {
        this.con = con;
        initPreparedStatement();
    }

    /**
     * initialize PreparedStatement
     */
    @Override
    protected void initPreparedStatement() {
        try {
            this.pre_Procedure = this.con.prepareCall("BEGIN DIRECTACCESS(?, ?); END;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * get all of Access Sites
     *
     * @param startSite
     *          start site name
     * @return directSites
     *          Vector<>
     */
    @Override
    public ArrayList<DirectAccessStruct> getAccessSites(String startSite) {
	ArrayList<DirectAccessStruct> directSites = null;
        DirectAccessStruct newElement = null;
        try {
            this.pre_Procedure.setString(1, startSite);
            this.pre_Procedure.registerOutParameter(2, OracleTypes.CURSOR);
            this.pre_Procedure.execute();
            ResultSet rs = ((OracleCallableStatement)this.pre_Procedure).getCursor(2);

            directSites = new ArrayList<DirectAccessStruct>();
            while (rs.next()) {
                newElement = new DirectAccessStruct();
                newElement.siteName = rs.getString("SNAME");
                newElement.lineName = rs.getString("LNAME");
                newElement.runLeft = rs.getInt("RUNLEFT");
                newElement.runTime = rs.getInt("RUNTIME");
                newElement.distance = rs.getInt("DISTANCE");
                directSites.add(newElement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

//		for(int i = 0; i < directSites.size(); i++)
//			System.out.println(directSites.get(i).sname);

        return directSites;
    }
}
