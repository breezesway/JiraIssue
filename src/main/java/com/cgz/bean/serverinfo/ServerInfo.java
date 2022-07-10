package com.cgz.bean.serverinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
public class ServerInfo {
    private String baseUrl;
    private String version;
    private int[] versionNumbers;
    private String deploymentType;
    private String buildNumber;
    private String buildDate;
    private String databaseBuildNumber;
    private String serverTime;
    private String scmInfo;
    private String serverTitle;

}
