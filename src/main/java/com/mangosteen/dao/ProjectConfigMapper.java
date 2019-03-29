package com.mangosteen.dao;

import com.mangosteen.model.ProjectConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2018/12/2011:12 AM
 */
@Mapper
public interface ProjectConfigMapper {

    @Select("select projectId, serverIp, serverPort, serverContainer from tb_projectConfig where projectId=#{projectId} ")
    List<ProjectConfig> queryProjectConfigByProjectId(int projectId);

    @Select("SELECT projectId,serverIp,serverPort,serverContainer FROM tb_projectConfig tc JOIN tb_project tp ON tp.projectName=#{projectName} AND tc.serverIp=#{ip}")
    ProjectConfig queryProjectConfigByProjectNameAndIp(String projectName, String ip);

    @Select("SELECT DISTINCT " +
            "serverIp," +
            "serverPort," +
            "serverContainer " +
            "FROM " +
            "tb_projectConfig tc " +
            "JOIN tb_project tp ON tp.projectName = #{projectName} " +
            "AND tc.projectId = tp.id")
    List<ProjectConfig> queryProjectConfigByProjectName(String projectName);

}
