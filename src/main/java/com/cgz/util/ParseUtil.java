package com.cgz.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.issue.*;
import com.cgz.bean.metadata.IssueLinkType;
import com.cgz.bean.project.Version;
import com.cgz.bean.user.User;

import java.util.ArrayList;
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

        Issue.Progress aggregateProgress = issue.new Progress();
        aggregateProgress.setProgress((int)fields.getJSONObject("aggregateprogress").get("progress"));
        aggregateProgress.setTotal((int)fields.getJSONObject("aggregateprogress").get("total"));
        aggregateProgress.setPercent((float)fields.getJSONObject("aggregateprogress").getIntValue("percent"));
        issue.setAggregateProgress(aggregateProgress);

        Issue.Progress progress = issue.new Progress();
        progress.setProgress((int)fields.getJSONObject("aggregateprogress").get("progress"));
        progress.setTotal((int)fields.getJSONObject("aggregateprogress").get("total"));
        progress.setPercent((float)fields.getJSONObject("aggregateprogress").getIntValue("percent"));
        issue.setProgress(progress);

        Issue.TimeTracking timeTracking = issue.new TimeTracking();
        timeTracking.setRemainingEstimate(fields.getJSONObject("timetracking").getString("remainingEstimate"));
        timeTracking.setTimeSpent(fields.getJSONObject("timetracking").getString("timeSpent"));
        timeTracking.setRemainingEstimateSeconds(fields.getJSONObject("timetracking").getIntValue("remainingEstimateSeconds"));
        timeTracking.setTimeSpentSeconds(fields.getJSONObject("timetracking").getIntValue("timeSpentSeconds"));
        issue.setTimetracking(timeTracking);

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
        issue.setHistories(parseHistoryList(historyArray,jsonObject.getString("key")));

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

    public static List<History> parseHistoryList(JSONArray jsonArray,String issueKey){
        ArrayList<History> histories = new ArrayList<>();
        for (int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            History history = new History();
            history.setId(jsonObject.getString("id"));
            history.setCreated(jsonObject.getString("created"));
            history.setIssueKey(issueKey);
            User user = jsonObject.getJSONObject("author").toJavaObject(User.class);
            history.setAuthor(user);
            JSONArray itemsJSONArray = jsonObject.getJSONArray("items");
            ArrayList<History.Item> items = new ArrayList<>();
            for (int j=0;j<itemsJSONArray.size();j++){
                JSONObject itemJSONObject = itemsJSONArray.getJSONObject(j);
                History.Item item = history.new Item();
                item.setField(itemJSONObject.getString("field"));
                item.setFieldtype(itemJSONObject.getString("fieldtype"));
                item.setFrom(itemJSONObject.getString("from"));
                item.setFromString(itemJSONObject.getString("fromString"));
                item.setTo(itemJSONObject.getString("to"));
                item.setToString(itemJSONObject.getString("toString"));
                items.add(item);
            }
            history.setItems(items);
            histories.add(history);
        }
        return histories;
    }

    public static List<Transition> parseTransitionList(Issue issue){
        List<Transition> transitions = new ArrayList<>();
        List<History> histories = issue.getHistories();
        for (History history:histories){
            for (History.Item item:history.getItems()){
                if("status".equals(item.getField())){
                    Transition transition = new Transition();
                    transition.setAuthorDisplayName(history.getAuthor().getDisplayName());
                    transition.setCreated(history.getCreated());
                    transition.setIssueKey(history.getIssueKey());
                    transition.setFromString(item.getFromString());
                    transition.setToString(item.getToString());
                    transitions.add(transition);
                }
            }
        }
        return transitions;
    }
}
