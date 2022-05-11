package com.cgz.bean.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueLinkType {
    private String id;
    private String name;
    private String inward;
    private String outward;
    private String self;
}
