package com.cgz.request.issue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.issue.Issue;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import java.util.HashMap;
import java.util.List;

public class IssueAPI {

    public List<Issue> getIssues(String jql,int startAt,int maxResults){
        String url="https://issues.apache.org/jira/rest/api/2/search";
        HashMap<String, Object> parameters = new HashMap<>();
        if(!"all".equals(jql)) {
            parameters.put("jql", jql);
        }
        parameters.put("fields","*all");
        parameters.put("startAt",startAt);
        parameters.put("maxResults",maxResults);
        parameters.put("expand","changelog");
        String body;
        HttpResponse<String> response = null;
        int status = 401;
        while (status == 401){
            try {
                response = Unirest.get(url)
                        .header("Accept", "application/json")
                        .basicAuth("ScanArr","angm13y4-$")
                        .queryString(parameters)
                        .asString();
                status = response.getStatus();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        body = response.getBody();
        JSONArray jsonArray = JSONObject.parseObject(body).getJSONArray("issues");
        return ParseUtil.parseIssueList(jsonArray);
    }

    public int getIssueCount(String jql){
        String url="https://issues.apache.org/jira/rest/api/2/search";
        HashMap<String, Object> parameters = new HashMap<>();
        if(!"all".equals(jql)) {
            parameters.put("jql", jql);
        }
        parameters.put("fields","key");
        parameters.put("maxResults",1);
        String body = null;
        try {
            HttpRequest request = Unirest.get(url)
                    .header("Accept", "application/json")
                    .basicAuth("ScanArr","angm13y4-$")
                    .queryString(parameters);
            body = request
                    .asString()
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(body).getIntValue("total");
    }
}
