package com.cgz.dao.metadata;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.metadata.Priority;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PriorityDao {
    public void insertPriority(List<Priority> priorities) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into priority(id,name,description,statusColor,self,iconUrl) values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(Priority priority:priorities){
            pstmt.setObject(1,priority.getId());
            pstmt.setObject(2,priority.getName());
            pstmt.setObject(3,priority.getDescription());
            pstmt.setObject(4,priority.getStatusColor());
            pstmt.setObject(5,priority.getSelf());
            pstmt.setObject(6,priority.getIconUrl());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
