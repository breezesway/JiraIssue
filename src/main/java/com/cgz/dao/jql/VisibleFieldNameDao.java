package com.cgz.dao.jql;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.jql.VisibleFieldName;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class VisibleFieldNameDao {
    public void insertVisibleFieldName(List<VisibleFieldName> visibleFieldNames) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into visiblefieldname values(?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(VisibleFieldName visibleFieldName:visibleFieldNames){
            pstmt.setObject(1,visibleFieldName.getValue());
            pstmt.setObject(2,visibleFieldName.getDisplayName());
            pstmt.setObject(3,visibleFieldName.getAuto());
            pstmt.setObject(4,visibleFieldName.getOrderable());
            pstmt.setObject(5,visibleFieldName.getSearchable());
            pstmt.setObject(6, Arrays.toString(visibleFieldName.getOperators()));
            pstmt.setObject(7, Arrays.toString(visibleFieldName.getTypes()));
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
