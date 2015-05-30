package com.snail.traffic.persistence.select;

import com.snail.traffic.container.data.TwoStringStruct;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by weiliu on 2015/5/29.
 */
public class SelectSiteToLineView extends SelectView {

    /**
     * constructor
     * @param con
     */
    public SelectSiteToLineView(Connection con) {
        this.con = con;
        initPreparedStatement();
    }

    /**
     * initialize PreparedStatement
     */
    @Override
    protected void initPreparedStatement() {
        String viewSiteLineSql = "SELECT llidseq, rlidseq FROM View_SiteLine WHERE sname = ?";
        try {
            this.pre_View = this.con.prepareStatement(viewSiteLineSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * get two long String from view
     *
     * @param siteName
     * @return lineSeq
     *          TwoStringStruct or null
     */
    @Override
    public TwoStringStruct getSeq(String siteName) {
        TwoStringStruct lineSeq = null;
        try {
            this.pre_View.setString(1, siteName);
            ResultSet rs = this.pre_View.executeQuery();

            if(rs.next()) {
                lineSeq = new TwoStringStruct();
                lineSeq.put(true, rs.getString(1));
                lineSeq.put(false, rs.getString(2));
                return lineSeq;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lineSeq; // TwoStringStruct/ null
    }
}
