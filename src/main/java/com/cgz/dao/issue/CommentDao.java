package com.cgz.dao.issue;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.issue.Attachment;
import com.cgz.bean.issue.Comment;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CommentDao {
    public void insertComments(List<Comment> comments) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert ignore into comment values(?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(Comment comment:comments){
            pstmt.setObject(1,comment.getId());
            pstmt.setObject(2,comment.getBody());
            pstmt.setObject(3,comment.getCreated());
            pstmt.setObject(4,comment.getUpdated());
            pstmt.setObject(5,comment.getAuthor()!=null?comment.getAuthor().getDisplayName():null);
            pstmt.setObject(6,comment.getUpdateAuthor()!=null?comment.getUpdateAuthor().getDisplayName():null);
            pstmt.setObject(7,comment.getIssueKey());
            pstmt.setObject(8,comment.getSelf());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
