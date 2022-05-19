package com.cgz.handler;

import com.cgz.bean.issue.*;
import com.cgz.bean.user.User;
import com.cgz.dao.issue.*;
import com.cgz.dao.user.UserDao;
import com.cgz.request.issue.*;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IssueHandler {

    public int threadCount = 1;
    public ExecutorService executorService ;

    public IssueHandler(){}
    public IssueHandler(int threadCount){
        this.threadCount = threadCount;
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * 获取并插入一个项目的所有issue
     * @param projectKey 项目的key
     */
    public void insertMutliIssue(String projectKey) throws UnirestException, SQLException {
        List<Issue> issues = new IssueAPI().getIssues(projectKey, executorService, threadCount);
        System.out.println("正在获取issue的详细信息...");
        int issueNum = issues.size()/threadCount+1;  //每个线程需要处理的issue数量
        ArrayList<List<Issue>> sublistList = new ArrayList<>();
        for (int i=0;i<issues.size();i+=issueNum){
            List<Issue> subList = issues.subList(i, Math.min(i + issueNum, issues.size()));
            sublistList.add(subList);
        }
        List<User> users = new ArrayList<>();
        List<RemoteLink> remoteLinks = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        List<WorkLog> workLogs = new ArrayList<>();

        List<Attachment> attachments = new ArrayList<>();
        List<IssueLink> issueLinks = new ArrayList<>();
        List<History> histories = new ArrayList<>();
        List<Transition> transitions = new ArrayList<>();

        CountDownLatch cdl = new CountDownLatch(threadCount);
        for (List<Issue> list:sublistList){
            executorService.submit(()->{
                try {
                    Task(cdl,list,users,remoteLinks,comments,workLogs,issueLinks,histories,transitions);
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("已获取"+issues.size()+"个issue的详细信息...");
        System.out.println("正在插入数据库...");
        new IssueDao().insertIssues(issues);
        new UserDao().insertUsers(users);
        new AttachmentDao().insertAttachments(attachments);
        new IssueLinkDao().insertIssueLinks(issueLinks);
        new RemoteLinkDao().insertRemoteLinks(remoteLinks);
        new CommentDao().insertComments(comments);
        new WorkLogDao().insertWorkLogs(workLogs);
        new HistoryDao().insertHistories(histories);
        new TransitionDao().insertTransitions(transitions);

        System.out.println(issues.size()+"个issue已插入!");
        executorService.shutdown();
    }

    private void Task(CountDownLatch cdl,List<Issue> issues, List<User> users, List<RemoteLink> remoteLinks, List<Comment> comments ,List<WorkLog> workLogs,List<IssueLink> issueLinks,List<History> histories,List<Transition> transitions) throws UnirestException {
        List<User> subUsers = new ArrayList<>();
        List<RemoteLink> subRemoteLinks = new ArrayList<>();
        List<Comment> subComments = new ArrayList<>();
        List<WorkLog> subWorkLogs = new ArrayList<>();

        //WatcherAPI watcherAPI = new WatcherAPI();
        //VoteAPI voteAPI = new VoteAPI();
        RemoteLinkAPI remoteLinkAPI = new RemoteLinkAPI();
        CommentAPI commentAPI = new CommentAPI();
        WorkLogAPI workLogAPI = new WorkLogAPI();

        IssueLinkAPI issueLinkAPI = new IssueLinkAPI();

        List<Attachment> attachments = new ArrayList<>();
        List<IssueLink> subIssueLinks = new ArrayList<>();
        List<History> subHistories = new ArrayList<>();
        List<Transition> subTransitions = new ArrayList<>();
        for (Issue issue:issues){
            String issueKey = issue.getKey();

            subUsers.add(issue.getAssignee());
            subUsers.add(issue.getCreator());
            subUsers.add(issue.getReporter());

            //List<User> issueWatchers = watcherAPI.getWatchers(issueKey);
            //List<User> issueVoters = voteAPI.getVoters(issueKey);
            List<RemoteLink> issueRemoteLinks = remoteLinkAPI.getRemoteLinks(issueKey);
            List<Comment> issueComments = commentAPI.getComments(issueKey);
            List<WorkLog> issueWorkLogs = workLogAPI.getWorkLogs(issueKey);

            //subUsers.addAll(issueWatchers);
            //subUsers.addAll(issueVoters);
            subRemoteLinks.addAll(issueRemoteLinks);
            subComments.addAll(issueComments);
            subWorkLogs.addAll(issueWorkLogs);

            //issue.setWatchers(issueWatchers);
            //issue.setVoters(issueVoters);
            issue.setRemoteLinks(issueRemoteLinks);
            issue.setComments(issueComments);
            issue.setWorklog(issueWorkLogs);

            attachments.addAll(issue.getAttachment());
            subIssueLinks.addAll(issueLinkAPI.getIssueLinks(issue.getIssueLinks()));
            subHistories.addAll(issue.getHistories());
            subTransitions.addAll(ParseUtil.parseTransitionList(issue));
            System.out.println(issueKey);
        }
        users.addAll(subUsers);
        remoteLinks.addAll(subRemoteLinks);
        comments.addAll(subComments);
        workLogs.addAll(subWorkLogs);
        issueLinks.addAll(subIssueLinks);
        histories.addAll(subHistories);
        transitions.addAll(subTransitions);
        cdl.countDown();
    }

    /**
     * 获取并插入单个issue
     * @param issueKey 要插入的issue的key
     */
    public void insertSingleIssue(String issueKey) throws UnirestException, SQLException {
        Issue issue = new IssueAPI().getIssue(issueKey);
        //获取其他字段详细信息
        //List<User> watchers = new WatcherAPI().getWatchers(issueKey);
        //List<User> voters = new VoteAPI().getVoters(issueKey);
        List<RemoteLink> remoteLinks = new RemoteLinkAPI().getRemoteLinks(issueKey);
        List<Comment> comments = new CommentAPI().getComments(issueKey);
        List<WorkLog> workLogs = new WorkLogAPI().getWorkLogs(issueKey);

        List<IssueLink> issueLinks = new IssueLinkAPI().getIssueLinks(issue.getIssueLinks());

        //issue.setWatchers(watchers);
        //issue.setVoters(voters);
        issue.setRemoteLinks(remoteLinks);
        issue.setComments(comments);
        issue.setWorklog(workLogs);

        //插入issue表
        new IssueDao().insertIssue(issue);
        //插入user表
        UserDao userDao = new UserDao();
        if(issue.getAssignee()!=null) {
            userDao.insertUser(issue.getAssignee());
        }
        if(issue.getCreator()!=null) {
            userDao.insertUser(issue.getCreator());
        }
        if(issue.getReporter()!=null) {
            userDao.insertUser(issue.getReporter());
        }
        /*if(!watchers.isEmpty()){
            userDao.insertUsers(watchers);
        }
        if(!voters.isEmpty()){
            userDao.insertUsers(voters);
        }*/
        //插入attachment表
        if(!issue.getAttachment().isEmpty()) {
            new AttachmentDao().insertAttachments(issue.getAttachment());
        }
        //插入issuelink表
        if(!issueLinks.isEmpty()){
            new IssueLinkDao().insertIssueLinks(issueLinks);
        }
        //插入remotelink表
        if (!remoteLinks.isEmpty()){
            new RemoteLinkDao().insertRemoteLinks(remoteLinks);
        }
        //插入comment表
        if(!comments.isEmpty()){
            new CommentDao().insertComments(comments);
        }
        //插入worklog表
        if(!workLogs.isEmpty()){
            new WorkLogDao().insertWorkLogs(workLogs);
        }
        //插入history表
        if(!issue.getHistories().isEmpty()){
            new HistoryDao().insertHistories(issue.getHistories());
        }
        //插入transition表
        List<Transition> transitions = ParseUtil.parseTransitionList(issue);
        if(!transitions.isEmpty()){
            new TransitionDao().insertTransitions(transitions);
        }
        System.out.println(issueKey+"已插入!");
    }
}
