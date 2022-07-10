package com.cgz.bean.user;

import lombok.Data;

@Data
public class User {
    private String name;
    private String key;
    private String displayName;
    private boolean active;
    private String timeZone;
    private String self;

}
