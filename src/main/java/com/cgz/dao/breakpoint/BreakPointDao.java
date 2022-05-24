package com.cgz.dao.breakpoint;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BreakPointDao {
    public int getLastBreakPoint(String jql) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="select lastpoint from breakpoint where jql=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,jql);
        ResultSet resultSet = pstmt.executeQuery();
        int lastpoint = 0;
        if(resultSet.next()){
            lastpoint = resultSet.getInt("lastpoint");
        }else {
            insertLastBreakPoint(jql);
        }
        pstmt.close();
        conn.close();
        return lastpoint;
    }

    public void updateLastBreakpoint(int num, String jql){
        try {
            DruidPooledConnection conn = Database.getConnection();
            String sql="update breakpoint set lastpoint=? where jql=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1,num);
            pstmt.setObject(2,jql);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insertLastBreakPoint(String jql) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="insert into breakpoint value (?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setObject(1,0);
        pstmt.setObject(2,jql);
        pstmt.execute();
        pstmt.close();
        conn.close();
    }
}
