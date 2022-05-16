package com.cgz.dao.jql;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class JqlReservedWordDao {
    public void insertVisibleFunctionName(List<String> jqlReservedWords) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into jqlreservedword values(?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(String jqlReservedWord:jqlReservedWords){
            pstmt.setObject(1,jqlReservedWord);
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
