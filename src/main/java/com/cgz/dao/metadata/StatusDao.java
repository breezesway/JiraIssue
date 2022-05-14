package com.cgz.dao.metadata;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.metadata.Status;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StatusDao {
    public static void insertStatuses(List<Status> statuses) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into status values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (Status status:statuses){
            pstmt.setObject(1,status.getId());
            pstmt.setObject(2,status.getName());
            pstmt.setObject(3,status.getDescription());
            pstmt.setObject(4,status.getIconUrl());
            pstmt.setObject(5,status.getSelf());
            pstmt.setObject(6,status.getStatusCategory().getId());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
