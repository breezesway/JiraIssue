package com.cgz.request.issue;

import com.cgz.bean.issue.IssueLink;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class IssueLinkAPI {
    public static IssueLink getIssueLink(String linkId) throws UnirestException {
        String url="https://issues.apache.org/jira/rest/api/2/issueLink/";
        String body = Unirest.get(url+linkId)
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return ParseUtil.parseIssueLink(body);
    }
}
