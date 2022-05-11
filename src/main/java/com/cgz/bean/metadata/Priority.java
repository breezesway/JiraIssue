package com.cgz.bean.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Priority {
    String id;
    String name;
    String description;
    String statusColor;
    String self;
    String iconUrl;
}
