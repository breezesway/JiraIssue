package com.cgz.request.metadata;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.metadata.StatusCategory;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class StatusCategoryAPI {
    public static List<StatusCategory> getStatusCategories() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/statuscategory")
                .header("Accept", "application/json")
                .asString()
                .getBody();

        return JSONObject.parseArray(body, StatusCategory.class);
    }
}
