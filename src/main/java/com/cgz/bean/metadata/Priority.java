package com.cgz.bean.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Priority {
    private String id;
    private String name;
    private String description;
    private String statusColor;
    private String self;
    private String iconUrl;

}
