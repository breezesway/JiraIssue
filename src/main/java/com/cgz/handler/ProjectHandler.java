package com.cgz.handler;

import com.cgz.bean.project.Component;
import com.cgz.bean.project.Project;
import com.cgz.bean.project.Version;
import com.cgz.dao.project.ComponentDao;
import com.cgz.dao.project.ProjectDao;
import com.cgz.dao.project.VersionDao;
import com.cgz.request.project.ComponentAPI;
import com.cgz.request.project.ProjectAPI;
import com.cgz.request.project.VersionAPI;
import com.cgz.ui.MyFrame;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectHandler {
    public void getAllProjectInfo(MyFrame myFrame) throws UnirestException, SQLException {
        List<Project> projects = new ProjectAPI().getProjects();
        myFrame.addJTextAreaInfo("已获取到所有Project的信息...");
        new ProjectDao().insertProjects(projects);
        myFrame.addJTextAreaInfo("已将所有Project的信息插入Mysql...");
        ComponentAPI componentAPI = new ComponentAPI();
        VersionAPI versionAPI = new VersionAPI();
        List<Component> components = new ArrayList<>();
        List<Version> versions = new ArrayList<>();
        for (Project project:projects){
            String key = project.getKey();
            components.addAll(componentAPI.getComponents(key));
            versions.addAll(versionAPI.getVersions(key));
        }
        myFrame.addJTextAreaInfo("已获取到所有Project的component和version信息...");
        new ComponentDao().insertComponents(components);
        new VersionDao().insertVersions(versions);
        myFrame.addJTextAreaInfo("已将所有Project的component和version信息插入Mysql...");
        myFrame.addJTextAreaInfo("所有项目信息获取,插入完成!");
    }
}
