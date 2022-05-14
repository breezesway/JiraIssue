package com.cgz.bean.issue;

import lombok.Data;

@Data
public class RemoteLink {
    private int id;
    private String globalId;    //可能有些没有该字段
    private String self;
    //private Object applicaiton;   //暂不清楚该字段的类型和含义
    private String remoteObjectUrl;
    private String remoteObjectTitle;
    //private Object remoteObjectIcon;  //不清楚该字段类型
    //private Object remoteObjectStatus;    //不清楚该字段类型
}
