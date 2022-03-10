package io.dougj.metrics.dto;

import java.io.Serializable;

public class AppDataDto implements Serializable {

    private String applicationName;

    private String applicationVersion;

    public String getApplicationName() {return applicationName;}

    public void setApplicationName(String applicationName) {this.applicationName = applicationName;}

    public String getApplicationVersion() {return applicationVersion;}

    public void setApplicationVersion(String applicationVersion) {this.applicationVersion = applicationVersion;}
}
