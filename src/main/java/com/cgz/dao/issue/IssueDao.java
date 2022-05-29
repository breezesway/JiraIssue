package com.cgz.dao.issue;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cgz.bean.issue.*;
import com.cgz.bean.project.Component;
import com.cgz.bean.project.Version;
import com.cgz.dao.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IssueDao {
    public void insertIssue(Issue issue) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        //MySql的重复插入：https://blog.csdn.net/sunny243788557/article/details/109603274
        String sql="replace into issue values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        setValues(pstmt,issue);
        pstmt.execute();
        pstmt.close();
        conn.close();
    }

    public void insertIssues(List<Issue> issues) throws SQLException {
        DruidPooledConnection conn = Database.getConnection();
        String sql="replace into issue values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (Issue issue:issues){
            setValues(pstmt,issue);
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.clearBatch();
        pstmt.close();
        conn.close();
    }

    private static void setValues(PreparedStatement pstmt,Issue issue) throws SQLException {
        pstmt.setObject(1,issue.getId());
        pstmt.setObject(2,issue.getKey());
        pstmt.setObject(3,issue.getSelf());
        pstmt.setObject(4,issue.getLastViewed());
        pstmt.setObject(5,issue.getResolutionDate());
        pstmt.setObject(6,issue.getCreated());
        pstmt.setObject(7,issue.getUpdated());
        pstmt.setObject(8,issue.getWorkRatio());
        pstmt.setObject(9,issue.getSummary());
        pstmt.setObject(10,issue.getDescription());
        pstmt.setObject(11,issue.getEnvironment());
        pstmt.setObject(12,issue.getDuedate());

        pstmt.setObject(13,issue.getPriority());
        pstmt.setObject(14,issue.getStatus());
        pstmt.setObject(15,issue.getResolution());
        pstmt.setObject(16,issue.getIssueType());
        pstmt.setObject(17,issue.getProject());

        pstmt.setObject(18,issue.getAssignee()!=null?issue.getAssignee().getDisplayName():null);
        pstmt.setObject(19,issue.getCreator()!=null?issue.getCreator().getDisplayName():null);
        pstmt.setObject(20,issue.getReporter()!=null?issue.getReporter().getDisplayName():null);

        pstmt.setObject(21,issue.getTimeEstimate());
        pstmt.setObject(22,issue.getAggregateTimeOriginalEstimate());
        pstmt.setObject(23,issue.getAggregateTimeEstimate());
        pstmt.setObject(24,issue.getTimeOriginalEstimate());
        pstmt.setObject(25,issue.getTimeSpent());
        pstmt.setObject(26,issue.getAggregateTimeSpent());

        pstmt.setObject(27,issue.getAggregateProgress()!=null ? issue.getAggregateProgress().getProgress() : null);
        pstmt.setObject(28,issue.getAggregateProgress()!=null ?issue.getAggregateProgress().getTotal() : null);
        pstmt.setObject(29,issue.getAggregateProgress()!=null ?issue.getAggregateProgress().getPercent() : null);
        pstmt.setObject(30,issue.getProgress()!=null ? issue.getProgress().getProgress() : null);
        pstmt.setObject(31,issue.getProgress()!=null ? issue.getProgress().getTotal() : null);
        pstmt.setObject(32,issue.getProgress()!=null ? issue.getProgress().getPercent() : null);
        pstmt.setObject(33,issue.getTimetracking()!=null ? issue.getTimetracking().getRemainingEstimate() : null);
        pstmt.setObject(34,issue.getTimetracking()!=null ? issue.getTimetracking().getTimeSpent() : null);
        pstmt.setObject(35,issue.getTimetracking()!=null ? issue.getTimetracking().getRemainingEstimateSeconds() : null);
        pstmt.setObject(36,issue.getTimetracking()!=null ? issue.getTimetracking().getTimeSpentSeconds() : null);

        pstmt.setObject(37,issue.getLabels()!=null?issue.getLabels().toString():null);

        ArrayList<String> list = new ArrayList<>();
        for (Version version:issue.getFixVersions()){
            list.add(version.getId());
        }
        pstmt.setObject(38,list.toString());
        list.clear();

        for (Version version:issue.getVersions()){
            list.add(version.getId());
        }
        pstmt.setObject(39,list.toString());
        list.clear();

        pstmt.setObject(40,issue.getWatchCount());
        pstmt.setObject(41,issue.getVotesCount());
        /*for (User watcher:issue.getWatchers()){
            list.add(watcher.getDisplayName());
        }
        pstmt.setObject(40,list.toString());
        list.clear();

        for (User voter:issue.getVoters()){
            list.add(voter.getDisplayName());
        }
        pstmt.setObject(41,list.toString());
        list.clear();*/

        for (Component component:issue.getComponents()){
            list.add(component.getId());
        }
        pstmt.setObject(42,list.toString());
        list.clear();

        for (Attachment attachment:issue.getAttachment()){
            list.add(attachment.getId());
        }
        pstmt.setObject(43,list.toString());
        list.clear();

        pstmt.setObject(44,issue.getSubtasks()!=null?issue.getSubtasks().toString():null);
        pstmt.setObject(45,issue.getParent());

        pstmt.setObject(46,issue.getIssueLinks()!=null?issue.getIssueLinks().toString():null);

        for (RemoteLink remoteLink:issue.getRemoteLinks()){
            list.add(String.valueOf(remoteLink.getId()));
        }
        pstmt.setObject(47,list.toString());
        list.clear();

        for (Comment comment:issue.getComments()){
            list.add(comment.getId());
        }
        pstmt.setObject(48,list.toString());
        list.clear();

        for (WorkLog workLog:issue.getWorklog()){
            list.add(workLog.getId());
        }
        pstmt.setObject(49,list.toString());
        list.clear();

        for (History history:issue.getHistories()){
            list.add(history.getId());
        }
        pstmt.setObject(50,list.toString());
        list.clear();
    }
}
