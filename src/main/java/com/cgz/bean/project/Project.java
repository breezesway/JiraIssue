package com.cgz.bean.project;

import com.cgz.bean.metadata.ProjectCategory;
import com.cgz.bean.user.User;
import lombok.Data;

@Data
public class Project {
    private String id;
    private String key;
    private String name;
    private String description;
    private User lead;
    private String assigneeType;
    private String url;
    private ProjectCategory projectCategory;
    private String projectTypeKey;
    private boolean archived;
    private String self;

}
