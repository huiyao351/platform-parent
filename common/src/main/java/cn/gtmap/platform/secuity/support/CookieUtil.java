package cn.gtmap.platform.secuity.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用来解析cookie，客户端的cookie
 * Created by JIBO on 2016/5/17.
 */
public class CookieUtil {
    static final Logger logger= LoggerFactory.getLogger(CookieUtil.class);
    static final  String COOKIENAME="jwt";

    /**
     * 将jwt放入客户端的cookie中
     * @param response
     * @param jwt
     */
    public static void setJwtToCookie(HttpServletResponse response,String jwt){
        Cookie cookieClient = new Cookie(COOKIENAME, jwt); //bake cookie
        cookieClient.setMaxAge(-1); //set expire time to 1000 sec
        cookieClient.setPath("/");
        cookieClient.setHttpOnly(true);   //禁止js读取，提高安全性
        response.addCookie(cookieClient); //put cookie in response
    }

    /**
     * 删除jwt从client response
     * @param response
     */
    public static void clearJwtFormCookie(HttpServletResponse response){
        Cookie cookieClient = new Cookie(COOKIENAME, null); //bake cookie
        cookieClient.setMaxAge(-1); //set expire time to 1000 sec
        cookieClient.setPath("/");
        cookieClient.setHttpOnly(true);   //禁止js读取，提高安全性
        response.addCookie(cookieClient); //put cookie in response
    }

    /**
     * 从客户端的cookie中获取jwt
     * @param request
     * @return
     */
    public static String getJwtFromCookie(HttpServletRequest request){
        if (request.getCookies()!=null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase(COOKIENAME))
                    return cookie.getValue();
            }
        }
        return null;
    }
}
