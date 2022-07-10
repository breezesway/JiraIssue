package com.cgz.bean.issue;

import com.cgz.bean.metadata.IssueLinkType;
import lombok.Data;

@Data
public class IssueLink {
    private String id;
    private String self;
    private IssueLinkType type;
    private String inwardIssueKey;
    private String outwardIssueKey;

}
