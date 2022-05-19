package com.cgz.dao.issue;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.issue.History;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class HistoryDao {
    public void insertHistories(List<History> histories) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="replace into history values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(History history:histories){
            pstmt.setObject(1,history.getId());
            pstmt.setObject(2,history.getAuthor().getDisplayName());
            pstmt.setObject(3,history.getCreated());
            pstmt.setObject(4,history.getIssueKey());
            pstmt.setObject(5,history.getItems().toString());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
