package com.cgz.dao.metadata;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.metadata.ProjectCategory;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProjectCategoryDao {
    public void insertProjectCategories(List<ProjectCategory> projectCategories) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into projectcategory values(?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(ProjectCategory projectCategory:projectCategories){
            pstmt.setObject(1,projectCategory.getId());
            pstmt.setObject(2,projectCategory.getName());
            pstmt.setObject(3,projectCategory.getDescription());
            pstmt.setObject(4,projectCategory.getSelf());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
