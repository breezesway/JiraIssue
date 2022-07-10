package com.cgz.bean.issue;

import lombok.Data;

@Data
public class PriorityChanged {
    private String authorDisplayName;
    private String created;
    private String issueKey;
    private String fromString;
    private String toString;

}
