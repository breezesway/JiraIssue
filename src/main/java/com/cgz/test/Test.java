package com.cgz.test;

import com.cgz.request.jql.AutoCompleteDataAPI;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.sql.SQLException;
import java.util.List;

/**
 https://issues.apache.org/jira/secure/BrowseProjects.jspa?selectedCategory=all
 https://issues.apache.org/jira/projects/ACCUMULO/issues/ACCUMULO-4419?filter=allissues
 https://developer.atlassian.com/cloud/jira/platform/rest/v3/api-group-issues/#api-rest-api-3-issue-issueidorkey-get
 */

public class Test {
    public static void main(String[] args) throws UnirestException, SQLException {
        List<Object> autoCompleteData = AutoCompleteDataAPI.getAutoCompleteData();
    }
}
