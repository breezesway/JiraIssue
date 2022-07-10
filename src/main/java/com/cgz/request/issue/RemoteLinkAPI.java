package com.cgz.request.issue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.issue.RemoteLink;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class RemoteLinkAPI {
    public List<RemoteLink> getRemoteLinks(String issueKey){
        String url = "https://issues.apache.org/jira/rest/api/2/issue/"+issueKey+"/remotelink";
        String body;
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(url)
                    .header("Accept", "application/json")
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        while (response.getStatus()==401){
            try {
                response = Unirest.get(url)
                        .header("Accept", "application/json")
                        .asString();
                System.out.println(response.getBody());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        body = response.getBody();
        JSONArray jsonArray = JSONObject.parseArray(body);
        return ParseUtil.parseRemoteLinkList(jsonArray);
    }
}
