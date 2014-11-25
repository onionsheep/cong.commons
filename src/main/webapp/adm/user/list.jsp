<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.wwl.entity.Admin" %>
<%@ page import="cong.common.dao.BaseDao" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: cong
  Date: 2014/11/4
  Time: 15:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  BaseDao dao = new BaseDao();
  ArrayList<Admin> adminList = dao.queryAll(Admin.class);
  pageContext.setAttribute("adminList", adminList);
%>
<div>
  <legend>管理员列表</legend>
  <table class="table table-striped table-bordered table-hover table-condensed">
    <tr>
      <th>自动ID</th>
      <th>登陆ID</th>
      <th>密码</th>
      <th>名字</th>
      <th>部门</th>
      <th>角色</th>
      <th colspan="2">op</th>
    </tr>
    <c:forEach items="${adminList}" var="a">
      <tr>
        <td>${a.auto_id}</td>
        <td>${a.id}</td>
        <td>***</td>
        <td>${a.name}</td>
        <td>${a.dept}</td>
        <td>${a.rank}</td>
        <td><a href="?path=user/edit.jsp&op=edit&auto_id=${a.auto_id}">编辑</a></td>
        <td><a href="#" class="delete" data-id="${a.auto_id}">删除</a></td>
      </tr>
    </c:forEach>
  </table>
</div>