package com.cgz.dao.project;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.project.Version;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class VersionDao {
    public void insertVersions(List<Version> versions) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="replace into version values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (Version version:versions){
            pstmt.setObject(1,version.getId());
            pstmt.setObject(2,version.getName());
            pstmt.setObject(3,version.getDescription());
            pstmt.setObject(4,version.isArchived()?1:0);
            pstmt.setObject(5,version.isReleased()?1:0);
            pstmt.setObject(6,version.getReleaseDate());
            pstmt.setObject(7,version.getUserReleaseDate());
            pstmt.setObject(8,version.getProjectId());
            pstmt.setObject(9,version.getSelf());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
