package com.cgz.bean.issue;

import com.cgz.bean.user.User;
import lombok.Data;

@Data
public class WorkLog {
    private String id;
    private String comment;
    private String created;
    private String updated;
    private String started;
    private String timeSpent;
    private long timeSpentSeconds;
    private User author;
    private User updateAuthor;
    private String issueId;
    private String issueKey;
    private String self;

}
