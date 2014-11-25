<%@ page import="com.wwl.entity.Admin" %>
<%@ page import="cong.common.dao.BaseDao" %>
<%@ page import="cong.common.util.WebUtil" %>
<%@ page import="jodd.util.StringUtil" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: cong
  Date: 2014/11/4
  Time: 10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

  Admin admin = WebUtil.getParameter(request, Admin.class);
  String op = request.getParameter("op");
  if ("save".equals(op) || "edit".equals(op)) {
    BaseDao dao = new BaseDao();
    if (admin.getAuto_id() != null) {
      //若auto_id不为空，则为update
      String pwd = admin.getPwd();
      if (StringUtil.isBlank(pwd)) {
        pageContext.setAttribute("info", "密码不能为空");
      } else {
        pwd = Admin.encryptPwd(pwd);
        admin.setPwd(pwd);
        int updateResult = dao.update(admin);
        if (updateResult > 0) {
          pageContext.setAttribute("info", "修改成功");
        } else {
          pageContext.setAttribute("info", "修改失败");
        }
      }
    } else {
      //若auto_id为空，则为add
      String id = admin.getId();
      if (StringUtil.isBlank(id)) {
        pageContext.setAttribute("info", "id不能为空");
      } else {
        ArrayList<Admin> adminList = dao.queryByField(Admin.class, "id", id);
        if (adminList.size() > 0) {
          pageContext.setAttribute("info", "id已存在，请换一个试试");
        } else {
          String pwd = admin.getPwd();
          if (StringUtil.isBlank(pwd)) {
            pageContext.setAttribute("info", "密码不能为空");
          } else {
            pwd = Admin.encryptPwd(pwd);
            admin.setPwd(pwd);
            int addresult = dao.add(admin);
            if (addresult > 0) {
              pageContext.setAttribute("info", "添加成功");
            } else {
              pageContext.setAttribute("info", "添加失败");
            }
          }
        }
      }
    }
    Integer auto_id = WebUtil.getParameter(request, Integer.class, "auto_id");
    if (auto_id != null) {
      admin = dao.queryById(Admin.class, auto_id);
      pageContext.setAttribute("admin", admin);
    }
  } else {
    pageContext.setAttribute("info", "");
  }


%>
<div>
  <legend>编辑</legend>
  <form class="form-horizontal" role="form" method="post" action="?path=user/edit.jsp">
    <div class="form-group">
      <label for="auto_id" class="col-sm-2 control-label">自动ID</label>

      <div class="col-sm-10">
        <input type="text" class="form-control" id="auto_id" name="auto_id" value="${admin.auto_id}"
               placeholder="auto_id, no need to fill" readonly>
      </div>
    </div>
    <div class="form-group">
      <label for="id" class="col-sm-2 control-label">登陆ID</label>

      <div class="col-sm-10">
        <input type="text" class="form-control" id="id" name="id" value="${admin.id}" placeholder="id">
      </div>
    </div>
    <div class="form-group">
      <label for="pwd" class="col-sm-2 control-label">密码</label>

      <div class="col-sm-10">
        <input type="password" class="form-control" id="pwd" name="pwd" value="***" placeholder="pwd">
      </div>
    </div>
    <div class="form-group">
      <label for="name" class="col-sm-2 control-label">名字</label>

      <div class="col-sm-10">
        <input type="text" class="form-control" id="name" name="name" value="${admin.name}" placeholder="name">
      </div>
    </div>
    <div class="form-group">
      <label for="dept" class="col-sm-2 control-label">部门</label>

      <div class="col-sm-10">
        <input type="text" class="form-control" id="dept" name="dept" value="${admin.dept}" placeholder="dept">
      </div>
    </div>
    <div class="form-group">
      <label for="rank" class="col-sm-2 control-label">角色</label>

      <div class="col-sm-10">
        <select class="form-control" name="rank" id="rank">
          <option value="3">3</option>
        </select>
      </div>
    </div>
    <input type="hidden" name="op" value="save">

    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10">
        <button type="submit" class="btn btn-default">确定</button>
        <span class="alert-info">${info}</span>
      </div>
    </div>
  </form>
</div>