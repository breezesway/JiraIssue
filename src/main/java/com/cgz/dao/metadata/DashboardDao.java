package com.cgz.dao.metadata;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.metadata.Dashboard;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DashboardDao {
    public void insertDashboards(List<Dashboard> dashboards) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="replace into dashboard(id,name,self,view) values(?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(Dashboard dashboard:dashboards){
            pstmt.setObject(1,dashboard.getId());
            pstmt.setObject(2,dashboard.getName());
            pstmt.setObject(3,dashboard.getSelf());
            pstmt.setObject(4,dashboard.getView());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
