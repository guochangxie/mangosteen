package com.mangosteen.service;


import java.io.File;
import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/4/1012:15 PM
 */
public interface CodeRepository {

    /**
     * 从代码仓库下载代码
     * @param repository 本地文件路径
     * @param branch     要下载的分支地址
     * @return
     */
     boolean downLoad(File repository, String branch);

    /**
     * 从代码仓库更新代码
     * @param file 本地代码位置
     * @param branch 要更新的分支地址
     * @return
     */
     boolean update(File file, String branch);

    /**
     *
     * @param baseUrl
     * @param diffUrl
     * @param diffRestFile 对比结果文件
     * @return 变更的方法列表
     */
     List<String> diff(String baseUrl, String diffUrl,String diffRestFile);

}
