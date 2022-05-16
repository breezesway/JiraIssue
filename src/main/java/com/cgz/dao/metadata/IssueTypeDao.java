package com.cgz.dao.metadata;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.metadata.IssueType;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class IssueTypeDao {
    public void insertIssueTypes(List<IssueType> issueTypes) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into issuetype(id,name,description,subtask,avatarId,self,iconUrl) values(?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(IssueType issueType:issueTypes){
            pstmt.setObject(1,issueType.getId());
            pstmt.setObject(2,issueType.getName());
            pstmt.setObject(3,issueType.getDescription());
            if(issueType.isSubtask()){
                pstmt.setObject(4,1);
            }else {
                pstmt.setObject(4,0);
            }
            pstmt.setObject(5,issueType.getAvatarId());
            pstmt.setObject(6,issueType.getSelf());
            pstmt.setObject(7,issueType.getIconUrl());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
