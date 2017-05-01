package cn.gtmap.platform.secuity.service;

import cn.gtmap.platform.secuity.LoginUser;
import cn.gtmap.platform.secuity.support.UserLoginException;

/**
 * Created by JIBO on 2016/5/17.
 */
public interface UserLoginService {

    /**
     * 根据用户名和密码返回用户唯一的标识
     * @param loginName
     * @param passWord
     * @return LoginUser
     */
    LoginUser getLoginUser(String loginName, String passWord) throws UserLoginException;
}
