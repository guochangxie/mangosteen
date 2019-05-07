package com.mangosteen.service;

import com.mangosteen.service.impl.GitRepositoryImpl;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.shared.invoker.*;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/4/1012:27 PM
 */
@Slf4j
@Service
@Setter
public class CodeEngine {

     private LogOption logOption;

    public boolean readyClasses(CodeRepository codeRepository,String devBranch, String sourceFile){

        logOption.writeLog("begin readyClasses");
        File file=new File(sourceFile);
        if (!file.exists()){
            try {
                file.mkdirs();
                codeRepository.downLoad(file,devBranch);
            } catch (Exception e) {
                log.error("code DownLoad失败，详情为:{}",e.getMessage());
                logOption.writeLog("code DownLoad失败，详情为:"+e.getMessage());
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
            final CollectingLogOutputStream logOutput = new CollectingLogOutputStream(true);
            invoker.setOutputHandler(new PrintStreamHandler(new PrintStream(logOutput), true));
            InvocationResult result = invoker.execute(request);
            if (result.getExitCode()!=0){
                if ( result.getExecutionException() != null )
                {
                    log.error(result.getExecutionException().getMessage());
                    logOption.writeLog("code compileClass，详情为:"+result.getExecutionException().getMessage());
                }
                else
                {
                    log.error("maven execute errorCode is {}",result.getExitCode());
                }

            }
            logOption.writeLog(logOutput.getLine());

        } catch (MavenInvocationException e) {
            log.error("maven package fail：{}",e.getMessage());
            logOption.writeLog("maven package fail：:"+e.getMessage());

            return false;
        }

        return true;
    }

    List<String> getDiffClassFile(CodeRepository codeRepository,String baseUrl, String diffUrl, String diffRestFile){

        return codeRepository.diff(baseUrl,diffUrl,diffRestFile);
    }
}
