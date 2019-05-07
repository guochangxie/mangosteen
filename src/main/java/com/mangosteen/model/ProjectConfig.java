package com.mangosteen.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectConfig {

    private int id ;
    private int projectId;
    private String serverIp;
    private int serverPort;
    private String serverContainer;


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
