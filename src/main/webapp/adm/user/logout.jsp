<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cong
  Date: 2014/10/30
  Time: 22:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  session.removeAttribute("user");
  session.removeAttribute("manager_id");
  session.removeAttribute("ispassed");
%>

<h2><a href="/adm/login.jsp">已注销，点击此处重新登陆</a></h2>