package com.mangosteen.service;

import com.mangosteen.dao.ProjectConfigMapper;
import com.mangosteen.model.ProjectConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2018/12/2011:09 AM
 */
@Service
public class ProjectConfigService {

    @Autowired
    private ProjectConfigMapper projectConfigMapper;

    public List<ProjectConfig> queryProjectConfigByProjectId(int projectId) {
        return projectConfigMapper.queryProjectConfigByProjectId(projectId);

    }

    public ProjectConfig queryProjectConfigByProjectNameAndIp(String projectName, String ip){
        return  projectConfigMapper.queryProjectConfigByProjectNameAndIp(projectName,ip);
    }

    public List<ProjectConfig> queryProjectConfigByProjectName(String projectName){
        return  projectConfigMapper.queryProjectConfigByProjectName(projectName);
    }

}
