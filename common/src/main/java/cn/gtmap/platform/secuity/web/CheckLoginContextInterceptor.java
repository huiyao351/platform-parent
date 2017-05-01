package cn.gtmap.platform.secuity.web;

import cn.gtmap.platform.secuity.LoginUser;
import cn.gtmap.platform.secuity.support.CookieUtil;
import cn.gtmap.platform.secuity.support.JwtHelper;
import cn.gtmap.web.util.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 单点登录拦截器，spring mvc用,使用jwt
 * Created by JIBO on 2016/5/17.
 */
public class CheckLoginContextInterceptor implements HandlerInterceptor {
    private String[] includes;   //需要登录的url列表
    private String[] excludes;    //不需要登录的、排除的url列表
    private String loginUrl;   //cas的登录地址
    private String checkLoginUrl;

    private UrlPathHelper urlPathHelper = RequestUtils.URL_PATH_HELPER;
    private PathMatcher pathMatcher = RequestUtils.PATH_MATCHER;
    private final String CACHE_KEY = "hi_" + hashCode() + "_";

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public void setCheckLoginUrl(String checkLoginUrl) {
        this.checkLoginUrl = checkLoginUrl;
    }

    //在请求处理之前进行调用
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if(needLogin(request)){
            //检查cookie信息
            try{
                String jwt= CookieUtil.getJwtFromCookie(request);
                if (StringUtils.isNoneBlank(jwt)){
                    String[] userInfo=JwtHelper.getUserByToken(jwt);
                    if (userInfo!=null && userInfo.length==2 && StringUtils.isNoneBlank(userInfo[0])){
                        LoginUser loginUser=new LoginUser(userInfo[0],userInfo[1]);
                        SecurityContext.getContext().setLoginUser(loginUser);
                        return true;
                    }
                }

            }catch (Exception ex){

            }
            sendRedirect(request, response, checkLoginUrl);
            return false;
        }
        return true;
    }

    //preHandle 方法的返回值为true 时才能被调用。
    // postHandle 方法，顾名思义就是在当前请求进行处理之后，也就是Controller 方法调用之后执行，但是它会在DispatcherServlet 进行视图返回渲染之前被调用
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    //当前对应的Interceptor 的preHandle 方法的返回值为true 时才会执行。
    // 该方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        SecurityContext.clearContext();
    }

    private boolean needLogin(HttpServletRequest request){
        Boolean need =true;
        String key = CACHE_KEY + request.getRequestURI();
        need = (Boolean) request.getAttribute(key);
        if (need != null) {
            return need;
        }
        //先排除不需要处理的请求
        need = !RequestUtils.matchAny(request, urlPathHelper, pathMatcher, excludes);
        if (need) {
            need = includes == null || RequestUtils.matchAny(request, urlPathHelper, pathMatcher, includes);//includes为空或者匹配的表示需要处理
        }
        request.setAttribute(key, need);
        return need;
    }

    public static final void sendRedirect(HttpServletRequest request,
                                          HttpServletResponse response,
                                          String url) throws IOException {
        String finalUrl;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            finalUrl=url;
        }else {
            finalUrl =  "http://" + request.getServerName()+ ":" + request.getServerPort() +url;
        }
        String strBackUrl = "http://" + request.getServerName() //服务器地址
                + ":"
                + request.getServerPort()           //端口号
                + request.getContextPath()      //项目名称
                + request.getServletPath();      //请求页面或其他地址

        if (StringUtils.isNotBlank(request.getQueryString())){
            strBackUrl= strBackUrl + "?" + request.getQueryString();//参数
        }
        finalUrl=finalUrl+"?url=" + RequestUtils.encode(strBackUrl);
        response.sendRedirect(response.encodeRedirectURL(finalUrl));
    }
}
