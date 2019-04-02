package com.mangosteen.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/3/264:23 PM
 */

@Mapper
public interface UserMapper {

    @Select("select passwd from tb_user where userName =#{userName}")
    String queryPasswdByUserName(String userName);

    @Select("SELECT " +
            "role " +
            "FROM " +
            " tb_role tr " +
            " JOIN tb_userrole tur ON tr.id = tur.roleId " +
            " JOIN tb_user tu ON tu.id = tur.userId " +
            " AND tu.userName = #{userName}")
    String queryUserRole(String userName);
}
