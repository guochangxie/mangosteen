package com.mangosteen.service;


import com.mangosteen.dao.UserMapper;
import com.mangosteen.model.ExecuteRecords;

import com.mangosteen.service.impl.GitRepositoryImpl;
import com.mangosteen.service.impl.SvnRepositoryImpl;
import com.mangosteen.tools.FileUtils;
import com.mangosteen.tools.MD5Tools;
import org.apache.commons.lang3.StringUtils;
import com.mangosteen.jacoco.core.tools.ExecDumpClient;
import com.mangosteen.jacoco.core.tools.ExecFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2018/12/2811:34 AM
 */
@Service
public class JacocoService {

    private static final Logger logger = LoggerFactory.getLogger(JacocoService.class);

    @Autowired
    private CodeCompilingEngine codeCompilingEngine;

    @Value("${mangosteen.workspace}")
    private String mangosteenWorkSpace;

    @Autowired
    private SvnRepositoryImpl svnRepositoryImpl;

    @Autowired
    private GitRepositoryImpl gitRepositoryImpl;

    @Autowired
    private UserMapper userMapper;

    /**
     * 执行dump命令，获取代码覆盖率二进制文件
     * @param address 服务IP地址
     * @param port    服务端口
     */

    public boolean visitDumpCommand(String saveFile,String ...ips){

        logger.info("visitDump....");
        File file=new File(saveFile);
        if (!file.exists()){
            file.mkdirs();
        }
        ExecDumpClient client = new ExecDumpClient();
        client.setDump(true);
        ExecFileLoader execFileLoader = null;
        try {
            for(String ip:ips){
                execFileLoader = client.dump(StringUtils.substringBefore(ip,":"), Integer.valueOf(StringUtils.substringAfter(ip,":")));
                execFileLoader.save(new File(saveFile+"jacoco-client.exec"), true);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;

    }


    /**
     *  重置服务端覆盖率文件
     * @param address
     * @param port
     */

    public boolean resetDump(String address,String port){
        ExecDumpClient client = new ExecDumpClient();
        client.setReset(true);
        client.setDump(false);
        //目标机器的ip和端口，对应着运行程序时javaagent参数里的ip和端口
        try {
            client.dump(address, Integer.valueOf(port));
        } catch (IOException e) {
            logger.error("覆盖率重置失败，失败详情{}",e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 构建jacoco测试报告文件
     * 文件结构为
     * mongosteenworkspace
     * 	jobName
     * 		branchName
     * 			source
     * 			builddate
     * 				jacoco-client-"+address+".exec
     * 				coveragereport
     * 			    target
     * @param executeRecords
     * 生成增量覆盖率报告思路为：
     * 1.利用svn或者gitdiff出差异文件，解析差异文件
     * 2.根据Index:标志解析到变更的文件名
     * 3.根据@@ -39,16 +40,68 @@ 标志解析到变更的行数，反向解析到变成的方法名（暂时还没实现，有技术难度可以用Java语法树或者JavaParser）
     * 4.将变更的文件名，方法名传入到jacoo-core的ReportGenerator类中analyzeStructure方法
     * 5.在analyzer.analyzeAll中将为变更的class文件过滤掉，然后执行dump命令,这样生成的报告只包含变更的文件
     * 6.报告后处理，在生成的报告文件上进行加工，将变更的行数进行样式修改，标记为变更的颜色（目前还未实现此功能）
     * @return
     */
    public String buildReport(ExecuteRecords executeRecords) throws IOException {

        String baseFilePath=null;
        CodeRepository codeRepository=null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String dumpFilePath=simpleDateFormat.format(executeRecords.getExecuteTime())+"/";
        String reportPath=null;
        if(executeRecords.isGit()){
            baseFilePath=mangosteenWorkSpace +"/"+executeRecords.getProjectName()+"/";
            executeRecords.setDiffUrl(baseFilePath+"source");
            reportPath="/"+executeRecords.getProjectName()+"/"+dumpFilePath+"coveragereport/index.html";
            codeRepository=gitRepositoryImpl;
        }else{
            baseFilePath= mangosteenWorkSpace +"/"+executeRecords.getProjectName()+"/"+ StringUtils.substringAfterLast(executeRecords.getCodeBranch(),"/")+"/";
            reportPath="/"+executeRecords.getProjectName()+"/"+ StringUtils.substringAfterLast(executeRecords.getCodeBranch(),"/")+"/"+dumpFilePath+"coveragereport/index.html";
            codeRepository=svnRepositoryImpl;
        }

        if (codeCompilingEngine.readyClasses(codeRepository,executeRecords.getCodeBranch(),baseFilePath+"source")){
            String[] ips = executeRecords.getServerIp().split(",");
            boolean dumpResult = visitDumpCommand( baseFilePath + dumpFilePath,ips);
            if(dumpResult){
                Path sourceTarget = Paths.get(baseFilePath + "source/target");
                Path reportTarget = Paths.get(baseFilePath + dumpFilePath);
                FileUtils.copyDir(sourceTarget,reportTarget);

                File baseFile=new File(baseFilePath);
                ReportGenerator reportGenerator=new ReportGenerator(baseFile);
                reportGenerator.setExecutionDataFile(new File(baseFile,dumpFilePath+"jacoco-client.exec"));
                reportGenerator.setClassesDirectory(new File(baseFile,dumpFilePath+"target/classes"));
                reportGenerator.setSourceDirectory(new File(baseFile,"source/src/main/java"));
                reportGenerator.setReportDirectory(new File(baseFile,dumpFilePath+"coveragereport"));
                if (executeRecords.isIncrement()){
                    String diffResut=baseFilePath+dumpFilePath+"difflog.txt";
                    List<String> diffClassFile = codeCompilingEngine.getDiffClassFile(codeRepository, executeRecords.getCodeBranch(), executeRecords.getDiffUrl(), diffResut);
                    reportGenerator.setChangefiles(diffClassFile);

                }

                reportGenerator.create();

                return reportPath;
            }

        }

        return null;
    }

    public boolean isLogin(String userName,String passwd){
        String passWord = userMapper.queryPasswdByUserName(userName);
        String encryption = MD5Tools.encryption(passwd);
        if(encryption.equals(passWord)){
            return true;
        }
        return false;
    }


    public String queryUserRole(String userName){
        return userMapper.queryUserRole(userName);
    }

}
