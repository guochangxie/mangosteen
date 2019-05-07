package com.mangosteen.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project  {


    private int id;

    private String projectName;

    private String projectGroup;

    private String codeBranch;

    private String projectConfig;


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
