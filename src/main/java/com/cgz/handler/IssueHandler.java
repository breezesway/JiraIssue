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
    private List<PriorityChanged> priorityChangeds = new ArrayList<>();

    public IssueHandler(MyFrame myFrame){
        this.myFrame = myFrame;
    }

    public void insertIssuesByCount(String jql, int startAt, int issueCount){
        if("".equals(jql)||jql==null){
            jql = "all";
        }
        BreakPointDao breakPointDao = new BreakPointDao();
        if (startAt==-1){
            try {
                startAt = breakPointDao.getLastBreakPoint(jql);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(issueCount==0){
            issueCount = issueAPI.getIssueCount(jql)-startAt;
        }
        myFrame.addJTextAreaInfo("要获取的issue数量为"+issueCount+"个...");
        myFrame.addJTextAreaInfo("正在获取issue...");
        int onceIssueCount = 200;
        int n=0;
        long stime = System.currentTimeMillis();
        for (int i=0;i<issueCount;i+=onceIssueCount) {
            issues.addAll(issueAPI.getIssues(jql, startAt+i, onceIssueCount));
            for (Issue issue:issues){
                getIssueInfo(issue);
                n++;
                if(n%10==0) {
                    myFrame.addJTextAreaInfo("已获取" + n + "个issue...");
                }
                long etime = System.currentTimeMillis();
                if((etime-stime)/60000>180){
                    return;
                    /*myFrame.addJTextAreaInfo("暂停一会...");
                    try {
                        Thread.sleep(1000*60*180);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myFrame.addJTextAreaInfo("继续获取...");
                    stime = System.currentTimeMillis();*/
                }
            }
            myFrame.addJTextAreaInfo("已获取" + n + "个issue...");
            myFrame.addJTextAreaInfo("issue获取完成...");
            myFrame.addJTextAreaInfo("插入mysql...");
            insertIssueInfo();
            breakPointDao.updateLastBreakpoint(startAt+n,jql);
        }
        myFrame.addJTextAreaInfo("已全部完成!");
    }

    private void getIssueInfo(Issue issue){
        String issueKey = issue.getKey();
        //System.out.println(issueKey);

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

        if(issue.getAttachment()!=null) {
            attachments.addAll(issue.getAttachment());
        }
        issueLinks.addAll(issueLinkAPI.getIssueLinks(issue.getIssueLinks()));
        histories.addAll(issue.getHistories());
        List<Object> list = ParseUtil.parseTransitionList(issue);
        transitions.addAll((List<Transition>)list.get(0));
        priorityChangeds.addAll((List<PriorityChanged>)list.get(1));
    }

    private void insertIssueInfo() {
        try {
            if(!issues.isEmpty()) {
                new IssueDao().insertIssues(issues);
            }
            if(!users.isEmpty()) {
                new UserDao().insertUsers(users);
            }
            if(!attachments.isEmpty()) {
                new AttachmentDao().insertAttachments(attachments);
            }
            if(!issueLinks.isEmpty()) {
                new IssueLinkDao().insertIssueLinks(issueLinks);
            }
            if(!remoteLinks.isEmpty()) {
                new RemoteLinkDao().insertRemoteLinks(remoteLinks);
            }
            if(!comments.isEmpty()) {
                new CommentDao().insertComments(comments);
            }
            if(!workLogs.isEmpty()) {
                new WorkLogDao().insertWorkLogs(workLogs);
            }
            if(!histories.isEmpty()) {
                new HistoryDao().insertHistories(histories);
            }
            if(!transitions.isEmpty()) {
                new TransitionDao().insertTransitions(transitions);
            }
            if(!priorityChangeds.isEmpty()) {
                new PriorityChangedDao().insertPriorityChangeds(priorityChangeds);
            }
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
