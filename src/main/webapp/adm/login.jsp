<%@ page import="cong.common.dao.BaseDao" %>
<%@ page import="java.util.Map" %>
<%--
  Created by IntelliJ IDEA.
  User: cong
  Date: 2014/11/6
  Time: 21:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<!--[if lt IE 7 ]><html class="ie ie6" lang="zh-CN"> <![endif]-->
<!--[if IE 7 ]><html class="ie ie7" lang="zh-CN"> <![endif]-->
<!--[if IE 8 ]><html class="ie ie8" lang="zh-CN"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->
<html lang="zh-CN">
<!--<![endif]-->
<head>
  <%
    final BaseDao dao = new BaseDao();
    final Map<String, Object> siteMap = dao.queryOneMapBySQL("select siteName from t_site limit 0, 1");
    pageContext.setAttribute("siteName", siteMap.get("siteName"));
  %>
  <title>${siteName} 后台管理</title>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">
  <link rel="icon" href="/favicon.ico">

  <link href="http://cdn.bootcss.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">

  <!--[if lt IE 9]>
  <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
  <![endif]-->
  <%--<!--[if lt IE 9]>--%>
  <%--<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>--%>
  <%--<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>--%>
  <%--<![endif]-->--%>
  <link href="./css/index.css" rel="stylesheet">
  <link href="./css/common.css" rel="stylesheet">

  <script src="http://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
  <script src="http://cdn.bootcss.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
  <script src="./js/common.js"></script>


  <style>
    body {
      background: url("./img/swirl_pattern.png");
    }

    .loginBox {
      width: 420px;
      /*height: 290px;*/
      padding: 20px;
      border: 1px solid #fff;
      color: #000;
      margin-top: 40px;
      border-radius: 8px;
      background: #4892FF;
      box-shadow: 0 0 15px #222;
      /*background: -moz-linear-gradient(top, #fff, #efefef 8%);*/
      /*background: -webkit-gradient(linear, 0 0, 0 100%, from(#f6f6f6), to(#f4f4f4));*/
      font: 11px/1.5em 'Microsoft YaHei';
      position: absolute;
      left: 50%;
      top: 50%;
      margin-left: -210px;
      margin-top: -165px;
      background: url("./img/diamonds.png");
      -moz-box-shadow: 2px 2px 20px #bbb, -2px -2px 20px #bbb;
      -webkit-box-shadow: 2px 2px 20px #bbb, -2px -2px 20px #bbb;
      box-shadow: 2px 2px 20px #bbb, -2px -2px 20px #bbb;
    }

    .loginBox h2 {
      height: 45px;
      font-size: 23px;
      font-weight: normal;
      /*color: #FFFFFF;*/
      text-shadow: 1px 1px 50px #333;
    }

    .loginBox .form-group:hover input {
      -moz-box-shadow: 1px 1px 3px #009933, 0px -1px 3px #009933;
      -webkit-box-shadow: 1px 1px 3px #009933, 0px -1px 3px #009933;
      box-shadow: 1px 1px 3px #009933, 0px -1px 3px #009933;
    }

    .loginBox .form-group:hover .input-group-addon {
      -moz-box-shadow: 0px 1px 3px #009933, -1px -1px 3px #009933;
      -webkit-box-shadow: 0px 1px 3px #009933, -1px -1px 3px #009933;
      box-shadow: 0px 1px 3px #009933, -1px -1px 3px #009933;
    }

    .loginBox .form-group:hover button {
      -moz-box-shadow: 1px 1px 3px #009933, -1px -1px 3px #009933;
      -webkit-box-shadow: 1px 1px 3px #009933, -1px -1px 3px #009933;
      box-shadow: 1px 1px 3px #009933, -1px -1px 3px #009933;
    }

  </style>
</head>
<body>
<div class="container">
  <div class="loginBox row">
    <h2 class="text-center">${siteName}网站 后台管理</h2>

    <form name="login" action="" method="post" class="form-horizontal">
      <div class="form-group">
        <div class="col-sm-10 col-sm-offset-1 input-group">
          <div class="input-group-addon"><span class="glyphicon glyphicon-user"></span></div>
          <input name="username" class="form-control" type="text" placeholder="用户名">
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-10 col-sm-offset-1 input-group">
          <div class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></div>
          <input name="password" class="form-control" type="password" placeholder="密码">
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-11 col-sm-offset-1">
          <span class="alert">
            ${errorMsg}
          </span>
        </div>
      </div>
      <div class="form-group">
        <button class="col-sm-10 col-sm-offset-1 btn btn-success" type="submit" value="登陆">登 陆</button>
      </div>
    </form>
  </div>
</div>
<!--.content-->

</body>
</html>
