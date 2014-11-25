<%@ page import="com.wwl.entity.Site" %>
<%@ page import="cong.common.dao.BaseDao" %>
<%--
  Created by IntelliJ IDEA.
  User: cong
  Date: 2014/10/30
  Time: 20:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  BaseDao dao = new BaseDao();
  Site site = dao.queryById(Site.class, 1);
  pageContext.setAttribute("site", site);
%>
<div>
  <legend>网站基本信息</legend>
  <form class="form-horizontal" role="form">
    <div class="form-group">
      <label class="col-sm-2 control-label">站点名称</label>

      <div class="col-sm-10">
        <input type="text" class="form-control" name="siteName" placeholder="siteName" value="${site.siteName}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-2 control-label">站点地址</label>

      <div class="col-sm-10">
        <input type="text" class="form-control" name="siteUrl" placeholder="siteUrl" value="${site.siteUrl}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-2 control-label">描述</label>

      <div class="col-sm-10">
        <input type="text" class="form-control" name="metaDescription" placeholder="metaDescription"
               value="${site.metaDescription}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-2 control-label">关键字</label>

      <div class="col-sm-10">
        <input type="text" class="form-control" name="metaKeyWords" placeholder="metaKeyWords"
               value="${site.metaKeyWords}">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-2 control-label">访问统计</label>

      <div class="col-sm-10">
        <input type="text" class="form-control" name="siteCount" placeholder="siteCount" value="${site.siteCount}">
      </div>
    </div>
    <input type="hidden" name="id" value="${site.id}">

    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10">
        <button type="submit" class="btn btn-default">修改</button>
      </div>
    </div>
  </form>
</div>
