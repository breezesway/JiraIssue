package com.cgz.request.project;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.project.Version;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class VersionAPI {
    /**
     *
     * @param projectKey 项目的key
     * @return 获取项目的所有版本
     * @throws UnirestException
     */
    public List<Version> getVersions(String projectKey) throws UnirestException {
        String url = "https://issues.apache.org/jira/rest/api/2/project/"+projectKey+"/versions";
        String body = Unirest.get(url)
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSONArray.parseArray(body,Version.class);
    }

    public static Version getVersion(String versionId) throws UnirestException {
        String url = "https://issues.apache.org/jira/rest/api/2/version/"+versionId;
        String body = Unirest.get(url)
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSONObject.parseObject(body,Version.class);
    }
}
