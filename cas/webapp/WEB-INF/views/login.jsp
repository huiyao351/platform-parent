<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>登录页面</title>
    <link rel="stylesheet" href="./static/css/style.css">
    <!--[if lt IE 9]><script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
    <style>

        button{
            width: 300px;
            height: 42px;
            border: 0;
            display: inline-block;
            overflow: hidden;
            vertical-align: middle;
            line-height: 42px;
            font-size: 16px;
            font-weight: 700;
            color: #fff;
            background: #f40;
            border-radius: 3px;
            cursor: pointer;
            zoom: 1;
            font: 100% "Microsoft YaHei",tahoma,arial,'Hiragino Sans GB','\5b8b\4f53',sans-serif;
            align-items: flex-start;
            text-align: center;
        }
        button[type=submit]:active {
            background: #cde5ef;
            border-color: #9eb9c2 #b3c0c8 #b4ccce;
            -webkit-box-shadow: inset 0 0 3px rgba(0, 0, 0, 0.2);
            box-shadow: inset 0 0 3px rgba(0, 0, 0, 0.2);
        }
    </style>
</head>
<body>
<section class="container">
    <div class="login">
        <h1>Login to Web App</h1>
        <c:if test="${loginOk}">
        <form method="post" action="./logout">

            <button type="submit" class="J_Submit" tabindex="5" id="J_SubmitStatic">退出登录</button>
        </form>
        </c:if>
        <c:if test="${!loginOk}">
        <form method="post" action="./login">
            <p><input type="text" name="loginName" value="${loginName}" placeholder="用户名"></p>
            <p><input type="password" name="passWord" value="${passWord}" placeholder="密码"></p>
            <p class="remember_me">
                <label>
                    ${error_info}
                </label>
            </p>
            <p class="submit"><input type="submit" name="commit" value="登 录"></p>
        </form>
        </c:if>
    </div>

</section>


</body>
</html>