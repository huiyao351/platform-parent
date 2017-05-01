package cn.gtmap.platform.secuity.support;

/**
 * Created by JIBO on 2016/5/17.
 */
public class UserLoginException extends Exception {

    public enum ErrorEnum{
        UserNotFound("用户不存在",101),
        UserPassWordNotRight("密码不正确",102),
        UserDisabled("用户无效",103);

        private String info;
        private int code;

        ErrorEnum(String info,int code){
            this.info=info;
            this.code=code;
        }

        @Override
        public String toString(){
            return info;
        }

        public int getCode(){
            return code;
        }
    }

    public UserLoginException(ErrorEnum error){
        super(error.toString());
    }

}
