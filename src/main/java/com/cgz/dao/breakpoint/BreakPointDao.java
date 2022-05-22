package com.cgz.dao.breakpoint;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BreakPointDao {
    public int getLastBreakPoint() throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="select lastpoint from breakpoint";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        int lastpoint = 0;
        if(resultSet.next()){
            lastpoint = resultSet.getInt("lastpoint");
        }else {
            insertLastBreakPoint();
        }
        pstmt.close();
        conn.close();
        return lastpoint;
    }

    public void updateLastBreakpoint(int num){
        try {
            DruidPooledConnection conn = Database.getConnection();
            String sql="update breakpoint set lastpoint=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1,num);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insertLastBreakPoint() throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="intsert into breakpoint value (0)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.execute();
        pstmt.close();
        conn.close();
    }
}
