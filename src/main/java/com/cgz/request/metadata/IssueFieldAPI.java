package com.cgz.request.metadata;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.metadata.IssueField;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class IssueFieldAPI {
    public List<IssueField> getIssueFields() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/field")
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSONObject.parseArray(body, IssueField.class);
    }
}
