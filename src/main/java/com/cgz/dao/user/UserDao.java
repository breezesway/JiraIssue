package com.cgz.dao.user;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.user.User;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insertUser(User user) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="replace into user values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        setValues(pstmt, user);
        pstmt.execute();
        pstmt.close();
        conn.close();
    }

    public void insertUsers(List<User> users) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="replace into user values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(User user:users){
            setValues(pstmt, user);
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }

    private void setValues(PreparedStatement pstmt, User user) throws SQLException {
        pstmt.setObject(1,user.getName());
        pstmt.setObject(2,user.getKey());
        pstmt.setObject(3,user.getDisplayName());
        pstmt.setObject(4,user.isActive()?1:0);
        pstmt.setObject(5,user.getTimeZone());
        pstmt.setObject(6,user.getSelf());
    }
}
