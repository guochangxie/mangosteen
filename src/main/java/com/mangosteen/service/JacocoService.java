package com.mangosteen.service;


import com.mangosteen.dao.UserMapper;
import com.mangosteen.model.ExecuteRecords;
import com.mangosteen.model.ProjectConfig;
import com.mangosteen.tools.FileUtils;
import com.mangosteen.tools.MD5Tools;
import com.mangosteen.tools.SvnManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.shared.invoker.*;
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

import java.util.Arrays;
import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2018/12/2811:34 AM
 */
@Service
public class JacocoService {

    private static final Logger logger = LoggerFactory.getLogger(JacocoService.class);

    @Value("${mangosteen.workspace}")
    private String mangosteenWorkSpace;

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private UserMapper userMapper;

    /**
     * 执行dump命令，获取代码覆盖率二进制文件
     * @param address 服务IP地址
     * @param port    服务端口
     */

    public boolean visitDumpCommand(String address,String port,String saveFile){

        logger.info("visitDump....");
        File file=new File(saveFile);
        if (!file.exists()){
            file.mkdirs();
        }
        ExecDumpClient client = new ExecDumpClient();
        client.setDump(true);
        ExecFileLoader execFileLoader = null;
        try {
            execFileLoader = client.dump(address, Integer.valueOf(port));
            execFileLoader.save(new File(saveFile+"jacoco-client.exec"), false);
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
     * svn 下载代码，并编译为class文件
     * @param devBranch
     * @return
     */
    public boolean readyClasses(String devBranch,String sourceFile){

        logger.info("svn init");
        boolean flag=true;
        File file=new File(sourceFile);
        //文件不存在则从svn下载，存在则从svn update
        if (!file.exists()){
            try {
                codeRepository.checkOut(sourceFile,devBranch);
            } catch (Exception e) {
                logger.error("svn 检出失败，详情为:{}",e.getMessage());
            }

        }else {
            codeRepository.update(file);
        }

        try {
            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile( new File( file,"pom.xml" ) );
            request.setGoals(Arrays.asList("clean","package -Dmaven.test.skip=true") );
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
            flag=false;
        }


        return flag;
    }

    public boolean doDidd(String baseUrl,String diffUrl,String resultFilePath){
        SvnManager svnManager=new SvnManager();
        svnManager.init();
        try {
            codeRepository.doDiff(baseUrl,diffUrl,resultFilePath);
        } catch (Exception e) {
            logger.error("svn diff fail：{}",e.getMessage());
            return false;
        }
       return true;
    }


    /**
     * 解析svndiff 后的差异文件，获取变更的文件类,将变更的文件切割到方法粒度
     * @param filePath
     * @return
     */
    // TODO: 2019/2/26
    /*public List<ChangeCode> paseDiffFile(String filePath){

        List<ChangeCode> changeCodes=new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(new File(filePath));
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            String  line =null;
            ChangeCode changeCode=null;
            while((line=bufferedReader.readLine()) != null){
                if (line.startsWith("Index:")){
                  changeCode=new ChangeCode();
                  changeCode.setFileName(StringUtils.substringAfterLast(line,"Index:").trim());
                  changeCodes.add(changeCode);
                }else if (StringUtils.contains(line,"@@")){
                    changeCode.addChangeLine(StringUtils.substringBetween(line,"+",","));
                }else {
                    String pattern = "(public|private|protected)+\\s(static)?\\s?(\\w+)\\s(\\w+)\\((\\w+.*\\w*)?\\)";

                    Pattern r = Pattern.compile(pattern);
                    Matcher matcher = r.matcher(pattern);
                    if(matcher.find()){
                        System.out.printf(line);
                        System.out.println("Found value: " + matcher.group(0) );
                        System.out.println("Found value: " + matcher.group(1) );
                        System.out.println("Found value: " + matcher.group(2) );
                        System.out.println("Found value: " + matcher.group(3) );
                    }

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return changeCodes;
    }
*/
    public List<String> paseDiffFile(String filePath){

        return codeRepository.paseDiffFile(filePath);
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

        String baseFilePath= mangosteenWorkSpace +"/"+executeRecords.getProjectName()+"/"+ StringUtils.substringAfterLast(executeRecords.getCodeBranch(),"/")+"/";
        boolean dumpFlag=true;
        if (readyClasses(executeRecords.getCodeBranch(),baseFilePath+"source")){
            String[] ips = executeRecords.getServerIp().split(",");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String dumpFilePath=simpleDateFormat.format(executeRecords.getExecuteTime())+"/";


            for (String ip:ips){
                boolean dumpResult = visitDumpCommand(StringUtils.substringBefore(ip,":"), StringUtils.substringAfter(ip,":"), baseFilePath + dumpFilePath);
                if(!dumpResult){
                    dumpFlag=false;
                }
            }

            if(dumpFlag){
                Path sourceTarget = Paths.get(baseFilePath + "source/target");
                Path reportTarget = Paths.get(baseFilePath + dumpFilePath);
                FileUtils.copyDir(sourceTarget,reportTarget);

                File baseFile=new File(baseFilePath);
                ReportGenerator reportGenerator=new ReportGenerator(baseFile);
                reportGenerator.setExecutionDataFile(new File(baseFile,dumpFilePath+"jacoco-client.exec"));
                reportGenerator.setClassesDirectory(new File(baseFile,dumpFilePath+"target/classes"));
                reportGenerator.setSourceDirectory(new File(baseFile,"source/src/main/java"));
                reportGenerator.setReportDirectory(new File(baseFile,dumpFilePath+"coveragereport"));
                if (StringUtils.isNotBlank(executeRecords.getDiffUrl())){
                    String diffResut=baseFilePath+dumpFilePath+"difflog.txt";
                    boolean doDidd = doDidd(executeRecords.getCodeBranch(), executeRecords.getDiffUrl(), diffResut);
                    if (doDidd){
                        List<String> paseDiffFile = paseDiffFile(diffResut);
                        reportGenerator.setChangefiles(paseDiffFile);
                    }
                }

                reportGenerator.create();
                String reportPath="/"+executeRecords.getProjectName()+"/"+ StringUtils.substringAfterLast(executeRecords.getCodeBranch(),"/")+"/"+dumpFilePath+"coveragereport/index.html";
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

}
