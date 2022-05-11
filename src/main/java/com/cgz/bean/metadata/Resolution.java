package com.cgz.bean.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resolution {
    String id;
    String name;
    String description;
    String self;
}
