package com.mangosteen.service.impl;

import com.mangosteen.tools.SvnManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/3/2010:47 AM
 */
@Service("svnImpl")
public class SvnRepositoryImpl {

    private static final Logger logger = LoggerFactory.getLogger(SvnRepositoryImpl.class);

    public boolean checkOut(String sourceFile, String devBranch) {
        SvnManager svnManager=new SvnManager();
        svnManager.init();
        try {
            svnManager.checkOutModel(sourceFile,devBranch);
        } catch (Exception e) {
            logger.error("svn 检出失败，详情为:{}",e.getMessage());
            return false;
        }
        return true;
    }


    public boolean update(File file) {
        SvnManager svnManager=new SvnManager();
        svnManager.init();
        try {
            svnManager.updateModel(file);
        } catch (Exception e) {
            logger.error("svn 更新失败，详情为:{}",e.getMessage());
            return false;
        }
        return true;
    }

    public boolean doDiff(String baseUrl, String diffUrl, String resultFilePath) {
        SvnManager svnManager=new SvnManager();
        svnManager.init();
        try {
            svnManager.doDiff(baseUrl,diffUrl,resultFilePath);
        } catch (Exception e) {
            logger.error("svn diff fail：{}",e.getMessage());
            return false;
        }
        return true;
    }

    public List<String> paseDiffFile(String filePath) {
        List<String> changfiles=new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(new File(filePath));
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            String  line =null;
            while((line=bufferedReader.readLine()) != null){
                if (line.startsWith("Index:")&&(line.endsWith(".java"))){
                    String chanagefile= StringUtils.substringAfterLast(line,"Index:").trim();
                    changfiles.add(StringUtils.substringAfterLast(chanagefile,"/").replace(".java",".class"));
                }
            }

        } catch (Exception e) {
            logger.error("pase svn diffFile fail:{}",e.getMessage());
        }


        return changfiles;
    }
}
