package com.mangosteen.dao;

import com.mangosteen.model.Project;
import com.mangosteen.model.ProjectConfig;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProjectMapper {

    @Select("select id,projectName,projectGroup,codeBranch from tb_project")
    List<Project> queryAllProject();

    @Insert("INSERT INTO tb_project(`projectName`, `projectGroup`, `codeBranch`) VALUES " +
            "(#{projectName}, 'default', #{codeBranch});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int saveProject(Project project);

    @Insert("<script>"  +
            "INSERT INTO tb_projectConfig( `projectId`, `serverIp`, `serverPort`, `serverContainer`) VALUES " +
            "<foreach collection=\"projectConfigs\" item=\"item\" index=\"index\"  separator=\",\">" +
            "(#{item.projectId},#{item.serverIp},#{item.serverPort},#{item.serverContainer})" +
            "</foreach>" +
            "</script>")
    void saveProjectConfig(@Param("projectConfigs") List<ProjectConfig> projectConfigs);

    @Select("select id,projectName,projectGroup,codeBranch from tb_project where projectName=#{projectName}")
    Project queryProjectByName(String projectName);
}
