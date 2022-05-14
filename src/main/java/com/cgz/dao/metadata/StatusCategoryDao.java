package com.cgz.dao.metadata;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.metadata.StatusCategory;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StatusCategoryDao {
    public static void insertStatusCategories(List<StatusCategory> statusCategories) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into statuscategory values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (StatusCategory statusCategory:statusCategories){
            pstmt.setObject(1,statusCategory.getId());
            pstmt.setObject(2,statusCategory.getKey());
            pstmt.setObject(3,statusCategory.getName());
            pstmt.setObject(4,statusCategory.getColorName());
            pstmt.setObject(5,statusCategory.getSelf());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
