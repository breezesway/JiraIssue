package com.cgz.bean.issue;

import lombok.Data;

@Data
public class Transition {
    private String authorDisplayName;
    private String created;
    private String issueKey;
    private String fromString;
    private String toString;
}
