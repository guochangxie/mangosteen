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

}
