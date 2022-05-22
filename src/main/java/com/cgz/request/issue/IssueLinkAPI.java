package com.cgz.request.issue;

import com.cgz.bean.issue.IssueLink;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.List;

public class IssueLinkAPI {
    public IssueLink getIssueLink(String linkId){
        String url="https://issues.apache.org/jira/rest/api/2/issueLink/";
        String body = null;
        try {
            body = Unirest.get(url+linkId)
                    .header("Accept", "application/json")
                    .asString()
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return ParseUtil.parseIssueLink(body);
    }

    public List<IssueLink> getIssueLinks(List<String> issueLinkIds){
        List<IssueLink> issueLinks = new ArrayList<>();
        for (String linkId:issueLinkIds){
            issueLinks.add(getIssueLink(linkId));
        }
        return issueLinks;
    }
}
