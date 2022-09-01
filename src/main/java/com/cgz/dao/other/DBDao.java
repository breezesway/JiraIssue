package com.cgz.dao.other;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DBDao {
    public String[][] getAllDatabase() throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="select * from alldb";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        while (resultSet.next()){
            String database = resultSet.getString("database");
            String description = resultSet.getString("description");
            map.put(database,description);
        }
        String[][] databases = new String[map.size()][2];
        int i=0;
        for (Map.Entry<String,String> entry:map.entrySet()){
            databases[i][0] = entry.getKey();
            databases[i++][1] = entry.getValue();
        }
        return databases;
    }


    public static void createDateBase(String dbName) throws SQLException {
        String sql = "create database "+dbName;
        DruidPooledConnection conn = Database.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.execute();
        pstmt.close();
        conn.close();

    }
}
