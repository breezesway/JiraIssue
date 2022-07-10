package com.cgz.request.issue;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.issue.WorkLog;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class WorkLogAPI {
    public List<WorkLog> getWorkLogs(String issueKey){
        String url = "https://issues.apache.org/jira/rest/api/2/issue/"+issueKey+"/worklog";
        String body = null;
        try {
            body = Unirest.get(url)
                    .header("Accept", "application/json")
                    .asString()
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        List<WorkLog> worklogs = JSONObject.parseObject(body).getJSONArray("worklogs").toJavaList(WorkLog.class);
        for (WorkLog workLog:worklogs){
            workLog.setIssueKey(issueKey);
        }
        return worklogs;
    }
}
