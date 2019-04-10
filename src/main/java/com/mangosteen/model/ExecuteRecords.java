package com.mangosteen.model;

import java.util.Date;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2018/12/209:43 AM
 */
public class ExecuteRecords {

    private int id ;

    private String projectName;

    private String codeBranch;

    private Date executeTime;

    private String serverIp;

    private String reportPath;

    private String diffUrl;

    //是否为git仓库
    private boolean isGit;
    //是否为增量覆盖率
    private boolean isIncrement;

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

    public String getCodeBranch() {
        return codeBranch;
    }

    public void setCodeBranch(String codeBranch) {
        this.codeBranch = codeBranch;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getDiffUrl() {
        return diffUrl;
    }

    public void setDiffUrl(String diffUrl) {
        this.diffUrl = diffUrl;
    }

    public boolean isGit() {
        return isGit;
    }

    public void setGit(boolean git) {
        isGit = git;
    }

    public boolean isIncrement() {
        return isIncrement;
    }

    public void setIncrement(boolean increment) {
        isIncrement = increment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"projectName\":\"")
                .append(projectName).append('\"');
        sb.append(",\"codeBranch\":\"")
                .append(codeBranch).append('\"');
        sb.append(",\"executeTime\":\"")
                .append(executeTime).append('\"');
        sb.append(",\"serverIp\":\"")
                .append(serverIp).append('\"');
        sb.append(",\"reportPath\":\"")
                .append(reportPath).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
