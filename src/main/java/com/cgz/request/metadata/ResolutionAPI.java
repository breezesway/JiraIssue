package com.cgz.request.metadata;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.metadata.Resolution;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class ResolutionAPI {
    public static List<Resolution> getResolutions() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/resolution")
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSONObject.parseArray(body, Resolution.class);
    }
}
