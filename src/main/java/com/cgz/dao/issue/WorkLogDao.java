package com.cgz.dao.issue;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.issue.RemoteLink;
import com.cgz.bean.issue.WorkLog;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class WorkLogDao {
    public void insertWorkLogs(List<WorkLog> workLogs) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert ignore into worklog values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(WorkLog workLog:workLogs){
            pstmt.setObject(1,workLog.getId());
            pstmt.setObject(2,workLog.getComment());
            pstmt.setObject(3,workLog.getCreated());
            pstmt.setObject(4,workLog.getUpdated());
            pstmt.setObject(5,workLog.getStarted());
            pstmt.setObject(6,workLog.getTimeSpent());
            pstmt.setObject(7,workLog.getTimeSpentSeconds());
            pstmt.setObject(8,workLog.getAuthor()!=null?workLog.getAuthor().getDisplayName():null);
            pstmt.setObject(9,workLog.getUpdateAuthor()!=null?workLog.getUpdateAuthor().getDisplayName():null);
            pstmt.setObject(10,workLog.getIssueId());
            pstmt.setObject(11,workLog.getIssueKey());
            pstmt.setObject(12,workLog.getSelf());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
