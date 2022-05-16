package com.cgz.request.metadata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.metadata.Dashboard;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class DashboardAPI {

    public List<Dashboard> getDashboards() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/dashboard")
                .header("Accept", "application/json")
                .queryString("maxResults",10000)
                .asString()
                .getBody();

        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray dashboards = jsonObject.getJSONArray("dashboards");
        return dashboards.toJavaList(Dashboard.class);
    }

}
