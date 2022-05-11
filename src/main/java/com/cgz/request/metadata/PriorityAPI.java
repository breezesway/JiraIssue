package com.cgz.request.metadata;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.metadata.Priority;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class PriorityAPI {
    public static List<Priority> getPriorities() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/priority")
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSONObject.parseArray(body,Priority.class);
    }
}
