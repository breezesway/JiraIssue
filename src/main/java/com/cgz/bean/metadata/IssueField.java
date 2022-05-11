package com.cgz.bean.metadata;

import lombok.Data;

@Data
public class IssueField {
    private String id;
    private String name;
    private boolean custom;
    private boolean orderable;
    private boolean navigable;
    private boolean searchable;
    private String[] clauseNames;
    private Schema schema;

    @Data
    public class Schema {
        private String type;
        private String system;
        private String custom;
        private String customId;
    }

}
