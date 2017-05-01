package cn.gtmap.platform.secuity;

import java.io.Serializable;

/**
 * Created by JIBO on 2016/5/17.
 */
public class LoginUser implements Serializable {

    String userId;
    String userName;
    String passWord;
    boolean valid;

    public LoginUser(){

    }

    public  LoginUser(String userId,String userName){
        this.userId=userId;
        this.userName=userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
