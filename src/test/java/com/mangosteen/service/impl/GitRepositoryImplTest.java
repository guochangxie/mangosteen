package com.mangosteen.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * GitRepositoryImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Apr 10, 2019</pre>
 */
public class GitRepositoryImplTest {


    GitRepositoryImpl gitRepository=new GitRepositoryImpl();
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: downLoad(File repository, String branch)
     */
    @Test
    public void testDownLoad() throws Exception {
//TODO: Test goes here...
        gitRepository.downLoad(new File("/Users/guochang.xie/Documents/tmp/Kubo-ci"),"http://gitlab.cardinfo.com.cn/QA/Kubo-CI.git");
    }

    /**
     * Method: update(File file, String branch)
     */
    @Test
    public void testUpdate() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: diff(String baseUrl, String diffUrl, String diffRestFile)
     */
    @Test
    public void testDiff() throws Exception {
//TODO: Test goes here...
    }


    /**
     * Method: prepareTreeParser(Repository repository, String ref)
     */
    @Test
    public void testPrepareTreeParser() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = GitRepositoryImpl.getClass().getMethod("prepareTreeParser", Repository.class, String.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
    }

}
