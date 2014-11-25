<%@ page import="com.wwl.entity.ArtClass" %>
<%@ page import="cong.common.dao.BaseDao" %>
<%@ page import="jodd.util.StringUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
  <%
    String pid = request.getParameter("pid");
    if (StringUtil.isBlank(pid)) {
      pid = request.getParameter("parentID");
      if (StringUtil.isBlank(pid)) {
        pid = "0";
      }
    }
    pageContext.setAttribute("pid", pid);
    final BaseDao dao = new BaseDao();
    final ArrayList<ArtClass> artClassList = dao.queryBySQLWhereClause(ArtClass.class, " where parentID = ? order by classOrder asc", pid);
    pageContext.setAttribute("artClassList", artClassList);
  %>
  <div>
    <legend>栏目列表</legend>
    <table class="table table-striped table-bordered table-hover table-condensed">
      <tr>
        <th>栏目ID</th>
        <th>栏目名</th>
        <th>关键字</th>
        <th>栏目类型</th>
        <th>英文名称</th>
        <th>显示顺序</th>
        <th>父级栏目ID</th>
        <th>栏目路径</th>
        <th>是否启用</th>
        <th colspan="3">操作</th>
      </tr>
      <c:forEach var="ac" items="${artClassList}" varStatus="vs">
        <tr>
          <td data-name="classID" data-id="1">${ac.classID}</td>
          <td data-name="name">${ac.name}</td>
          <td data-name="word">${ac.word}</td>
          <td data-name="classType">${ac.classType == 1 ? "多目":"单目"}</td>
          <td data-name="engName">${ac.engName}</td>
          <td data-name="classOrder">${ac.classOrder}</td>
          <td data-name="parentID">${ac.parentID}</td>
          <td data-name="path">${ac.path}</td>
          <td data-name="enabled">${ac.enabled}</td>
          <td><a href="?path=artClass/list.jsp&pid=${ac.classID}">子栏目</a></td>
          <td><a href="#" class="edit">编辑</a></td>
          <td><a href="#" class="delete" data-id="${ac.classID}">删除</a></td>
        </tr>
      </c:forEach>
      <tr>
        <td colspan="12">
          <a href="#" id="addArtClass">添加一个栏目</a>
          <c:if test="${pid != 0}">
            <a href="javascript:history.back();">返回</a>
          </c:if>
        </td>
      </tr>
    </table>
  </div>
</div>

<div class="modal fade" id="addArtClassModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
            class="sr-only">Close</span></button>
        <h4 class="modal-title">添加栏目</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" role="form" id="addArtClassForm">
          <div class="form-group">
            <label data-name="classID" class="col-sm-2 control-label">栏目ID</label>

            <div class="col-sm-10">
              <input name="classID" type="text" class="form-control" id="newclassID" placeholder="classID">
            </div>
          </div>
          <div class="form-group">
            <label data-name="name" class="col-sm-2 control-label">栏目名称</label>

            <div class="col-sm-10">
              <input name="name" type="text" class="form-control" id="newname" placeholder="name">
            </div>
          </div>
          <div class="form-group">
            <label data-name="word" class="col-sm-2 control-label">关键字</label>

            <div class="col-sm-10">
              <input name="word" type="text" class="form-control" id="newword" placeholder="word">
            </div>
          </div>
          <div class="form-group">
            <label data-name="classType" class="col-sm-2 control-label">栏目类型</label>

            <div class="col-sm-10">
              <label class="radio-inline">
                <input type="radio" name="classType" id="newclassType1" value="1" checked> 多目
              </label>
              <label class="radio-inline">
                <input type="radio" name="classType" id="newclassType2" value="2"> 单目
              </label>
            </div>
          </div>
          <div class="form-group">
            <label data-name="engName" class="col-sm-2 control-label">英文名称</label>

            <div class="col-sm-10">
              <input name="engName" type="text" class="form-control" id="newengName" placeholder="engName">
            </div>
          </div>
          <div class="form-group">
            <label data-name="classOrder" class="col-sm-2 control-label">显示顺序</label>

            <div class="col-sm-10">
              <input name="classOrder" type="text" class="form-control" id="newclassOrder" placeholder="classOrder">
            </div>
          </div>
          <div class="form-group">
            <label data-name="parentID" class="col-sm-2 control-label">父级栏目</label>

            <div class="col-sm-10">
              <%
                final ArrayList<ArtClass> parentArtClassList = dao.queryBySQLWhereClause(ArtClass.class, " where classType = 1 order by classOrder");
                pageContext.setAttribute("parentArtClassList", parentArtClassList);
              %>
              <select name="parentID" class="form-control">
                <option value="0">顶级栏目</option>
                <c:forEach items="${parentArtClassList}" var="ac">
                  <option value="${ac.classID}" data-pid="${ac.parentID}">${ac.name}</option>
                </c:forEach>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label data-name="path" class="col-sm-2 control-label">栏目路径</label>

            <div class="col-sm-10">
              <input name="path" type="text" class="form-control" id="newpath" placeholder="path">
            </div>
          </div>
          <div class="form-group">
            <label data-name="enabled" class="col-sm-2 control-label">是否启用</label>

            <div class="col-sm-10">
              <label class="radio-inline">
                <input type="radio" name="enabled" id="enabled1" value="1" checked> 启用
              </label>
              <label class="radio-inline">
                <input type="radio" name="enabled" id="enabled2" value="0"> 禁用
              </label>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" id="addArtClassOK" data-type="add">确定</button>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
