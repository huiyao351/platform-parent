package cn.gtmap.cas.controller;

import cn.gtmap.cas.service.CheckRefererService;
import cn.gtmap.cas.service.TokenService;
import cn.gtmap.platform.secuity.LoginUser;
import cn.gtmap.platform.secuity.service.UserLoginService;
import cn.gtmap.platform.secuity.support.CookieUtil;
import cn.gtmap.platform.secuity.support.JwtHelper;
import cn.gtmap.platform.secuity.support.UserLoginException;

import cn.gtmap.platform.secuity.web.SecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;

/**
 * Created by JIBO on 2016/5/15.
 */
@Controller
public class LoginController {
    static String KEYNAME = "_token";
    static String COOKIE_NAME = "JWT";
    static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    TokenService tokenService;

    @Autowired
    UserLoginService userLoginService;

    @Autowired
    CheckRefererService checkRefererService;

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response,
                        String loginName, String passWord, String url, Model model) {
        //判断是否已经登录
        String jwt= CookieUtil.getJwtFromCookie(request);
        if (StringUtils.isNoneBlank(jwt)){
            String[] userInfo=JwtHelper.getUserByToken(jwt);
            if (userInfo!=null && userInfo.length==2 && StringUtils.isNoneBlank(userInfo[0])){
                model.addAttribute("loginOk", true);
                model.addAttribute("userName", userInfo[1]);
                return "login";
            }
        }

        if (StringUtils.isNotBlank(loginName)) {
            //检查referer
            if (checkRefererService.checkReferer(request.getHeader("Referer"))) {
                try {
                    LoginUser user = userLoginService.getLoginUser(loginName, passWord);
                    String jws = JwtHelper.buildUserLoginToken(user.getUserId(), user.getUserName());
                    tokenService.putToken(request, jws);
                    if (StringUtils.isNotBlank(url)) {
                        response.sendRedirect(response.encodeRedirectURL(url));
                    }
                    model.addAttribute("loginOk", true);
                    model.addAttribute("userName", user.getUserName());
                } catch (UserLoginException ex) {
                    model.addAttribute("error_info", ex.getMessage());
                } catch (IOException ex) {
                    model.addAttribute("error_info", ex.getMessage());
                    logger.error("登录错误！", ex);
                }
            } else {
                model.addAttribute("error_info", "网站禁止登录，请检查CAS配置");
                return "login";
            }

        }
        return "login";
    }


    /**
     * 判断是否登录，如果登录返回cookie
     *
     * @return
     */
    @RequestMapping(value = "/cookie")
    @ResponseBody
    public String getCookie(HttpServletRequest request, HttpServletResponse response) {
        String jwt = tokenService.getToken(request);
        if (StringUtils.isNotBlank(jwt)) {
            CookieUtil.setJwtToCookie(response, jwt);
            return "clientHandler('" + jwt + "');";
        } else {
            //压根没有登录，跳转吧
            return "clientHandler('0');";
        }
    }

    private static String getJWTFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase(cookieName))
                    return cookie.getValue();
            }
        }
        return null;
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.clearJwtFormCookie(response);
        return "login";
    }
}
