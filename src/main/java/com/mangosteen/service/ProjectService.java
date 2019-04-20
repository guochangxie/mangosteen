package com.mangosteen.service;

import com.mangosteen.dao.ExecuteRecordMapper;
import com.mangosteen.dao.ProjectMapper;
import com.mangosteen.model.ExecuteRecords;
import com.mangosteen.model.Project;
import com.mangosteen.model.ProjectConfig;
import org.apache.commons.lang3.StringUtils;
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

    public List<ExecuteRecords> queryExecuteRecordByProjectName(String projectName,int pageNumber,int pageSize){

        return executeRecordMapper.queryExecuteRecordOnPage(projectName,pageNumber,pageSize);
    }

    public int countExecuteRecordByProjectName(String projectName){
        return executeRecordMapper.countExecuteRecordByProjectName(projectName);
    }
    public void updateProjectById(Project project){
        projectMapper.updateProjectById(project);
    }

    public void deleteByProjectName(String projectName) {
        projectMapper.deleteByProjectName(projectName);
    }

    public List<ExecuteRecords> queryExecuteRecordByProjectName(String projectName, String branch, String beginTime, String endTime, int currentPage, int pageSize) {
        return executeRecordMapper.queryExecuteRecordByBranch(projectName,branch,beginTime,endTime,currentPage,pageSize);
    }

    public int countExecuteRecordByProjectNameAndBranch(String projectName, String branch) {

        return executeRecordMapper.countExecuteRecordByProjectNameAndBranch(projectName,branch);
    }
}
