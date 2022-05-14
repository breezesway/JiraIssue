package com.cgz.request.issue;

import com.cgz.bean.issue.Comment;
import com.cgz.bean.issue.Issue;
import com.cgz.bean.issue.RemoteLink;
import com.cgz.bean.issue.WorkLog;
import com.cgz.bean.user.User;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class IssueAPI {
    public static Issue getIssue(String issueKey) throws UnirestException {
        String url="https://issues.apache.org/jira/rest/api/2/issue/";
        String body = Unirest.get(url+issueKey)
                .header("Accept", "application/json")
                .queryString("expand","changelog")
                .asString()
                .getBody();

        Issue issue = ParseUtil.parseIssue(body);

        List<User> watchers = WatcherAPI.getWatchers(issueKey);
        List<User> voters = VoteAPI.getVoters(issueKey);
        List<RemoteLink> remoteLinks = RemoteLinkAPI.getRemoteLinks(issueKey);
        List<Comment> comments = CommentAPI.getComments(issueKey);
        List<WorkLog> workLogs = WorkLogAPI.getWorkLogs(issueKey);
        issue.setWatchers(watchers);
        issue.setVoters(voters);
        issue.setRemoteLinks(remoteLinks);
        issue.setComments(comments);
        issue.setWorklog(workLogs);

        return issue;

    }
}
