package com.mangosteen.service;

import com.mangosteen.dao.UserMapper;
import com.mangosteen.tools.MD5Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/4/107:00 PM
 */
@Service
public class MangosteenUserService {
    @Autowired
    private UserMapper userMapper;

    public boolean isLogin(String userName,String passwd){
        String passWord = userMapper.queryPasswdByUserName(userName);
        String encryption = MD5Tools.encryption(passwd);
        if(encryption.equals(passWord)){
            return true;
        }
        return false;
    }


    public String queryUserRole(String userName){
        return userMapper.queryUserRole(userName);
    }
}
