package com.cgz.dao.metadata;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.metadata.Resolution;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ResolutionDao {
    public void insertResolutions(List<Resolution> resolutions) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into resolution(id,name,description,self) values(?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(Resolution resolution:resolutions){
            pstmt.setObject(1,resolution.getId());
            pstmt.setObject(2,resolution.getName());
            pstmt.setObject(3,resolution.getDescription());
            pstmt.setObject(4,resolution.getSelf());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
