package com.cgz.bean.metadata;

import lombok.Data;

@Data
public class Status {
    private String id;
    private String name;
    private String description;
    private String iconUrl;
    private String self;
    private StatusCategory statusCategory;
}
