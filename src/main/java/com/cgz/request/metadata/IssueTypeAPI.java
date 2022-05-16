package com.cgz.request.metadata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.metadata.IssueType;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class IssueTypeAPI {

    public List<IssueType> getIssueTypes() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/issuetype")
                .header("Accept", "application/json")
                .asString()
                .getBody();

        JSONArray jsonArray = JSONObject.parseArray(body);
        return jsonArray.toJavaList(IssueType.class);
    }
}
