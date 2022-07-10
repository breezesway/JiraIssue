package com.cgz.bean.issue;

import com.cgz.bean.user.User;
import lombok.Data;

import java.util.List;

@Data
public class History {
    private String id;
    private User author;
    private String created;
    private List<Item> items;
    private String issueKey;

    @Data
    public class Item{
        private String field;
        private String fieldtype;
        private String from;
        private String fromString;
        private String to;
        private String toString;

    }
}
