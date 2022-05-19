package com.cgz.handler;

import com.cgz.dao.metadata.*;
import com.cgz.request.metadata.*;
import com.cgz.ui.MyFrame;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.sql.SQLException;

public class MetaHandler {
    /**
     * 获取所有的元数据并插入mysql
     * @param myFrame
     * @throws UnirestException
     * @throws SQLException
     */
    public void getAllMetaData(MyFrame myFrame) throws UnirestException, SQLException {
        new DashboardDao().insertDashboards(new DashboardAPI().getDashboards());
        myFrame.addJTextAreaInfo("Dashboard数据已插入...");
        new IssueFieldDao().insertIssueFields(new IssueFieldAPI().getIssueFields());
        myFrame.addJTextAreaInfo("IssueField数据已插入...");
        new IssueLinkTypeDao().insertIssueLinkTypes(new IssueLinkTypeAPI().getIssueLinkTypes());
        myFrame.addJTextAreaInfo("IssueLinkType数据已插入...");
        new IssueTypeDao().insertIssueTypes(new IssueTypeAPI().getIssueTypes());
        myFrame.addJTextAreaInfo("IssueType数据已插入...");
        new PriorityDao().insertPriority(new PriorityAPI().getPriorities());
        myFrame.addJTextAreaInfo("Priority数据已插入...");
        new ProjectCategoryDao().insertProjectCategories(new ProjectCategoryAPI().getProjectCategories());
        myFrame.addJTextAreaInfo("ProjectCategory数据已插入...");
        new ProjectTypeDao().insertProjectTypes(new ProjectTypeAPI().getProjectTypes());
        myFrame.addJTextAreaInfo("ProjectType数据已插入...");
        new ResolutionDao().insertResolutions(new ResolutionAPI().getResolutions());
        myFrame.addJTextAreaInfo("Resolution数据已插入...");
        new StatusDao().insertStatuses(new StatusAPI().getStatuses());
        myFrame.addJTextAreaInfo("Status数据已插入...");
        new StatusCategoryDao().insertStatusCategories(new StatusCategoryAPI().getStatusCategories());
        myFrame.addJTextAreaInfo("StatusCategory数据已插入...");
        myFrame.addJTextAreaInfo("元数据获取,插入完成!");
    }
}
