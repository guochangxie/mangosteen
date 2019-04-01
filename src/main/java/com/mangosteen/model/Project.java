package com.mangosteen.model;

import java.io.Serializable;

public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    private String projectName;

    private String projectGroup;

    private String codeBranch;

    private String projectConfig;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }



    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGroup() {
        return projectGroup;
    }

    public void setGroup(String projectGroup) {
        this.projectGroup = projectGroup;
    }

    public String getCodeBranch() {
        return codeBranch;
    }

    public void setCodeBranch(String codeBranch) {
        this.codeBranch = codeBranch;
    }

    public String getProjectGroup() {
        return projectGroup;
    }

    public void setProjectGroup(String projectGroup) {
        this.projectGroup = projectGroup;
    }

    public String getProjectConfig() {
        return projectConfig;
    }

    public void setProjectConfig(String projectConfig) {
        this.projectConfig = projectConfig;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"projectName\":\"")
                .append(projectName).append('\"');
        sb.append(",\"projectGroup\":\"")
                .append(projectGroup).append('\"');
        sb.append(",\"codeBranch\":\"")
                .append(codeBranch).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
