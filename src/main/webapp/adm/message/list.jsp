<%--
  Created by IntelliJ IDEA.
  User: cong
  Date: 2014/11/12
  Time: 11:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.wwl.entity.ArtClass" %>
<%@ page import="com.wwl.entity.Message" %>
<%@ page import="cong.common.dao.BaseDao" %>
<%@ page import="cong.common.util.Pager" %>
<%@ page import="cong.common.util.Pair" %>
<%@ page import="cong.common.util.WebUtil" %>
<%@ page import="jodd.util.StringUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div>
  <%
    String path = "message/list.jsp";
    pageContext.setAttribute("path", path);
    final Pair<Integer, Integer> pageParam = WebUtil.getPageParam(request, 15);
    final int pageNum = pageParam.getV1();
    final int pageSize = pageParam.getV2();
    pageContext.setAttribute("pageNum", pageNum);
    pageContext.setAttribute("pageSize", pageSize);

    StringBuffer cndsb = new StringBuffer(" where 1=1 and ");

    String classID = request.getParameter("classID");
    if (StringUtil.isNotBlank(classID)) {
      cndsb.append(" classID = ? and ");
    }
    pageContext.setAttribute("classID", classID);

    final String replied = request.getParameter("replied");
    if(StringUtil.isBlank(replied)){
      //all
    }else if("unreplied".equals(replied)){
      cndsb.append(" reply is null or reply = '' and ");
    }else if("replied".equals(replied)){
      cndsb.append(" reply is not null and reply != '' and ");
    }
    pageContext.setAttribute("replied", replied);

    cndsb.delete(cndsb.length() - 5, cndsb.length() - 1);

    final BaseDao dao = new BaseDao();

    final String cnd = cndsb.toString();

    cndsb.append(" order by addTime desc");

    Long totalCount = 0l;
    ArrayList<Message> messageList = null;
    if(StringUtil.isBlank(classID)){
      messageList = dao.queryByPageMySQL(Message.class, pageNum, pageSize, cndsb.toString());
      totalCount = dao.count(Message.class, cnd);
    }else{
      messageList = dao.queryByPageMySQL(Message.class, pageNum, pageSize, cndsb.toString(), classID);
      totalCount = dao.count(Message.class, cnd, classID);
    }

    pageContext.setAttribute("messageList", messageList);
    pageContext.setAttribute("totalCount", totalCount);
    final long totalPage = Pager.getTotalPage(totalCount, pageSize);
    pageContext.setAttribute("totalPage", totalPage);
  %>
  <div>
    <legend>留言列表</legend>
    <form>
      <div class="row">
        <div class="col-sm-2">
          <a class="btn btn-primary form-control" href="?path=${path}&classID=${classID}&replied=replied">只看已回复的</a>
        </div>
        <div class="col-sm-2">
          <a class="btn btn-primary form-control" href="?path=${path}&classID=${classID}&replied=unreplied">只看未回复的</a>
        </div>
        <div class="col-sm-2">
          <a class="btn btn-primary form-control" href="?path=${path}&classID=${classID}&replied=careless">已回复与未回复都看</a>
        </div>
        <div class="col-sm-2">
          <select class="form-control" id="classFilter">
            <option value="?path=${path}&replied=${replied}" >所有类别</option>
            <option value="?path=${path}&replied=${replied}&classID=20900"
            ${classID == "20900" ? "selected" : ""}>
              ID=20900
            </option>
            <option value="?path=${path}&replied=${replied}&classID=1100"
            ${classID == "1100" ? "selected" : ""}>
              ID=1100
            </option>
          </select>
        </div>
      </div>
    </form>

    <table class="table table-striped table-bordered table-hover table-condensed">
      <tr>
        <th>留言ID</th>
        <th>姓名</th>
        <th>主题</th>
        <%--<th>内容</th>--%>
        <th>ip</th>
        <th>email</th>
        <th>qq</th>
        <%--<th>回复</th>--%>
        <th>留言时间</th>
        <%--<th>留言分类</th>--%>
        <th colspan="2">操作</th>
      </tr>
      <c:forEach var="m" items="${messageList}" varStatus="vs">
        <tr ${empty m.reply ? "class='info'" : ""} >
          <td data-name="id" data-id="1">${m.id}</td>
          <td data-name="name">${m.name}</td>
          <td data-name="topic">
            <c:if test="${fn:length(m.topic) > 20}">${fn:substring(m.topic, 0, 20)}</c:if>
            <c:if test="${fn:length(m.topic) <= 20}">${m.topic}</c:if>
          </td>
          <td data-name="ip">${m.ip}</td>
          <td data-name="email">${m.email}</td>
          <td data-name="qq">${m.qq}</td>
          <td data-name="addTime"><fmt:formatDate value="${m.addTime}" pattern="yyyy-MM-dd HH:mm"></fmt:formatDate></td>
          <td><a href="#" class="reply" data-id="${m.id}">回复</a></td>
          <td><a href="#" class="delete" data-id="${m.id}">删除</a></td>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="12">
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

<div class="modal fade" id="messageModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
            class="sr-only">Close</span></button>
        <h4 class="modal-title">回复留言</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" role="form" id="messageForm">
          <div class="form-group">
            <label data-name="name" class="col-sm-2 control-label">姓名</label>
            <div class="col-sm-10">
              <input name="name" type="text" class="form-control" id="modal-name" disabled>
            </div>
          </div>
          <div class="form-group">
            <label data-name="topic" class="col-sm-2 control-label">主题</label>
            <div class="col-sm-10">
              <input name="topic" type="text" class="form-control" id="modal-topic" disabled>
            </div>
          </div>
          <div class="form-group">
            <label data-name="content" class="col-sm-2 control-label">内容</label>
            <div class="col-sm-10">
              <textarea rows="8" name="content" type="text" class="form-control" id="modal-content" disabled></textarea>
            </div>
          </div>
          <div class="form-group">
            <label data-name="email" class="col-sm-2 control-label">电子邮件</label>
            <div class="col-sm-10">
              <input name="email" type="text" class="form-control" id="modal-email" disabled>
            </div>
          </div>
          <div class="form-group">
            <label data-name="qq" class="col-sm-2 control-label">QQ</label>
            <div class="col-sm-10">
              <input name="qq" type="text" class="form-control" id="modal-qq" disabled>
            </div>
          </div>

          <div class="form-group">
            <label data-name="reply" class="col-sm-2 control-label">回复</label>

            <div class="col-sm-10">
              <textarea rows="5" name="reply" class="form-control" id="modal-reply" placeholder="请输入内容。。。"></textarea>
            </div>
          </div>

          <input type="hidden" name="id" value="0" id="modal-id">
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" id="modal-ok-btn" data-type="update">确定</button>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
