package cn.gtmap.cas.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by JIBO on 2016/5/17.
 */
public interface TokenService {

    /**
     * 把token存起来，根据request
     * @param request
     * @param token
     */
    void putToken(HttpServletRequest request,String token);

    /**
     * 把token取出来，根据request
     * @param request
     * @return
     */
    String getToken(HttpServletRequest request);
}
