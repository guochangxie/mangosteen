package com.mangosteen.dao;

import com.mangosteen.model.ExecuteRecords;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2018/12/204:29 PM
 */
@Mapper
public interface ExecuteRecordMapper {
    @Insert("INSERT INTO tb_executeRecords(`projectName`, `codeBranch`, `executeTime`, `serverIp`,`reportPath`,`diffUrl`) VALUES " +
            "(#{projectName}, #{codeBranch}, #{executeTime}, #{serverIp},#{reportPath},#{diffUrl});")
    void saveExecuteRecords(ExecuteRecords executeRecords);

    @Select("select id,projectName,codeBranch, executeTime,serverIp,reportPath from tb_executeRecords where projectName =#{projectName}  ORDER BY executeTime DESC")
    List<ExecuteRecords> queryExecuteRecordByProjectName(String projectName);

    @Select("select id,projectName,codeBranch, executeTime,serverIp,reportPath from tb_executeRecords where projectName =#{projectName}  ORDER BY executeTime DESC limit #{pageNumber},#{pageSize}")
    List<ExecuteRecords> queryExecuteRecordOnPage(String projectName, int pageNumber, int pageSize);

    @Select("select count(*) from tb_executeRecords where projectName =#{projectName}")
    int countExecuteRecordByProjectName(String projectName);

    @Select("<script> select id,projectName,codeBranch, executeTime,serverIp,reportPath from tb_executeRecords where projectName=#{projectName}  " +
            "<if test=\"branch !='' \"> and codeBranch =#{branch}</if> " +
            "<if test=\"beginTime != null\">and <![CDATA[ beginTime >= #{beginTime,jdbcType=DATE}  ]]> </if> " +
            "<if test=\"endTime != null\">and <![CDATA[ beginTime <= #{endTime,jdbcType=DATE}  ]]></if>  ORDER BY executeTime DESC limit #{currentPage},#{pageSize} </script>")
    List<ExecuteRecords> queryExecuteRecordByBranch(String projectName, String branch, String beginTime, String endTime,int currentPage,int pageSize);

    @Select("<script> select count(*) from tb_executeRecords where projectName =#{projectName}" +
            " <if test=\"branch !='' \"> and codeBranch =#{branch}</if></script>")
    int countExecuteRecordByProjectNameAndBranch(String projectName, String branch);
}
