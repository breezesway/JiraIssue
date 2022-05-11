package com.cgz.request.serverinfo;

import com.alibaba.fastjson.JSON;
import com.cgz.bean.serverinfo.ServerInfo;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ServerInfoAPI {
    public static ServerInfo getServerInfo() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/serverInfo")
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSON.parseObject(body, ServerInfo.class);
    }
}
