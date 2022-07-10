package com.cgz.dao.issue;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.issue.Attachment;
import com.cgz.bean.issue.RemoteLink;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RemoteLinkDao {
    public void insertRemoteLinks(List<RemoteLink> remoteLinks) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert ignore into remotelink values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(RemoteLink remoteLink:remoteLinks){
            pstmt.setObject(1,remoteLink.getId());
            pstmt.setObject(2,remoteLink.getGlobalId());
            pstmt.setObject(3,remoteLink.getRemoteObjectUrl());
            pstmt.setObject(4,remoteLink.getRemoteObjectTitle());
            pstmt.setObject(5,remoteLink.getSelf());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
