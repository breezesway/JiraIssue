package com.cgz.request.issue;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.issue.Issue;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class IssueAPI {
    public Issue getIssue(String issueKey) throws UnirestException {
        String url="https://issues.apache.org/jira/rest/api/2/issue/";
        String body = Unirest.get(url+issueKey)
                .header("Accept", "application/json")
                .queryString("expand","changelog")
                .asString()
                .getBody();

        return ParseUtil.parseIssue(body);
    }

    public List<Issue> getIssues(String projectKey, ExecutorService executorService,int threadCount) throws UnirestException {
        System.out.println("正在获取issue的基本信息...");
        String url="https://issues.apache.org/jira/rest/api/2/search";
        String body1 = Unirest.get(url)
                .header("Accept", "application/json")
                .queryString("jql", "project=" + projectKey)
                .queryString("fields", "key")
                .queryString("maxResults", 1)
                .asString()
                .getBody();
        int total = JSONObject.parseObject(body1).getIntValue("total");
        List<Issue> list = Collections.synchronizedList(new ArrayList<>());
        int issueNum = total/threadCount+1;   //每个线程处理的issue数量
        CountDownLatch cdl = new CountDownLatch(threadCount);
        for (int startAt=0;startAt<total;startAt+=issueNum) {
            int finalStartAt = startAt;
            executorService.submit(()-> {
                Task(url,projectKey,list, finalStartAt,issueNum,cdl);
            });
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("已获取"+list.size()+"个issue的基本信息...");
        return list;
    }

    private void Task(String url, String projectKey, List<Issue> list, int startAt,int issueNum, CountDownLatch cdl){
        String body = null;
        try {
            body = Unirest.get(url)
                    .header("Accept", "application/json")
                    .queryString("jql", "project=" + projectKey)
                    .queryString("fields", "*all")
                    .queryString("startAt",startAt)
                    .queryString("maxResults", issueNum)
                    .queryString("expand","changelog")
                    .asString()
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        List<Issue> issues = ParseUtil.parseIssueList(JSONObject.parseObject(body).getJSONArray("issues"));
        list.addAll(issues);
        cdl.countDown();
    }
}
