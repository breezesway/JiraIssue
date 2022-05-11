package com.cgz.request.jql;

import com.alibaba.fastjson.JSONObject;
import com.cgz.bean.jql.VisibleFieldName;
import com.cgz.bean.jql.VisibleFunctionName;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteDataAPI {
    public static List<Object> getAutoCompleteData() throws UnirestException {
        String body = Unirest.get("https://issues.apache.org/jira/rest/api/2/jql/autocompletedata")
                .header("Accept", "application/json")
                .asString()
                .getBody();
        JSONObject jsonObject = JSONObject.parseObject(body);
        List<VisibleFieldName> visibleFieldNames = JSONObject.parseArray(jsonObject.getString("visibleFieldNames"), VisibleFieldName.class);
        List<VisibleFunctionName> visibleFunctionNames = JSONObject.parseArray(jsonObject.getString("visibleFunctionNames"), VisibleFunctionName.class);
        List<String> jqlReservedWord = JSONObject.parseArray(jsonObject.getString("jqlReservedWords"), String.class);
        ArrayList<Object> list = new ArrayList<>();
        list.add(visibleFieldNames);
        list.add(visibleFunctionNames);
        list.add(jqlReservedWord);
        return list;
    }
}
