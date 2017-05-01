package cn.gtmap.cas.service.impl;

import cn.gtmap.platform.secuity.LoginUser;
import cn.gtmap.platform.secuity.service.UserLoginService;
import cn.gtmap.platform.secuity.support.UserLoginException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

/**
 * Created by JIBO on 2016/5/17.
 */
public class UserLoginDbServiceImpl implements UserLoginService {


    private JdbcTemplate jdbcT;
    private String sql;

    public void setJdbcT(JdbcTemplate jdbcT) {
        this.jdbcT = jdbcT;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public LoginUser getLoginUser(String loginName, String userPassWord) throws UserLoginException {
        LoginUser user=(LoginUser)
                jdbcT.queryForObject(sql,new String[]{loginName},new BeanPropertyRowMapper(LoginUser.class));
        if (user!=null) {
            if (!user.isValid()) {
                throw new UserLoginException(UserLoginException.ErrorEnum.UserDisabled);
            }else if (StringUtils.equals(user.getPassWord(), DigestUtils.md5Hex(userPassWord))){
                return user;
            }else {
                throw new UserLoginException(UserLoginException.ErrorEnum.UserPassWordNotRight);
            }
        }else{
            throw new UserLoginException(UserLoginException.ErrorEnum.UserNotFound);
        }
    }
}
