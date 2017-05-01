/*
 * Project:  hydroplat-parent
 * Module:   hydroplat-common
 * File:     SecurityContext.java
 * Modifier: yangxin
 * Modified: 2014-06-11 19:37
 *
 * Copyright (c) 2014 Mapjs All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent
 * or the registration of a utility model, design or code.
 */

package cn.gtmap.platform.secuity.web;


import cn.gtmap.platform.secuity.LoginUser;

/**
 * web上下文线程对象，用来处理登录信息
 */
public final class SecurityContext {

    private static ThreadLocal<SecurityContext> LOCAL = new InheritableThreadLocal<SecurityContext>() {
        @Override
        protected SecurityContext initialValue() {
            return new SecurityContext();
        }
    };

    private LoginUser loginUser;


    public static SecurityContext getContext() {
        return LOCAL.get();
    }

    public static void clearContext() {
        LOCAL.remove();
    }


    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    private SecurityContext() {
    }


    public static String getUserName(){
        return getContext().getLoginUser()!=null ? getContext().getLoginUser().getUserName() :null;
    }

    public static String getUserId(){
        return getContext().getLoginUser()!=null ? getContext().getLoginUser().getUserId() :null;
    }
}
