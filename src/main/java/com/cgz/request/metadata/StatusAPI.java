package com.cgz.request.metadata;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.metadata.Status;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class StatusAPI {
    public List<Status> getStatuses() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/status")
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSONObject.parseArray(body, Status.class);
    }
}
