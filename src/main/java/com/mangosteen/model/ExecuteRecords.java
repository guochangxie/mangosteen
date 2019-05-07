package com.mangosteen.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2018/12/209:43 AM
 */
@Getter
@Setter
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
