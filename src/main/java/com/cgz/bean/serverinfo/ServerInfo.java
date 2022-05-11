package com.cgz.bean.serverinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo {
    String baseUrl;
    String version;
    int[] versionNumbers;
    String deploymentType;
    String buildNumber;
    String buildDate;
    String databaseBuildNumber;
    String serverTime;
    String scmInfo;
    String serverTitle;
}
