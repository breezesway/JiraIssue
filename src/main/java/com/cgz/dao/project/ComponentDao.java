package com.cgz.dao.project;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.issue.Component;
import com.cgz.bean.project.Version;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ComponentDao {
    public static void insertComponents(List<Component> components) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into component values(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (Component component:components){
            pstmt.setObject(1,component.getId());
            pstmt.setObject(2,component.getName());
            pstmt.setObject(3,component.getDescription());
            pstmt.setObject(4, component.getLead()!=null ? component.getLead().getDisplayName() : null);
            pstmt.setObject(5,component.getAssigneeType());
            pstmt.setObject(6,component.getRealAssigneeType());
            pstmt.setObject(7,component.isAssigneeTypeValid()?1:0);
            pstmt.setObject(8,component.getProject());
            pstmt.setObject(9,component.getProjectId());
            pstmt.setObject(10,component.isArchived()?1:0);
            pstmt.setObject(11,component.getSelf());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
