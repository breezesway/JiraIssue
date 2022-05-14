package com.cgz.dao.issue;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.issue.Attachment;
import com.cgz.bean.issue.IssueLink;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IssueLinkDao {
    public static void insertIssueLink(IssueLink issueLink) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into issuelink values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setObject(1,issueLink.getId());
        pstmt.setObject(2,issueLink.getType().getName());
        pstmt.setObject(3,issueLink.getInwardIssueKey());
        pstmt.setObject(4,issueLink.getOutwardIssueKey());
        pstmt.setObject(5,issueLink.getSelf());
        pstmt.execute();
        pstmt.close();
        conn.close();
    }
}
