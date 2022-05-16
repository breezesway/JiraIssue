package com.cgz.request.issue;

import com.cgz.bean.issue.Issue;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

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
}
