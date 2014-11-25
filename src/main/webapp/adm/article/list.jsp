<%--
  Created by IntelliJ IDEA.
  User: cong
  Date: 2014/10/24
  Time: 11:4:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.wwl.entity.ArtClass" %>
<%@ page import="com.wwl.entity.Article" %>
<%@ page import="cong.common.util.Pair" %>
<%@ page import="cong.common.util.WebUtil" %>
<%@ page import="cong.common.dao.BaseDao" %>
<%@ page import="cong.common.util.Pager" %>
<%@ page import="jodd.util.StringUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div>
  <%
    String path = "article/list.jsp";
    pageContext.setAttribute("path", path);
    String classID = request.getParameter("classID");
    final Pair<Integer, Integer> pageParam = WebUtil.getPageParam(request, 15);
    final int pageNum = pageParam.getV1();
    final int pageSize = pageParam.getV2();
    pageContext.setAttribute("pageNum", pageNum);
    pageContext.setAttribute("pageSize", pageSize);
    ArrayList<Article> articleList;
    final BaseDao dao = new BaseDao();
    long totalCount;

    if (StringUtil.isBlank(classID) || "0".equals(classID)) {
      articleList = dao.queryByPageMySQL(Article.class, pageNum, pageSize,
          " where  deleted != 2 order by deleted desc , addTime desc");
      totalCount = dao.count(Article.class, " where deleted != 2");
    } else {
      articleList = dao.queryByPageMySQL(Article.class, pageNum, pageSize,
          " where classID = ? and deleted != 2 order by deleted desc , addTime desc", classID);
      totalCount = dao.count(Article.class, " where classID = ? and deleted != 2", classID);
    }
    pageContext.setAttribute("classID", classID);
    pageContext.setAttribute("articleList", articleList);
    pageContext.setAttribute("totalCount", totalCount);
    final long totalPage = Pager.getTotalPage(totalCount, pageSize);
    pageContext.setAttribute("totalPage", totalPage);

    final ArrayList<ArtClass> parentArtClassList = dao.queryBySQLWhereClause(ArtClass.class, " order by classOrder");
    pageContext.setAttribute("parentArtClassList", parentArtClassList);

  %>
  <div>
    <legend>新闻列表</legend>
    <form class="form-horizontal" role="form">
      <div class="row form-group">
        <label class="col-sm-2 control-label">选择栏目</label>

        <div class="col-sm-10">
          <select name="classID" class="form-control">
            <option value="0">所有栏目</option>
            <c:forEach items="${parentArtClassList}" var="ac">
              <option value="${ac.classID}" data-pid="${ac.parentID}" data-name="${ac.name}"
                ${ac.classID == classID ? "selected" : ""}>${ac.name}</option>
            </c:forEach>
          </select>
        </div>
      </div>
    </form>

    <table class="table table-striped table-bordered table-hover table-condensed">
      <tr>
        <th>新闻ID</th>
        <th>栏目</th>
        <th>标题</th>
        <th>管理员ID</th>
        <th>添加时间</th>
        <th>审核</th>
        <th colspan="3">操作</th>
      </tr>
      <c:forEach var="a" items="${articleList}">
        <tr>
          <td data-name="autoID" data-id="1">${a.autoID}</td>
          <td class="classID" data-classid="${a.classID}" data-name="classID">${a.classID}</td>
          <td data-name="title1">${fn:length(a.title1) > 40 ? fn:substring(a.title1, 0, 40) : a.title1}</td>
          <td data-name="adminID">${a.adminID}</td>
          <td data-name="addTime">${a.addTime}</td>
          <td data-name="passed">${a.passed == 1 ? "通过" : "未通过"}</td>

          <td><a href="?path=article/edit.jsp&autoID=${a.autoID}" class="edit">编辑</a></td>
          <td><a href="#" class="delete" data-id="${a.autoID}">删除</a></td>
          <td><a href="/show.jsp?aId=${a.autoID}&classID=${a.classID}" class="preview" data-id="${a.autoID}">预览</a></td>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="8">
          共${totalCount}条，共${totalPage}页，
          <a href="?path=${path}&classID=${classID}&p=1">首页</a>
          <c:if test="${pageNum > 1}">
            <a href="?path=${path}&classID=${classID}&p=${pageNum - 1}">上一页</a>
          </c:if>
          <c:if test="${pageNum < totalPage}">
            <a href="?path=${path}&classID=${classID}&p=${pageNum + 1}">下一页</a>
          </c:if>
          <a href="?path=${path}&classID=${classID}&p=${totalPage}">末页</a>
        </td>
      </tr>
    </table>
  </div>
</div>
