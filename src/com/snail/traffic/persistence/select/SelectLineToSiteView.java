package com.snail.traffic.persistence.select;

import com.snail.traffic.container.data.TwoStringStruct;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by weiliu on 2015/5/29.
 */
public class SelectLineToSiteView extends SelectView {

    /**
     * constructor
     * @param con
     */
    public SelectLineToSiteView(Connection con) {
        this.con = con;
        initPreparedStatement();
    }

    /**
     * initialize PreparedStatement
     */
    @Override
    protected void initPreparedStatement() {
        String viewLineSiteSql = "SELECT lsidseq, rsidseq FROM View_LineSite WHERE lname=?";
        try {
            this.pre_View = this.con.prepareStatement(viewLineSiteSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * get two long String from view
     *
     * @param lineName
     * @return siteSeq
     *          TwoStringStruct or null
     */
    @Override
    public TwoStringStruct getSeq(String lineName) {
        TwoStringStruct siteSeq = null;
        try {
            this.pre_View.setString(1, lineName);
            ResultSet rs = this.pre_View.executeQuery();

            if (rs.next()) {
                siteSeq = new TwoStringStruct();
                siteSeq.put(true, rs.getString(1));
                siteSeq.put(false, rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return siteSeq;
    }
}
