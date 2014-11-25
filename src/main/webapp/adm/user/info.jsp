<%--
  Created by IntelliJ IDEA.
  User: cong
  Date: 2014/10/30
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  Object user = session.getAttribute("user");
  pageContext.setAttribute("user", user);

%>
<div>
  <legend>用户信息</legend>
  <table class="table">
    <tr>
      <th>账号</th>
      <th>姓名</th>
      <th>角色</th>
      <th>部门</th>
    </tr>
    <tr>
      <td>${user.id}</td>
      <td>${user.name}</td>
      <td>${user.rank}</td>
      <td>${user.dept}</td>
    </tr>
  </table>

</div>