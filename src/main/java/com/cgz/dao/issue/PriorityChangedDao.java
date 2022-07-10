package com.cgz.dao.issue;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.issue.PriorityChanged;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PriorityChangedDao {
    public void insertPriorityChangeds(List<PriorityChanged> priorityChangeds) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert ignore into prioritychanged values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(PriorityChanged priorityChanged:priorityChangeds){
            pstmt.setObject(1,priorityChanged.getAuthorDisplayName());
            pstmt.setObject(2,priorityChanged.getCreated());
            pstmt.setObject(3,priorityChanged.getIssueKey());
            pstmt.setObject(4,priorityChanged.getFromString());
            pstmt.setObject(5,priorityChanged.getToString());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
