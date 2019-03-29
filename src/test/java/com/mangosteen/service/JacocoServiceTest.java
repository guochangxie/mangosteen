package com.mangosteen.service;

import com.mangosteen.MangosteenApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * JacocoService Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Feb 25, 2019</pre>
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MangosteenApplication.class)
public class JacocoServiceTest {

    @Autowired
     private JacocoService jacocoService;
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: visitDumpCommand(String address, int port, String saveFile)
     */
    @Test
    public void testVisitDumpCommand() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: resetDump(String address, int port)
     */
    @Test
    public void testResetDump() throws Exception {

    }

    /**
     * Method: readyClasses(String devBranch, String sourceFile)
     */
    @Test
    public void testReadyClasses() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: doDiff(String baseUrl, String diffUrl, String resultFilePath)
     */
    @Test
    public void testDoDidd() throws Exception {

    }

    /**
     * Method: paseDiffFile(String filePath)
     */
    @Test
    public void testPaseDiffFile() throws Exception {
        List<String> list = jacocoService.paseDiffFile("/Users/guochang.xie/Documents/tmp/zipkin-server_20180917-195835430_pos.txt");
        System.out.printf(">>>>>>>");
        System.out.println(list.toString());
    }

    /**
     * Method: buildReport(ExecuteRecords executeRecords)
     */
    @Test
    public void testBuildReport() throws Exception {
//TODO: Test goes here...
    }


}
