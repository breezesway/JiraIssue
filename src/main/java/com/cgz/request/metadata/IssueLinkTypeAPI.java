package com.cgz.request.metadata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.metadata.IssueLinkType;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class IssueLinkTypeAPI {
    public List<IssueLinkType> getIssueLinkTypes() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/issueLinkType")
                .header("Accept", "application/json")
                .asString()
                .getBody();

        JSONArray issueLinkTypes = JSONObject.parseObject(body).getJSONArray("issueLinkTypes");
        return issueLinkTypes.toJavaList(IssueLinkType.class);
    }
}
