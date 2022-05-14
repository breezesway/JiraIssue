package com.cgz.bean.user;

import lombok.Data;

@Data
public class User {
    String name;
    String key;
    String displayName;
    boolean active;
    String timeZone;
    String self;
}
