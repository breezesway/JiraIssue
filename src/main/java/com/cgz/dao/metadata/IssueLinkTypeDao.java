package com.cgz.dao.metadata;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.metadata.IssueLinkType;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class IssueLinkTypeDao {
    public void insertIssueLinkTypes(List<IssueLinkType> issueLinkTypes) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into issuelinktype(id,name,inward,outward,self) values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(IssueLinkType issueLinkType:issueLinkTypes){
            pstmt.setObject(1,issueLinkType.getId());
            pstmt.setObject(2,issueLinkType.getName());
            pstmt.setObject(3,issueLinkType.getInward());
            pstmt.setObject(4,issueLinkType.getOutward());
            pstmt.setObject(5,issueLinkType.getSelf());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
