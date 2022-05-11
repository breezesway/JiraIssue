package com.cgz.dao.jql;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.jql.VisibleFunctionName;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class VisibleFunctionNameDao {
    public static void insertVisibleFunctionName(List<VisibleFunctionName> visibleFieldNames) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into visiblefunctionname values(?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(VisibleFunctionName visibleFunctionName:visibleFieldNames){
            pstmt.setObject(1,visibleFunctionName.getValue());
            pstmt.setObject(2,visibleFunctionName.getDisplayName());
            pstmt.setObject(3,visibleFunctionName.getIsList());
            pstmt.setObject(4, Arrays.toString(visibleFunctionName.getTypes()));
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
