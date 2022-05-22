package com.cgz.request.issue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.user.User;
import com.cgz.util.ParseUtil;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class VoteAPI {
    public List<User> getVoters(String issueKey){
        String url = "https://issues.apache.org/jira/rest/api/2/issue/"+issueKey+"/votes";
        String body = null;
        try {
            body = Unirest.get(url)
                    .header("Accept", "application/json")
                    .asString()
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        JSONArray voters = JSONObject.parseObject(body).getJSONArray("voters");
        return ParseUtil.parseUserList(voters);
    }
}
