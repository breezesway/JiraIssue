package com.cgz.handler;

import com.cgz.bean.issue.*;
import com.cgz.bean.user.User;
import com.cgz.dao.breakpoint.BreakPointDao;
import com.cgz.dao.issue.*;
import com.cgz.dao.user.UserDao;
import com.cgz.request.issue.*;
import com.cgz.ui.MyFrame;
import com.cgz.util.ParseUtil;

import java.sql.SQLException;
import java.util.*;

public class IssueHandler {

    private MyFrame myFrame;

    private IssueAPI issueAPI = new IssueAPI();
    private RemoteLinkAPI remoteLinkAPI = new RemoteLinkAPI();
    private CommentAPI commentAPI = new CommentAPI();
    private WorkLogAPI workLogAPI = new WorkLogAPI();
    private IssueLinkAPI issueLinkAPI = new IssueLinkAPI();

    private List<Issue> issues = new ArrayList<>();

    private Set<User> users = new HashSet<>();
    private List<RemoteLink> remoteLinks = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    private List<WorkLog> workLogs = new ArrayList<>();

    private List<Attachment> attachments = new ArrayList<>();
    private List<IssueLink> issueLinks = new ArrayList<>();
    private List<History> histories = new ArrayList<>();
    private List<Transition> transitions = new ArrayList<>();

    public IssueHandler(MyFrame myFrame){
        this.myFrame = myFrame;
    }

    public void insertIssuesByProject(String projectKey){
        int issueCount = issueAPI.getIssueCount(projectKey);
        myFrame.addJTextAreaInfo(projectKey+"的issue数量为"+issueCount+"个...");
        System.out.println(issueCount);
        myFrame.addJTextAreaInfo("正在获取"+projectKey+"的issue...");
        for (int i=0;i<issueCount;i+=1000) {
            issues.addAll(issueAPI.getIssues(projectKey, i, 1000));
        }
        int n=1;
        for (Issue issue:issues){
            getIssueInfo(issue);
            if(n%100==0) {
                myFrame.addJTextAreaInfo(projectKey + "已获取" + n + "个issue...");
            }
            n++;
        }
        myFrame.addJTextAreaInfo(projectKey+"的issue获取完成...");
        myFrame.addJTextAreaInfo(projectKey+"的issue正在插入mysql...");
        insertIssueInfo();
        myFrame.addJTextAreaInfo(projectKey+"的全部issue及其信息插入完成!");
    }

    public void insertIssuesByCount(int startAt, int issueCount){
        BreakPointDao breakPointDao = new BreakPointDao();
        if (startAt==0){
            try {
                startAt = breakPointDao.getLastBreakPoint();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(issueCount==0){
            issueCount = issueAPI.getIssueCount("all");
        }
        myFrame.addJTextAreaInfo("要获取的issue数量为"+issueCount+"个...");
        myFrame.addJTextAreaInfo("正在获取issue...");
        int onceIssueCount = 500;
        for (int i=0;i<issueCount;i+=onceIssueCount) {
            long stime = System.currentTimeMillis();
            issues.addAll(issueAPI.getIssues("all", startAt+i, onceIssueCount));
            int n=1;
            for (Issue issue:issues){
                getIssueInfo(issue);
                if(n%10==0) {
                    myFrame.addJTextAreaInfo("已获取" + n + "个issue...");
                    System.out.println(n);
                }
                n++;
            }
            myFrame.addJTextAreaInfo("issue获取完成...");
            myFrame.addJTextAreaInfo("issue正在插入mysql...");
            insertIssueInfo();
            breakPointDao.updateLastBreakpoint(startAt+i);
            long etime = System.currentTimeMillis();
            System.out.println((etime-stime)/60000);
        }
        myFrame.addJTextAreaInfo("已全部完成!");
    }

    private void getIssueInfo(Issue issue){
        String issueKey = issue.getKey();

        if(issue.getAssignee()!=null) {
            users.add(issue.getAssignee());
        }
        if(issue.getCreator()!=null) {
            users.add(issue.getCreator());
        }
        if(issue.getReporter()!=null) {
            users.add(issue.getReporter());
        }

        //List<User> issueWatchers = watcherAPI.getWatchers(issueKey);
        //List<User> issueVoters = voteAPI.getVoters(issueKey);
        List<RemoteLink> issueRemoteLinks = remoteLinkAPI.getRemoteLinks(issueKey);
        List<Comment> issueComments = commentAPI.getComments(issueKey);
        List<WorkLog> issueWorkLogs = workLogAPI.getWorkLogs(issueKey);

        //subUsers.addAll(issueWatchers);
        //subUsers.addAll(issueVoters);
        remoteLinks.addAll(issueRemoteLinks);
        comments.addAll(issueComments);
        workLogs.addAll(issueWorkLogs);

        //issue.setWatchers(issueWatchers);
        //issue.setVoters(issueVoters);
        issue.setRemoteLinks(issueRemoteLinks);
        issue.setComments(issueComments);
        issue.setWorklog(issueWorkLogs);

        attachments.addAll(issue.getAttachment());
        issueLinks.addAll(issueLinkAPI.getIssueLinks(issue.getIssueLinks()));
        histories.addAll(issue.getHistories());
        transitions.addAll(ParseUtil.parseTransitionList(issue));
    }

    private void insertIssueInfo() {
        try {
            new IssueDao().insertIssues(issues);
            new UserDao().insertUsers(users);
            new AttachmentDao().insertAttachments(attachments);
            new IssueLinkDao().insertIssueLinks(issueLinks);
            new RemoteLinkDao().insertRemoteLinks(remoteLinks);
            new CommentDao().insertComments(comments);
            new WorkLogDao().insertWorkLogs(workLogs);
            new HistoryDao().insertHistories(histories);
            new TransitionDao().insertTransitions(transitions);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        clearCollection();
    }

    private void clearCollection(){
        issues.clear();
        users.clear();
        remoteLinks.clear();
        comments.clear();
        workLogs.clear();
        histories.clear();
        transitions.clear();
    }
}
