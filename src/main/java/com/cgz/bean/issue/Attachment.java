package com.cgz.bean.issue;

import com.cgz.bean.user.User;
import lombok.Data;

@Data
public class Attachment {
    private String id;  //唯一Id
    private String filename;    //补丁的文件名
    private User author;    //作者
    private String created; //创建时间
    private long size;  //补丁大小（字节）
    private String mineType;    //文件类型
    private String content; //补丁文件的Url
    private String self;
}
