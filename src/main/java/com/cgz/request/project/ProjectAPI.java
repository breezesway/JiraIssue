package com.cgz.request.project;

import com.alibaba.fastjson.JSONArray;
import com.cgz.bean.project.Project;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class ProjectAPI {
    public List<Project> getProjects() throws UnirestException {
        String url = "https://issues.apache.org/jira/rest/api/2/project";
        String body = Unirest.get(url)
                .header("Accept", "application/json")
                .queryString("expand","description,lead,url")
                .asString()
                .getBody();
        System.out.println("已获取到全部项目");
        return JSONArray.parseArray(body,Project.class);
    }
}
