<%@ page import="jodd.util.StringUtil" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

  final ServletContext servletContext = session.getServletContext();
  final String rootPath = servletContext.getRealPath("/");
  final String contextPath = request.getContextPath();

  String codePath = request.getParameter("codePath");
  if (StringUtil.isBlank(codePath)) {
    codePath = request.getServletPath();
  }
  final String codeRealPath = servletContext.getRealPath(codePath);

  File codeFile = new File(codeRealPath);
  if (codeFile.exists() && codeFile.canRead()) {
    final InputStreamReader isr = new InputStreamReader(new FileInputStream(codeFile), "utf-8");
    final BufferedReader br = new BufferedReader(isr);
    final StringBuffer sb = new StringBuffer();
    char[] cbuf = new char[1024];
    int len = br.read(cbuf);
    while(len != -1){
      sb.append(cbuf, 0, len);
      len = br.read(cbuf);
    }
    pageContext.setAttribute("code", sb.toString());
  }

  pageContext.setAttribute("rootPath", rootPath);
  pageContext.setAttribute("contextPath", contextPath);

  pageContext.setAttribute("codePath", codePath);
  pageContext.setAttribute("codeRealPath", codeRealPath);
%>
<script src="./codemirror/mode/xml/xml.js"></script>
<script src="./codemirror/mode/javascript/javascript.js"></script>
<script src="./codemirror/mode/css/css.js"></script>
<script src="./codemirror/mode/vbscript/vbscript.js"></script>
<script src="./codemirror/mode/htmlmixed/htmlmixed.js"></script>
<div>
  <legend>代码编辑</legend>
  <form class="form-horizontal" role="form" method="post" id="codeeditform">
    <div class="form-group">
      <div class="col-sm-6">
        <div class="input-group">
          <div class="input-group-addon">rootPath</div>
          <input type="text" class="form-control" name="rootPath" placeholder="rootPath" value="${rootPath}">
        </div>
      </div>
      <div class="col-sm-6">
        <div class="input-group">
          <div class="input-group-addon">contextPath</div>
          <input type="text" class="form-control" name="contextPath" placeholder="contextPath"
                 value="${contextPath}">
        </div>
      </div>
    </div>
    <div class="form-group">
      <div class="col-sm-6">
        <div class="input-group">
          <div class="input-group-addon">codePath</div>
          <input type="text" class="form-control" name="codePath" placeholder="codePath" value="${codePath}">
        </div>
      </div>
      <div class="col-sm-6">
        <div class="input-group">
          <div class="input-group-addon">codeRealPath</div>
          <input type="text" class="form-control" name="codeRealPath" placeholder="codeRealPath"
                 value="${codeRealPath}">
        </div>
      </div>
    </div>

    <div class="form-group">
      <div class="col-sm-12">
        <textarea id="code">${code}</textarea>
      </div>
    </div>
  </form>
</div>

