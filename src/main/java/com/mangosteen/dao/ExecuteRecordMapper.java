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
}
