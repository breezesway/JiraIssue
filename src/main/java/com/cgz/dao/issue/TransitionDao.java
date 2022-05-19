package com.cgz.dao.issue;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.issue.Transition;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TransitionDao {
    public void insertTransitions(List<Transition> transitions) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="replace into transition values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(Transition transition:transitions){
            pstmt.setObject(1,transition.getAuthorDisplayName());
            pstmt.setObject(2,transition.getCreated());
            pstmt.setObject(3,transition.getIssueKey());
            pstmt.setObject(4,transition.getFromString());
            pstmt.setObject(5,transition.getToString());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
