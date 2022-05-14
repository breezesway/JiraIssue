package com.cgz.request.issue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.user.User;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class VoteAPI {
    public static List<User> getVoters(String issueKey) throws UnirestException {
        String url = "https://issues.apache.org/jira/rest/api/2/issue/"+issueKey+"/votes";
        String body = Unirest.get(url)
                .header("Accept", "application/json")
                .asString()
                .getBody();
        JSONArray voters = JSONObject.parseObject(body).getJSONArray("voters");
        return ParseUtil.parseUserList(voters);
    }
}
