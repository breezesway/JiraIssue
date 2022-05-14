package com.cgz.request.issue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.issue.Comment;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class CommentAPI {
    public static List<Comment> getComments(String issueKey) throws UnirestException {
        String url="https://issues.apache.org/jira/rest/api/2/issue/"+issueKey+"/comment";
        String body = Unirest.get(url)
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return ParseUtil.parseCommentList(body,issueKey);
    }
}
