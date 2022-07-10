package com.cgz.bean.issue;

import com.cgz.bean.user.User;
import lombok.Data;

@Data
public class Comment {
    private String id;
    private String body;
    private String created;
    private String updated;
    private User author;
    private User updateAuthor;
    private String issueKey;
    private String self;

}
