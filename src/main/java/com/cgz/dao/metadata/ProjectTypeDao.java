package com.cgz.dao.metadata;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.metadata.ProjectType;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProjectTypeDao {
    public static void insertProjectTypes(List<ProjectType> projectTypes) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into projecttype values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(ProjectType projectType:projectTypes){
            pstmt.setObject(1,projectType.getKey());
            pstmt.setObject(2,projectType.getFormattedKey());
            pstmt.setObject(3,projectType.getDescriptionI18nKey());
            pstmt.setObject(4,projectType.getColor());
            pstmt.setObject(5,projectType.getIcon());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
