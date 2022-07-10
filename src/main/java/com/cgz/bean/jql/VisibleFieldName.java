package com.cgz.bean.jql;

import lombok.Data;

@Data
public class VisibleFieldName {
    private String value;
    private String displayName;
    private String auto;
    private String orderable;
    private String searchable;
    private String[] operators;
    private String[] types;

}
