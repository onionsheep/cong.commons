<%@ page import="cong.common.dao.BaseDao" %>
<%@ page import="jodd.util.StringUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cong
  Date: 2014/10/22
  Time: 9:7:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="zh"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8" lang="zh"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9" lang="zh"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="zh"> <!--<![endif]-->
<head>
  <%
    final BaseDao dao = new BaseDao();
    final Map<String, Object> siteMap = dao.queryOneMapBySQL("select siteName from t_site limit 0, 1");
    pageContext.setAttribute("siteName", siteMap.get("siteName"));

    final ArrayList<Map<String, Object>> functionList = dao.queryMapListBySQL(
        "select * from t_functions where enabled='enabled' and path is not null ");
    pageContext.setAttribute("functionList", functionList);

    String path = request.getParameter("path");
    if (StringUtil.isBlank(path)) {
      path = "user/info.jsp";
    }
    pageContext.setAttribute("path", path);

  %>
  <title>${siteName} 后台管理</title>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">
  <link rel="icon" href="/favicon.ico">

  <!--[if lt IE 9]>
  <script src="./js/vendor/html5shiv.min.js"></script>
  <script src="./js/vendor/respond.min.js"></script>
  <![endif]-->

  <link href="./bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="./css/index.css" rel="stylesheet">
  <link href="./css/common.css" rel="stylesheet">
  <link href="./codemirror/lib/codemirror.css" rel="stylesheet">

  <script src="./js/vendor/jquery.min.js"></script>
  <script src="./bootstrap/js/bootstrap.min.js"></script>
  <script src="./js/common.js"></script>
  <script src="./codemirror/lib/codemirror.js"></script>


</head>
<body>
<!--[if lte IE 8]>
<p class="browsehappy">
  您的浏览器已经过于老旧，存在漏洞且已经停止维护，无法正常使用本站所有功能。
  您可以到<a href="http://browsehappy.com/">BROWSE HAPPY</a>.寻找一款安全的现代浏览器使用。
</p>
<![endif]-->

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
              aria-expanded="false" aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">${siteName} 后台管理</a>
    </div>
    <div id="navbar" class="navbar-collapse collapse">
      <form class="navbar-form navbar-right">
        <button class="btn btn-success">Admin</button>
      </form>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#">Help</a></li>
      </ul>
    </div>
  </div>
</nav>

<div class="container-fluid">
  <div class="row">
    <div class="col-sm-3 col-md-2 sidebar">
      <ul class="nav nav-sidebar">
        <c:forEach var="fun" items="${functionList}">
          <li><a href="?path=${fun.path}">${fun.name}</a></li>
        </c:forEach>
      </ul>
    </div>
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      <jsp:include page="${path}"></jsp:include>
    </div>
  </div>
</div>
<script src="./js/${path}.js"></script>
</body>
</html>
