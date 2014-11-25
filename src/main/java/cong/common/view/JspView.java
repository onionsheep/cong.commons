package cong.common.view;

import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
/**
 *把对象设置到request的“obj” attribute中，并转发请求道dest指定的jsp文件
 *
 *dest可以是
 *	绝对路径/WEB-INF/...
 *	相对/WEB-INF/views/的路径 a.jsp则转发到/WEB-INF/views/a.jsp
 *	空字符串或null，此时，假如Web请求是http://host:port/uri-p0/uri-p1?attr=val
 *		那么转发到的jsp文件就是/WEB-INF/views/uri-p0/uri-p1.jsp
 */

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 */
public class JspView {
  private static final Logger LG = LoggerFactory.getLogger(JspView.class);
  public static final String EXT_STRING = "jsp";
  public static String defaultViewPath = "/WEB-INF/views/";
  public static final String ATTR = "obj";
  public static final String CONTEXT_PATH_ATTR = "contextPath";
  public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";

  public static void render(final HttpServletRequest req, final HttpServletResponse resp,
                            final Object obj) throws ServletException, IOException {
    render(req, resp, obj, null);
  }

  @SuppressWarnings("unchecked")
  public static void render(final HttpServletRequest req, final HttpServletResponse resp,
                            final Object obj, final String dest) throws ServletException, IOException {
    resp.setHeader("Content-Type", DEFAULT_CONTENT_TYPE);
    Object attribute = req.getAttribute(ATTR);
    if ((attribute == null) || !(obj instanceof Map)) {
      req.setAttribute(ATTR, obj);
    } else if (attribute instanceof Map) {
      @SuppressWarnings("rawtypes")
      Map attrMap = (Map) attribute;
      @SuppressWarnings("rawtypes")
      Map objMap = (Map) obj;
      attrMap.putAll(objMap);
      req.setAttribute(ATTR, attrMap);
    }
    req.setAttribute(CONTEXT_PATH_ATTR, req.getContextPath());
    String jspPath = null;
    if (StringUtil.isBlank(dest)) {
      String requestUri = req.getRequestURI();
      int contextPathLength = req.getContextPath().length();
      String viewPath = requestUri.substring(contextPathLength + 1);
      jspPath = defaultViewPath + viewPath + "." + EXT_STRING;
    } else if (dest.startsWith("/")) {
      jspPath = dest;
    } else {
      jspPath = defaultViewPath + dest;
    }
    if (LG.isDebugEnabled()) {
      LG.debug("jspView: " + jspPath);
    }

    req.getRequestDispatcher(jspPath).include(req, resp);
  }

  public static String getDEFAULT_VIEW_PATH() {
    return defaultViewPath;
  }

  public static void setDEFAULT_VIEW_PATH(final String DEFAULT_VIEW_PATH) {
    defaultViewPath = DEFAULT_VIEW_PATH;
  }

}
