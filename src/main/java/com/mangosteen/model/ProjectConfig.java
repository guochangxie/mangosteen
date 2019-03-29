package com.mangosteen.model;


public class ProjectConfig {

    private int id ;
    private int projectId;
    private String serverIp;
    private int serverPort;
    private String serverContainer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerContainer() {
        return serverContainer;
    }

    public void setServerContainer(String serverContainer) {
        this.serverContainer = serverContainer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"projectId\":")
                .append(projectId);
        sb.append(",\"serverIp\":\"")
                .append(serverIp).append('\"');
        sb.append(",\"serverPort\":\"")
                .append(serverPort).append('\"');
        sb.append(",\"serverContainer\":\"")
                .append(serverContainer).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
