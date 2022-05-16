package com.cgz.request.metadata;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.metadata.ProjectType;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class ProjectTypeAPI {
    public List<ProjectType> getProjectTypes() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/project/type")
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSONObject.parseArray(body, ProjectType.class);
    }
}
