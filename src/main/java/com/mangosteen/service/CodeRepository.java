package com.mangosteen.service;

import java.io.File;
import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/3/2010:32 AM
 */

public interface CodeRepository {

    /**
     * 代码检出
     * @param sourceFile 检出到本地文件路径
     * @param devBranch 分支地址
     */
     void checkOut(String sourceFile,String devBranch);

    /**
     * 代码文件更新
     * @param file
     */
     void update(File file);

    /**
     * 分支比对
     * @param baseUrl 主干或者master版本地址
     * @param diffUrl 分支地址
     * @param resultFilePath 比对后结果文件路径
     * @return
     */
    boolean doDiff(String baseUrl, String diffUrl, String resultFilePath);


    /**
     * 解析diff后的日志文件
     * @param filePath diff日志路径
     * @return 发生变更的class列表
     */
    List<String> paseDiffFile(String filePath);



}
