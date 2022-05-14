package com.cgz.bean.issue;

import com.cgz.bean.user.User;
import lombok.Data;

@Data
public class Component{
    private String id;
    private String name;
    private String description;
    private String self;
    private User lead;
    private String assigneeType;
    private String realAssigneeType;
    private boolean isAssigneeTypeValid;
    private String project;
    private long projectId;
    private boolean archived;
}
