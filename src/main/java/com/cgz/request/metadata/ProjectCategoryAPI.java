package com.cgz.request.metadata;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.metadata.ProjectCategory;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class ProjectCategoryAPI {
    public List<ProjectCategory> getProjectCategories() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/projectCategory")
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSONObject.parseArray(body, ProjectCategory.class);
    }
}
