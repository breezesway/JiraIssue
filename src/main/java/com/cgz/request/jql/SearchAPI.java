package com.cgz.request.jql;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

public class SearchAPI {
    public void Search(Map<String,Object> map) throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/search")
                .header("Accept", "application/json")
                .queryString(map)
                .asString()
                .getBody();

    }
}
