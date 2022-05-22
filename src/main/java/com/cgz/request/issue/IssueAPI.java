package com.cgz.request.issue;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.issue.Issue;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.List;

public class IssueAPI {
    public Issue getIssue(String issueKey){
        String url="https://issues.apache.org/jira/rest/api/2/issue/";
        String body = null;
        try {
            body = Unirest.get(url+issueKey)
                    .header("Accept", "application/json")
                    .queryString("expand","changelog")
                    .asString()
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return ParseUtil.parseIssue(body);
    }

    public List<Issue> getIssues(String projectKey,int startAt,int maxResults){
        String url="https://issues.apache.org/jira/rest/api/2/search";
        HashMap<String, Object> parameters = new HashMap<>();
        if(!"all".equals(projectKey)) {
            parameters.put("jql", "project=" + projectKey);
        }
        parameters.put("fields","*all");
        parameters.put("startAt",startAt);
        parameters.put("maxResults",maxResults);
        parameters.put("expand","changelog");
        String body = null;
        try {
            body = Unirest.get(url)
                    .header("Accept", "application/json")
                    .queryString(parameters)
                    .asString()
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return ParseUtil.parseIssueList(JSONObject.parseObject(body).getJSONArray("issues"));
    }

    public int getIssueCount(String projectKey){
        String url="https://issues.apache.org/jira/rest/api/2/search";
        HashMap<String, Object> parameters = new HashMap<>();
        if(!"all".equals(projectKey)) {
            parameters.put("jql", "project=" + projectKey);
        }
        parameters.put("fields","key");
        parameters.put("maxResults",1);
        String body = null;
        try {
            body = Unirest.get(url)
                    .header("Accept", "application/json")
                    .queryString(parameters)
                    .asString()
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        int total = JSONObject.parseObject(body).getIntValue("total");
        System.out.println(total);
        return total;
    }
}
