package com.mangosteen.service;

import com.mangosteen.dao.ExecuteRecordMapper;
import com.mangosteen.dao.ProjectMapper;
import com.mangosteen.model.ExecuteRecords;
import com.mangosteen.model.Project;
import com.mangosteen.model.ProjectConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ExecuteRecordMapper executeRecordMapper;

    public List<Project> queryAllProject() {
        return projectMapper.queryAllProject();
    }

    public int saveProject(Project project){
        return projectMapper.saveProject(project);
    }

    public Project queryProjectByName(String  projectName){
        return projectMapper.queryProjectByName(projectName);
    }


    public void saveProjectExecuteRecords(ExecuteRecords executeRecords) {
        executeRecordMapper.saveExecuteRecords(executeRecords);
    }

    public List<ExecuteRecords> queryExecuteRecordByProjectName(String projectName) {
        return executeRecordMapper.queryExecuteRecordByProjectName(projectName);
    }
}
