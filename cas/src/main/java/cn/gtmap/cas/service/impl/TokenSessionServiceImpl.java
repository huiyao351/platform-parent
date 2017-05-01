package cn.gtmap.cas.service.impl;

import cn.gtmap.cas.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by JIBO on 2016/5/17.
 */
public class TokenSessionServiceImpl implements TokenService {

    static  final String SESSIONNAME="_TOKEN";

    public void putToken(HttpServletRequest request, String token) {
        HttpSession session= request.getSession();
        request.getSession().setAttribute(SESSIONNAME,token);
    }

    public String getToken(HttpServletRequest request) {
        HttpSession session= request.getSession();
        return session.getAttribute(SESSIONNAME)!=null ? session.getAttribute(SESSIONNAME).toString() :null;
    }


}
