package com.cgz.bean.project;

import lombok.Data;

@Data
public class Version {
    private String id;
    private String name;
    private String description;
    private boolean archived;
    private boolean released;
    private String releaseDate;
    private String userReleaseDate;
    private int projectId;
    private String self;

}
