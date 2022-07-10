package com.cgz.dao.issue;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.issue.IssueLink;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class IssueLinkDao {
    public void insertIssueLink(IssueLink issueLink) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="replace into issuelink values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        setValues(pstmt,issueLink);
        pstmt.execute();
        pstmt.close();
        conn.close();
    }

    public void insertIssueLinks(List<IssueLink> issueLinks) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert ignore into issuelink values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (IssueLink issueLink:issueLinks){
            setValues(pstmt,issueLink);
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }

    private void setValues(PreparedStatement pstmt, IssueLink issueLink) throws SQLException {
        pstmt.setObject(1,issueLink.getId());
        pstmt.setObject(2,issueLink.getType().getName());
        pstmt.setObject(3,issueLink.getInwardIssueKey());
        pstmt.setObject(4,issueLink.getOutwardIssueKey());
        pstmt.setObject(5,issueLink.getSelf());
    }
}
