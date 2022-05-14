package com.cgz.bean.project;

import lombok.Data;

@Data
public class Version {
    String id;
    String name;
    String description;
    boolean archived;
    boolean released;
    String releaseDate;
    String userReleaseDate;
    long projectId;
    String self;
}
