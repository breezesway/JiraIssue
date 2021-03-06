package com.cgz.request.project;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.project.Component;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class ComponentAPI {
    /**
     *
     * @param projectKey 项目的key
     * @return 返回该项目的所有component
     * @throws UnirestException
     */
    public List<Component> getComponents(String projectKey) throws UnirestException {
        String url = "https://issues.apache.org/jira/rest/api/2/project/"+projectKey+"/components";
        String body = Unirest.get(url)
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSONArray.parseArray(body,Component.class);
    }

    public static Component getComponent(String componentId) throws UnirestException {
        String url = "https://issues.apache.org/jira/rest/api/2/component/"+componentId;
        String body = Unirest.get(url)
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSONObject.parseObject(body,Component.class);
    }
}
