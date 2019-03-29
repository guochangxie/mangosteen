package com.mangosteen.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mangosteen.model.ExecuteRecords;
import com.mangosteen.model.Project;
import com.mangosteen.model.ProjectConfig;
import com.mangosteen.service.JacocoService;
import com.mangosteen.service.ProjectConfigService;
import com.mangosteen.service.ProjectService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/project")
public class MangosteenProjectController {

    private static final Logger logger = LoggerFactory.getLogger(MangosteenProjectController.class);
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectConfigService projectConfigService;
    @Autowired
    private JacocoService jacocoService;
    @RequestMapping("/showProject")
    public String createProject(HttpServletRequest httpServletRequest, ModelMap modelMap) {

        List<Project> projectList = projectService.queryAllProject();
        modelMap.addAttribute("templatename", "projectList");
        modelMap.addAttribute("projectList", projectList);

        return "index";
    }

    /**
     * 跳转到添加项目页面
     *
     * @param httpServletRequest
     * @param modelMap
     * @return
     */
    @RequestMapping("/toAddProject")
    public String toAddProject(HttpServletRequest httpServletRequest, ModelMap modelMap) {
        modelMap.addAttribute("templatename", "addProject");
        return "index";
    }

    @RequestMapping("/login")
    public String toLogin(HttpServletRequest httpServletRequest, ModelMap modelMap) {
        return "login";
    }

    @RequestMapping("/isLogin")
    @ResponseBody
    public String login(HttpServletRequest httpServletRequest){

        String userName=httpServletRequest.getParameter("userName");
        String passwd=httpServletRequest.getParameter("password");
        boolean isLogin = jacocoService.isLogin(userName, passwd);
        if(isLogin){
            httpServletRequest.getSession().setAttribute("userName",userName);

            return "SUCCESS";
        }
        return "FAIL";

    }


    /**
     * 跳转到项目执行页面
     *
     * @param httpServletRequest
     * @param modelMap
     * @return
     */
    @RequestMapping("/toExecuteProject")
    public String toExecuteProject(HttpServletRequest httpServletRequest, Model modelMap) {
        String projectName = httpServletRequest.getParameter("projectName");
        modelMap.addAttribute("templatename", "executeProject");
        Project project = projectService.queryProjectByName(projectName);
        List<ProjectConfig> projectConfigs=projectConfigService.queryProjectConfigByProjectId(project.getId());
        modelMap.addAttribute("project",project);
        modelMap.addAttribute("projectConfig",projectConfigs);

        return "index";
    }

    /**
     * 跳转到构建历史记录
     *
     * @param httpServletRequest
     * @param modelMap
     * @return
     */
    @RequestMapping("/tobuildHistory")
    public String tobuildHistory(HttpServletRequest httpServletRequest, Model modelMap) {
        String projectName = httpServletRequest.getParameter("projectName");
        modelMap.addAttribute("templatename", "buildHistory");
        List<ExecuteRecords> executeRecords=projectService.queryExecuteRecordByProjectName(projectName);
        modelMap.addAttribute("executeRecords",executeRecords);

        return "index";
    }


    @RequestMapping("/saveProject")
    @ResponseBody
    public  String addProject(@RequestBody String request, HttpServletRequest httpServletRequest) {

        JSONObject requestData = JSON.parseObject(request);
        Project project=projectService.queryProjectByName(requestData.getString("projectName"));
        if(project==null){
            project=new Project();
            project.setProjectName(requestData.getString("projectName"));
            project.setCodeBranch(requestData.getString("codeBranch"));
            projectService.saveProject(project);

        }


        JSONArray projectConfigArray = requestData.getJSONArray("projectConfig");
        List<ProjectConfig> projectConfigs = new ArrayList<ProjectConfig>();
        for (int i=0;i<projectConfigArray.size();i++){
            JSONObject jsonObject = projectConfigArray.getJSONObject(i);
            ProjectConfig projectConfig = jsonObject.toJavaObject(jsonObject, ProjectConfig.class);
            projectConfig.setProjectId(project.getId());
            projectConfigs.add(projectConfig);

        }
        projectService.saveProjectConfig(projectConfigs);
        return "SUCCESS";
    }

    @RequestMapping(value = "/reportDetail")

    public String toReport(HttpServletRequest httpServletRequest, Model modelMap) {

        modelMap.addAttribute("templatename", "jacocoReportDetail");
        modelMap.addAttribute("reportPath", httpServletRequest.getParameter("reportPath"));
        modelMap.addAttribute("projectName",httpServletRequest.getParameter("projectName"));
        modelMap.addAttribute("codeBranch",httpServletRequest.getParameter("codeBranch"));
        modelMap.addAttribute("serverIp",httpServletRequest.getParameter("serverIp"));

        return "index";
    }

    @RequestMapping("/buildReport")
    public String buildReport(HttpServletRequest request,Model modelMap){
        JSONObject requestData = JSON.parseObject(request.getParameter("buildReportForm"));
        ExecuteRecords executeRecords=new ExecuteRecords();
        executeRecords.setProjectName(requestData.getString("hf-projectName"));
        String codeBranch=requestData.getString("hf-devBranch");
        if (codeBranch.endsWith("/")){
            codeBranch=StringUtils.substringBeforeLast(codeBranch,"/");
        }
        executeRecords.setCodeBranch(codeBranch);
        executeRecords.setServerIp(requestData.getString("hf-ip").replace("[","").replace("]","").replace("\"",""));
        executeRecords.setExecuteTime(new Date());
        if(StringUtils.isNotBlank(request.getParameter("Increment"))){
            Project project = projectService.queryProjectByName(executeRecords.getProjectName());
            executeRecords.setDiffUrl(project.getCodeBranch());
        }
        String reportPath = null;
        try {
            reportPath = jacocoService.buildReport(executeRecords);
        } catch (IOException e) {
            logger.error("覆盖率报告生成失败:{}",e.getMessage());
            reportPath ="/error.html";
        }
        if (StringUtils.isNotBlank(reportPath)){
            executeRecords.setReportPath(reportPath);
            projectService.saveProjectExecuteRecords(executeRecords);

        }else {
            reportPath ="/error.html";
        }
        modelMap.addAttribute("templatename", "jacocoReportDetail");
        modelMap.addAttribute("reportPath", reportPath);
        modelMap.addAttribute("projectName",executeRecords.getProjectName());
        modelMap.addAttribute("codeBranch",executeRecords.getCodeBranch());
        modelMap.addAttribute("serverIp",executeRecords.getServerIp());

        return "index";
    }


    @RequestMapping("/toResetCoverage")
    public String toResetCoverage(HttpServletRequest request,ModelMap modelMap){
        List<Project> projectList = projectService.queryAllProject();
        modelMap.addAttribute("templatename", "resetCoverage");
        modelMap.addAttribute("projectList", projectList);
        return "index";
    }

    @RequestMapping("/resetCoverage")
    @ResponseBody
    public String resetCoverage(HttpServletRequest request){
        String projectName = request.getParameter("projectName");
        String ip=request.getParameter("ip");
        ProjectConfig projectConfig = projectConfigService.queryProjectConfigByProjectNameAndIp(projectName, ip);
        boolean resetDump = jacocoService.resetDump(ip, projectConfig.getServerPort());
        if(resetDump){
            return "SUCCESS";
        }
        return "FAIL";
    }


    @RequestMapping("/queryServerIP")
    @ResponseBody
    public List<ProjectConfig> queryServerIP(HttpServletRequest request){
        String projectName = request.getParameter("projectName");
        return projectConfigService.queryProjectConfigByProjectName(projectName);
    }



}
