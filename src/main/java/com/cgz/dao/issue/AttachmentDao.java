package com.cgz.dao.issue;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.issue.Attachment;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AttachmentDao {
    public void insertAttachments(List<Attachment> attachments) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="replace into attachment values(?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(Attachment attachment:attachments){
            pstmt.setObject(1,attachment.getId());
            pstmt.setObject(2,attachment.getFilename());
            pstmt.setObject(3,attachment.getAuthor().getDisplayName());
            pstmt.setObject(4,attachment.getCreated());
            pstmt.setObject(5,attachment.getSize());
            pstmt.setObject(6,attachment.getMineType());
            pstmt.setObject(7,attachment.getContent());
            pstmt.setObject(8,attachment.getSelf());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
