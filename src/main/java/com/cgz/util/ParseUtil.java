package com.cgz.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.issue.*;
import com.cgz.bean.metadata.IssueLinkType;
import com.cgz.bean.project.Version;
import com.cgz.bean.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParseUtil {
    public static Issue parseIssue(String body){
        Issue issue = new Issue();
        JSONObject jsonObject = JSONObject.parseObject(body);
        issue.setId((String) jsonObject.get("id"));
        issue.setKey((String) jsonObject.get("key"));
        issue.setSelf((String) jsonObject.get("self"));
        JSONObject fields = jsonObject.getJSONObject("fields");
        issue.setLastViewed((String) fields.get("lastViewed"));
        issue.setResolutionDate((String) fields.get("resolutiondate"));
        issue.setCreated((String) fields.get("created"));
        issue.setUpdated((String) fields.get("updated"));
        issue.setWorkRatio(((Integer) fields.get("workratio")).floatValue());
        issue.setSummary((String) fields.get("summary"));
        issue.setDescription((String) fields.get("description"));
        issue.setEnvironment((String) fields.get("environment"));
        issue.setDuedate((String) fields.get("duedate"));

        issue.setPriority((String) fields.getJSONObject("priority").get("name"));
        issue.setStatus((String) fields.getJSONObject("status").get("name"));
        issue.setResolution((String) fields.getJSONObject("resolution").get("name"));
        issue.setIssueType((String) fields.getJSONObject("issuetype").get("name"));
        issue.setProject((String) fields.getJSONObject("project").get("key"));

        issue.setAssignee(parseUser(fields.getJSONObject("assignee").toJSONString()));
        issue.setCreator(parseUser(fields.getJSONObject("creator").toJSONString()));
        issue.setReporter(parseUser(fields.getJSONObject("reporter").toJSONString()));

        issue.setTimeEstimate(fields.getIntValue("timeestimate"));
        issue.setAggregateTimeOriginalEstimate(fields.getIntValue("aggregatetimeoriginalestimate"));
        issue.setAggregateTimeEstimate(fields.getIntValue("aggregatetimeestimate"));
        issue.setTimeOriginalEstimate(fields.getIntValue("timeoriginalestimate"));
        issue.setTimeSpent(fields.getIntValue("timespent"));
        issue.setAggregateTimeSpent(fields.getIntValue("aggregatetimespent"));
        issue.setAggregateProgress(fields.getJSONObject("aggregateprogress").toJavaObject(Issue.Progress.class));
        issue.setProgress(fields.getJSONObject("progress").toJavaObject(Issue.Progress.class));

        ArrayList<String> labels = new ArrayList<>();
        for (Object object:fields.getJSONArray("labels")){
            labels.add((String) object);
        }
        issue.setLabels(labels);
        issue.setFixVersions(fields.getJSONArray("fixVersions").toJavaList(Version.class));
        issue.setVersions(fields.getJSONArray("versions").toJavaList(Version.class));
        issue.setComponents(fields.getJSONArray("components").toJavaList(Component.class));
        issue.setAttachment(fields.getJSONArray("attachment").toJavaList(Attachment.class));

        JSONArray jsonArray1 = fields.getJSONArray("subtasks");
        List<String> subtasks = new ArrayList<>();
        for (int i=0;i<jsonArray1.size();i++){
            subtasks.add((String) jsonArray1.getJSONObject(i).get("key"));
        }
        issue.setSubtasks(subtasks);
        if(fields.containsKey("parent")){
            issue.setParent((String) fields.getJSONObject("parent").get("key"));
        }

        JSONArray jsonArray2 = fields.getJSONArray("issuelinks");
        ArrayList<String> issueLinks = new ArrayList<>();
        for (int i=0;i<jsonArray2.size();i++){
            issueLinks.add((String) jsonArray2.getJSONObject(i).get("id"));
        }
        issue.setIssueLinks(issueLinks);

        JSONArray historyArray = jsonObject.getJSONObject("changelog").getJSONArray("histories");
        List<History> histories = historyArray.toJavaList(History.class);
        issue.setHistories(histories);

        return issue;
    }

    public static User parseUser(String body){
        JSONObject jsonObject = JSONObject.parseObject(body);
        User user = new User();
        user.setKey((String) jsonObject.get("key"));
        user.setName((String) jsonObject.get("name"));
        user.setDisplayName((String) jsonObject.get("displayName"));
        user.setActive((boolean) jsonObject.get("active"));
        if(jsonObject.containsKey("timeZone")){
            user.setTimeZone((String) jsonObject.get("timeZone"));
        }
        user.setSelf((String) jsonObject.get("self"));
        return user;
    }

    public static List<User> parseUserList(JSONArray jsonArray){
        List<User> users = new ArrayList<>();
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            User user = ParseUtil.parseUser(jsonObject.toJSONString());
            users.add(user);
        }
        return users;
    }

    public static List<RemoteLink> parseRemoteLinkList(JSONArray jsonArray){
        ArrayList<RemoteLink> remoteLinks = new ArrayList<>();
        for (int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            RemoteLink remoteLink = new RemoteLink();
            remoteLink.setId(jsonObject.getIntValue("id"));
            if(jsonObject.containsKey("globalId")){
                remoteLink.setGlobalId((String) jsonObject.get("globalId"));
            }
            remoteLink.setSelf((String) jsonObject.get("self"));
            remoteLink.setRemoteObjectUrl((String) jsonObject.getJSONObject("object").get("url"));
            remoteLink.setRemoteObjectTitle((String) jsonObject.getJSONObject("object").get("title"));
            remoteLinks.add(remoteLink);
        }
        return remoteLinks;
    }

    public static IssueLink parseIssueLink(String body){
        JSONObject jsonObject = JSONObject.parseObject(body);
        IssueLink issueLink = new IssueLink();
        issueLink.setId((String) jsonObject.get("id"));
        issueLink.setSelf((String) jsonObject.get("self"));
        issueLink.setType(jsonObject.getObject("type", IssueLinkType.class));
        issueLink.setInwardIssueKey((String) jsonObject.getJSONObject("inwardIssue").get("key"));
        issueLink.setOutwardIssueKey((String) jsonObject.getJSONObject("outwardIssue").get("key"));
        return issueLink;
    }

    public static List<Comment> parseCommentList(String body,String issueKey){
        List<Comment> comments = JSONObject.parseObject(body).getJSONArray("comments").toJavaList(Comment.class);
        for (Comment comment:comments){
            comment.setIssueKey(issueKey);
        }
        return comments;
    }
}
