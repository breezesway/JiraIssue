package com.cgz.bean.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class IssueType {
    private int id;
    private String name;
    private String description;
    private boolean subtask;
    private String avatarId;
    private String self;
    private String iconUrl;

}
