package com.cgz.request.issue;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.issue.RemoteLink;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class RemoteLinkAPI {
    public List<RemoteLink> getRemoteLinks(String issueKey){
        String url = "https://issues.apache.org/jira/rest/api/2/issue/"+issueKey+"/remotelink";
        String body = null;
        try {
            body = Unirest.get(url)
                    .header("Accept", "application/json")
                    .asString()
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return ParseUtil.parseRemoteLinkList(JSONObject.parseArray(body));
    }
}
