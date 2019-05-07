package com.mangosteen.service.impl;

import com.mangosteen.service.CodeRepository;
import com.mangosteen.tools.SvnManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
public class SvnRepositoryImpl implements CodeRepository {

    @Override
    public boolean downLoad(File repository, String branch) {

        SvnManager svnManager=new SvnManager();
        svnManager.init();
        try {
            svnManager.checkOutModel(repository,branch);
        } catch (Exception e) {
            log.error("svn 检出失败，详情为:{}",e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean update(File file, String branch) {
        SvnManager svnManager=new SvnManager();
        svnManager.init();
        try {
            svnManager.updateModel(file);
        } catch (Exception e) {
            log.error("svn 更新失败，详情为:{}",e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<String> diff(String baseUrl, String diffUrl,String diffRestFile) {
        SvnManager svnManager=new SvnManager();
        svnManager.init();
        try {
            svnManager.doDiff(baseUrl,diffUrl,diffRestFile);
        } catch (Exception e) {
            log.error("svn diff fail：{}",e.getMessage());

        }
        List<String> changfiles=new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(new File(diffRestFile));
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            String  line =null;
            while((line=bufferedReader.readLine()) != null){
                if (line.startsWith("Index:")&&(line.endsWith(".java"))){
                    String chanagefile= StringUtils.substringAfterLast(line,"Index:").trim();
                    changfiles.add(StringUtils.substringAfterLast(chanagefile,"/").replace(".java",".class"));
                }
            }

        } catch (Exception e) {
            log.error("pase svn diffFile fail:{}",e.getMessage());
        }


        return changfiles;
    }

}
