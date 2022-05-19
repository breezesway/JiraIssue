package com.cgz.dao.project;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.project.Project;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProjectDao {
    public void insertProjects(List<Project> projects) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="replace into project values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (Project project:projects){
            pstmt.setObject(1,project.getId());
            pstmt.setObject(2,project.getKey());
            pstmt.setObject(3,project.getName());
            pstmt.setObject(4,project.getDescription());
            pstmt.setObject(5,project.getLead().getDisplayName());
            pstmt.setObject(6,project.getAssigneeType());
            pstmt.setObject(7,project.getUrl());
            if(project.getProjectCategory()!=null) {
                pstmt.setObject(8, project.getProjectCategory().getName());
            }else {
                pstmt.setObject(8,null);
            }
            pstmt.setObject(9,project.isArchived()?1:0);
            pstmt.setObject(10,project.getSelf());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
