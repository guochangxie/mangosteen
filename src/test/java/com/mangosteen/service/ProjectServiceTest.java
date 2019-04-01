package com.mangosteen.service;

import com.alibaba.fastjson.JSON;
import com.mangosteen.MangosteenApplication;
import com.mangosteen.model.Project;
import com.mangosteen.model.ProjectConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * ProjectService Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Apr 1, 2019</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MangosteenApplication.class)
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: queryAllProject()
     */
    @Test
    public void testQueryAllProject() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: saveProject(Project project)
     */
    @Test
    public void testSaveProject() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: queryProjectByName(String  projectName)
     */
    @Test
    public void testQueryProjectByName() throws Exception {
        Project project = projectService.queryProjectByName("123");
        List<ProjectConfig> projectConfigs= JSON.parseArray(project.getProjectConfig(), ProjectConfig.class);
        System.out.printf(projectConfigs.get(0).toString());

    }

    /**
     * Method: saveProjectExecuteRecords(ExecuteRecords executeRecords)
     */
    @Test
    public void testSaveProjectExecuteRecords() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: queryExecuteRecordByProjectName(String projectName)
     */
    @Test
    public void testQueryExecuteRecordByProjectName() throws Exception {
//TODO: Test goes here...
    }


}
