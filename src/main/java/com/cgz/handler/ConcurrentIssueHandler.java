package com.cgz.handler;

import com.cgz.bean.issue.*;
import com.cgz.bean.user.User;
import com.cgz.dao.issue.*;
import com.cgz.dao.user.UserDao;
import com.cgz.request.issue.*;
import com.cgz.ui.MyFrame;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentIssueHandler {

    private ExecutorService executorService ;

    private MyFrame myFrame;

    private List<Issue> issues = Collections.synchronizedList(new ArrayList<>());

    private Set<User> users = Collections.synchronizedSet(new HashSet<>());
    private List<RemoteLink> remoteLinks = Collections.synchronizedList(new ArrayList<>());
    private List<Comment> comments = Collections.synchronizedList(new ArrayList<>());
    private List<WorkLog> workLogs = Collections.synchronizedList(new ArrayList<>());

    private List<Attachment> attachments = Collections.synchronizedList(new ArrayList<>());
    private List<IssueLink> issueLinks = Collections.synchronizedList(new ArrayList<>());
    private List<History> histories = Collections.synchronizedList(new ArrayList<>());
    private List<Transition> transitions = Collections.synchronizedList(new ArrayList<>());
    private List<PriorityChanged> priorityChangeds = Collections.synchronizedList(new ArrayList<>());

    public ConcurrentIssueHandler(int threadCount, MyFrame myFrame){
        this.executorService = Executors.newFixedThreadPool(threadCount);
        this.myFrame = myFrame;
    }

    /**
     * 获取并插入一个项目的所有issue
     * @param projectKey 项目的key
     */
    public void insertMutliIssue(String projectKey) throws UnirestException, SQLException {
        IssueAPI issueAPI = new IssueAPI();
        int issueCount = issueAPI.getIssueCount(projectKey);
        myFrame.addJTextAreaInfo(projectKey+"的issue数量为"+issueCount+"个...");
        CountDownLatch cdl = new CountDownLatch(issueCount%1000==0?issueCount/1000:issueCount/1000+1);
        myFrame.addJTextAreaInfo("正在获取issue...");
        for (int i=0;i<issueCount;i+=1000){
            int finalI = i;
            executorService.submit(()->{
                try {
                    Task(cdl,projectKey, finalI,1000);
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
        myFrame.addJTextAreaInfo("issue获取完成...");
        myFrame.addJTextAreaInfo("正在插入...");
        new IssueDao().insertIssues(issues);
        new UserDao().insertUsers(users);
        new AttachmentDao().insertAttachments(attachments);
        new IssueLinkDao().insertIssueLinks(issueLinks);
        new RemoteLinkDao().insertRemoteLinks(remoteLinks);
        new CommentDao().insertComments(comments);
        new WorkLogDao().insertWorkLogs(workLogs);
        new HistoryDao().insertHistories(histories);
        new TransitionDao().insertTransitions(transitions);
        new PriorityChangedDao().insertPriorityChangeds(priorityChangeds);
        myFrame.addJTextAreaInfo("全部issue及其信息插入完成!");
        executorService.shutdown();
    }

    private void Task(CountDownLatch cdl,String projectKey, int startAt, int maxResults) throws UnirestException {
        List<Issue> subIssues = new IssueAPI().getIssues(projectKey, startAt, maxResults);
        Set<User> subUsers = new HashSet<>();
        List<RemoteLink> subRemoteLinks = new ArrayList<>();
        List<Comment> subComments = new ArrayList<>();
        List<WorkLog> subWorkLogs = new ArrayList<>();

        //WatcherAPI watcherAPI = new WatcherAPI();
        //VoteAPI voteAPI = new VoteAPI();
        RemoteLinkAPI remoteLinkAPI = new RemoteLinkAPI();
        CommentAPI commentAPI = new CommentAPI();
        WorkLogAPI workLogAPI = new WorkLogAPI();

        IssueLinkAPI issueLinkAPI = new IssueLinkAPI();

        List<Attachment> subAttachments = new ArrayList<>();
        List<IssueLink> subIssueLinks = new ArrayList<>();
        List<History> subHistories = new ArrayList<>();
        List<Transition> subTransitions = new ArrayList<>();
        List<PriorityChanged> subPriorityChangeds = new ArrayList<>();
        int i=1;
        for (Issue issue:subIssues){
            String issueKey = issue.getKey();

            if(issue.getAssignee()!=null) {
                subUsers.add(issue.getAssignee());
            }
            if(issue.getCreator()!=null) {
                subUsers.add(issue.getCreator());
            }
            if(issue.getReporter()!=null) {
                subUsers.add(issue.getReporter());
            }

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

            subAttachments.addAll(issue.getAttachment());
            subIssueLinks.addAll(issueLinkAPI.getIssueLinks(issue.getIssueLinks()));
            subHistories.addAll(issue.getHistories());
            List<Object> list = ParseUtil.parseTransitionList(issue);
            subTransitions.addAll((List<Transition>) list.get(0));
            subPriorityChangeds.addAll((List<PriorityChanged>) list.get(1));
            System.out.println(Thread.currentThread().getName()+":"+i++);
        }
        System.out.println(Thread.currentThread().getName()+"开始合并");
        issues.addAll(subIssues);

        users.addAll(subUsers);

        remoteLinks.addAll(subRemoteLinks);
        comments.addAll(subComments);
        workLogs.addAll(subWorkLogs);

        issueLinks.addAll(subIssueLinks);
        attachments.addAll(subAttachments);
        histories.addAll(subHistories);
        transitions.addAll(subTransitions);
        priorityChangeds.addAll(subPriorityChangeds);
        cdl.countDown();
        System.out.println(Thread.currentThread().getName()+"执行完成...");
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
        List<Object> list = ParseUtil.parseTransitionList(issue);
        //插入transition表
        List<Transition> transitions =(List<Transition>) list.get(0);
        if(!transitions.isEmpty()){
            new TransitionDao().insertTransitions(transitions);
        }
        //插入prioritychanged表
        List<PriorityChanged> priorityChangeds =(List<PriorityChanged>) list.get(1);
        if(!priorityChangeds.isEmpty()){
            new PriorityChangedDao().insertPriorityChangeds(priorityChangeds);
        }
        System.out.println(issueKey+"已插入!");
    }
}
