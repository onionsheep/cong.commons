<%@ page import="com.wwl.entity.ArtClass" %>
<%@ page import="com.wwl.entity.Article" %>
<%@ page import="cong.common.dao.BaseDao" %>
<%@ page import="cong.common.util.WebUtil" %>
<%@ page import="jodd.util.StringUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  final String id = request.getParameter("autoID");
  final BaseDao dao = new BaseDao();

  final String op = request.getParameter("op");
  if ("save".equals(op)) {
    Article article = WebUtil.getParameter(request, Article.class);
    if (article.getAutoID() != null) {
      final int updateResult = dao.update(article);
      if (updateResult > 0) {
        pageContext.setAttribute("info", "修改成功");
      }
    } else {
      final int addResult = dao.add(article);
      if (addResult > 0) {
        pageContext.setAttribute("info", "添加成功");
      }
    }
  }

  if (StringUtil.isNotBlank(id)) {
    final Article a = dao.queryById(Article.class, id);
    pageContext.setAttribute("a", a);
  }


%>
<script src="./ueditor/ueditor.config.js"></script>
<script src="./ueditor/ueditor.all.min.js"></script>
<script src="./js/ueditor/uploadSwfDialog.js"></script>
<div>
  <form class="form-horizontal" role="form" method="post" id="editform">

    <div class="form-group">
      <div class="col-sm-6">
        <div class="input-group">
          <div class="input-group-addon">栏目</div>
          <%
            final ArrayList<ArtClass> parentArtClassList = dao.queryBySQLWhereClause(ArtClass.class, " order by classOrder");
            pageContext.setAttribute("parentArtClassList", parentArtClassList);
          %>
          <select name="classID" class="form-control">
            <option value="0">顶级栏目</option>
            <c:forEach items="${parentArtClassList}" var="ac">
              <option value="${ac.classID}"
                      data-pid="${ac.parentID}" ${a.classID == ac.classID ? 'selected': ''}>${ac.name}</option>
            </c:forEach>
          </select>
        </div>
      </div>
      <div class="col-sm-3">
        <div class="input-group">
          <div class="input-group-addon">时间</div>
          <jsp:useBean id="now" class="java.util.Date" />
          <fmt:formatDate var="addTime" value="${empty a.addTime ? now : a.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
          <%--2014-11-12 21:25:04.0--%>
          <input type="text" class="form-control" name="addTime" placeholder="addTime" value="${addTime}">
                 <%--value="${empty a.addTime ? now : addTime}">--%>
        </div>
      </div>
      <div class="col-sm-3">
        <div class="input-group">
          <div class="input-group-addon">审核</div>
          <select class="form-control" name="passed">
            <option value="1" ${a.passed == 1 ? "selected" :""}>通过</option>
            <option value="0" ${a.passed == 0 ? "selected" :""}>不通过</option>
          </select>
        </div>
      </div>
    </div>

    <div class="form-group">
      <div class="col-sm-6">
        <div class="input-group">
          <div class="input-group-addon">标题</div>
          <input type="text" class="form-control" name="title1" placeholder="title1" value="${a.title1}">
        </div>
      </div>
      <div class="col-sm-2">
        <div class="input-group">
          <div class="input-group-addon">字体</div>
          <select class="form-control" name="titleFontType">
            <option value="宋体"          ${a.titleFontType == "宋体" ? "selected" : ""}>宋体</option>
            <option value="华文中宋"      ${a.titleFontType == "华文中宋" ? "selected" : ""} >华文中宋</option>
            <option value="黑体"          ${a.titleFontType == "黑体" ? "selected" : ""}>黑体</option>
            <option value="楷体_GB2312"   ${a.titleFontType == "楷体_GB2312" ? "selected" : ""}>楷体</option>
            <option value="仿宋_GB2312"   ${a.titleFontType == "仿宋_GB2312" ? "selected" : ""}>仿宋</option>
            <option value="隶书"          ${a.titleFontType == "隶书" ? "selected" : ""}>隶书</option>
            <option value="幼圆"          ${a.titleFontType == "幼圆" ? "selected" : ""}>幼圆</option>
          </select>
        </div>
      </div>
      <div class="col-sm-2">
        <div class="input-group">
          <div class="input-group-addon">字号</div>
          <select class="form-control" name="titleFontSize">
            <c:forEach begin="1" end="14" step="1" var="v">
              <option value="${v}" ${v==4 ? "selected" :""} >${v}px</option>
            </c:forEach>
          </select>
        </div>
      </div>
      <div class="col-sm-2">
        <div class="input-group">
          <div class="input-group-addon">颜色</div>
          <input class="form-control" type="color" name="titleFontColor" value="${a.titleFontColor}">
        </div>
      </div>
    </div>


    <div class="form-group">
      <div class="col-sm-12">
        <textarea class="form-control" id="editor" name="content">${a.content}</textarea>
      </div>
    </div>

    <div class="form-group">
      <div class="col-sm-6">
        <div class="input-group">
          <div class="input-group-addon">封面</div>
          <input type="text" class="form-control" name="flashImg" placeholder="flashImg" value="${a.flashImg}">
        </div>
      </div>
      <div class="col-sm-6">
        <div class="input-group">
          <div class="input-group-addon">外链</div>
          <input type="text" class="form-control" name="linkUrl" placeholder="linkUrl" value="${a.linkUrl}">
        </div>
      </div>
    </div>

    <div class="form-group">
      <div class="col-sm-6">
        <div class="input-group">
          <div class="input-group-addon">作者</div>
          <input type="text" class="form-control" name="author" placeholder="author" value="${a.author}">
        </div>
      </div>
      <div class="col-sm-6">
        <div class="input-group">
          <div class="input-group-addon">置顶</div>
          <select class="form-control" name="deleted">
            <option value="1" selected>不置顶</option>
            <option value="3">置顶</option>
          </select>
        </div>
      </div>
    </div>
    <input type="hidden" name="op" value="save">
    <c:if test="${not empty a}">
      <input type="hidden" name="autoID" value="${a.autoID}">
    </c:if>

    <div class="form-group">
      <div class="col-sm-12">
        <input type="submit" class="form-control btn btn-primary">
      </div>
    </div>
    <div class="form-group">
      <div class="col-sm-12">
        <span class="alert">${info}</span>
      </div>
    </div>
  </form>
</div>
