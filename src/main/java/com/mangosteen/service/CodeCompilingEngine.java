package com.mangosteen.service;

import com.mangosteen.service.impl.GitRepositoryImpl;
import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/4/1012:27 PM
 */
@Service
public class CodeCompilingEngine {

    private static final Logger logger = LoggerFactory.getLogger(CodeCompilingEngine.class);


    public boolean readyClasses(CodeRepository codeRepository,String devBranch, String sourceFile){

        logger.info("begin readyClasses");
        File file=new File(sourceFile);
        if (!file.exists()){
            try {
                file.mkdirs();
                codeRepository.downLoad(file,devBranch);
            } catch (Exception e) {
                logger.error("code DownLoad失败，详情为:{}",e.getMessage());
            }

        }else {
            if(codeRepository instanceof GitRepositoryImpl){
                codeRepository.update(file,"master");
                if(!devBranch.endsWith(".git")){
                    codeRepository.update(file,devBranch);
                }

            }else {
                codeRepository.update(file,devBranch);
            }


        }

        return compileClass(file);
    }

    public boolean compileClass(File file){

        try {
            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile( new File( file,"pom.xml" ) );
            request.setGoals(Arrays.asList("clean","compile -Dmaven.test.skip=true") );
            Invoker invoker = new DefaultInvoker();
            invoker.setMavenHome(new File(System.getenv("M2_HOME")));
            InvocationResult result = invoker.execute(request);

            if (result.getExitCode()!=0){
                if ( result.getExecutionException() != null )
                {
                    logger.error(result.getExecutionException().getMessage());

                }
                else
                {
                    logger.error("maven execute errorCode is {}",result.getExitCode());

                }

            }

        } catch (MavenInvocationException e) {
            logger.error("maven package fail：{}",e.getMessage());
            return false;
        }
        return true;
    }

    List<String> getDiffClassFile(CodeRepository codeRepository,String baseUrl, String diffUrl, String diffRestFile){

        return codeRepository.diff(baseUrl,diffUrl,diffRestFile);
    }
}
