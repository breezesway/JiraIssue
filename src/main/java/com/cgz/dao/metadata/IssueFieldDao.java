package com.cgz.dao.metadata;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.metadata.IssueField;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class IssueFieldDao {
    public void insertIssueFields(List<IssueField> issueFields) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="replace into issuefield values(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(IssueField issueField:issueFields){
            pstmt.setObject(1,issueField.getId());
            pstmt.setObject(2,issueField.getName());
            //布尔类型转为1或0
            pstmt.setObject(3,issueField.isCustom()?1:0);
            pstmt.setObject(4,issueField.isOrderable()?1:0);
            pstmt.setObject(5,issueField.isNavigable()?1:0);
            pstmt.setObject(6,issueField.isSearchable()?1:0);
            //将字符串数组转为字符串
            pstmt.setObject(7,Arrays.toString(issueField.getClauseNames()));
            IssueField.Schema schema = issueField.getSchema();
            //有些字段可能没有schema,所以判断一下
            if(schema!=null) {
                pstmt.setObject(8, schema.getType());
                pstmt.setObject(9, schema.getSystem());
                pstmt.setObject(10, schema.getCustom());
                pstmt.setObject(11, schema.getCustomId());
            }
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }
}
