<%@ page import="com.wwl.dao.factory.DaoFactoryImpl" %>
<%@ page import="com.wwl.dao.inter.AdminDao" %>
<%@ page import="com.wwl.entity.Admin" %>
<%@ page import="cong.common.dao.BaseDao" %>
<%@ page import="jodd.util.StringUtil" %>
<%--
  Created by IntelliJ IDEA.
  User: cong
  Date: 2014/10/30
  Time: 21:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String password = request.getParameter("password");
  String newpassword = request.getParameter("newpassword");
  String newpasswordcheck = request.getParameter("newpasswordcheck");
  String errorMsg = "";
  if (!StringUtil.isAllBlank(password, newpassword, newpasswordcheck)) {
    //如果有三个参数之一
    BaseDao dao = new BaseDao();
    Admin user = (Admin) session.getAttribute("user");
    if (!user.getPwd().equals(Admin.encryptPwd(password))) {
      errorMsg = "旧密码错误";
    } else {
      if (!StringUtil.equals(newpassword, newpasswordcheck)) {
        errorMsg = "两次输入的新密码不一致";
      } else {
        DaoFactoryImpl df = new DaoFactoryImpl();
        AdminDao ad = df.getAdminDao();
        int i = ad.modifyPsd(user, newpassword);
        if (i == 1) {
          errorMsg = "修改成功";
        } else {
          errorMsg = "修改失败，服务器错误";
        }
      }
    }
  }

  pageContext.setAttribute("errorMsg", errorMsg);
%>
<div>
  <legend>修改密码</legend>
  <form class="form-horizontal" role="form" action="?path=user/chpwd.jsp" method="post">
    <div class="form-group">
      <label class="col-sm-2 control-label">原密码</label>

      <div class="col-sm-10">
        <input type="password" class="form-control" name="password" placeholder="password">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-2 control-label">新密码</label>

      <div class="col-sm-10">
        <input type="password" class="form-control" name="newpassword" placeholder="newpassword">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-2 control-label">新密码</label>

      <div class="col-sm-10">
        <input type="password" class="form-control" name="newpasswordcheck" placeholder="newpasswordcheck">
      </div>
    </div>
    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10">
        <button type="submit" class="btn btn-default">修改</button>
        <span class="bg-warning">${errorMsg}</span>
      </div>
    </div>
  </form>
</div>

