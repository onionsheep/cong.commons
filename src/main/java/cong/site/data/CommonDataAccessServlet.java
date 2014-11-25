package cong.site.data;

import cong.common.dao.BaseDao;
import cong.common.util.Pair;
import cong.common.util.WebUtil;
import cong.common.view.JSONView;
import cong.common.view.JspView;
import jodd.bean.BeanUtil;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servlet implementation class CommonDataControllerServlet
 * <br></br>
 * 通用的数据的增删改查方法<br></br>
 * HTTP 参数：<br></br>
 * <ul>
 * <li>v  : view 有json/jsp/redirect-referer可以选择，默认json</li>
 * <li>m  : model 数据类名字, 默认</li>
 * <li>t  : type 操作类型， 有list/add/delete/update/get可以选择，默认list</li>
 * <li>p  : page 页码， 在t=list时可用，默认1</li>
 * <li>ps : pageSize 页码大小，在t=list时可用，默认10</li>
 * <li>id : id 数据的id，在t=delete和t=get时可用</li>
 * </ul>
 */
public class CommonDataAccessServlet extends HttpServlet {
  public static final ConcurrentHashMap<String, Class<?>> nameClassMap
      = new ConcurrentHashMap<String, Class<?>>();

  static {
//    nameClassMap.put("ArtClass", ArtClass.class);
//    nameClassMap.put("Article", Article.class);
//    nameClassMap.put("Message", Message.class);
//    nameClassMap.put("Site", Site.class);
//    nameClassMap.put("Admin", Admin.class);
  }

  private static final long serialVersionUID = 1L;
  private static final Logger log = LoggerFactory
      .getLogger(CommonDataAccessServlet.class);


  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    HashMap<String, Object> resultMap = new HashMap<String, Object>();
    String view = request.getParameter("v");
    if (view == null) {
      view = "json";
    }
    String modelName = request.getParameter("m");
    if (StringUtil.isBlank(modelName)) {
      modelName = "ArtClass";
    }
    Class<?> modelClazz = null;
    modelClazz = nameClassMap.get(modelName);
    if (modelClazz == null) {
      try {
        modelClazz = Class.forName(modelName);
      } catch (ClassNotFoundException e) {
        log.error("{} 类未找到.", modelName);
        e.printStackTrace();
        return;
      }
    }
    if (modelClazz == null) {
      return;
    }

    BaseDao dao = new BaseDao();
    String type = request.getParameter("t");
    if (type == null) {
      type = "list";
    }

    if ("list".equals(type)) {

      final Pair<Integer, Integer> pageParam = WebUtil.getPageParam(request);

      Integer page = pageParam.getV1();
      Integer pageSize = pageParam.getV2();

      ArrayList<?> queryResult = dao.queryByPageMySQL(modelClazz, page, pageSize);
      resultMap.put("modelList", queryResult);
    } else if ("add".equals(type)) {
      Object object = WebUtil.getParameter(request, modelClazz);
      if (object == null) {
        return;
      }
      int result = dao.add(object);
      resultMap.put("result", result);

    } else if ("update".equals(type)) {
      Object object = WebUtil.getParameter(request, modelClazz);
      Boolean ignoreNull = WebUtil.getParameter(request, Boolean.class, "ignoreNull");
      int result = -1;
      if (ignoreNull != null && ignoreNull) {
        result = dao.updateIgnoreNull(object);
      } else {
        result = dao.update(object);
      }

      resultMap.put("result", result);
    } else if ("delete".equals(type)) {
      Integer id = WebUtil.getParameter(request, Integer.class, "id");
      if (id == null) {
        id = WebUtil.getParameterFromRequestStream(request, Integer.class);
      }
      if (id == null) {
        Object object = WebUtil.getParameter(request, modelClazz);
        if (object == null) {
          return;
        } else {
          int result = dao.delete(object);
          resultMap.put("result", result);
        }
      } else {
        int result = dao.deleteById(modelClazz, id);
        resultMap.put("result", result);
      }
    } else if ("get".equals(type)) {
      Integer id = WebUtil.getParameter(request, Integer.class, "id");
      if (id == null) {
        return;
      }
      Object object = dao.queryById(modelClazz, id);
      resultMap.put(modelName, object);
    } else if ("save".equals(type)) {
      Object object = WebUtil.getParameter(request, modelClazz);
      if (object == null) {
        return;
      }
      int result = -1;
      Integer id = (Integer) BeanUtil.getDeclaredProperty(object, "id");
      if (id != null) {
        Object objInDb = dao.queryById(modelClazz, id);
        if (objInDb != null) {
          result = dao.update(object);
        } else {
          result = dao.add(object);
        }
      } else {
        result = dao.add(object);
      }
      resultMap.put("result", result);
    }

    if ("json".equals(view)) {
      JSONView.render(request, response, resultMap);
    } else if ("jsp".equals(view)) {
      String jspPath = request.getParameter("jp");
      if (jspPath == null) {
        JspView.render(request, response, resultMap);
      } else {
        JspView.render(request, response, resultMap, jspPath);
      }
    } else if ("redirect-referer".equalsIgnoreCase(view)) {
      String referer = request.getParameter("referer");
      if (referer == null) {
        referer = request.getHeader("Referer");
      }
      if (referer == null) {
        referer = request.getContextPath();
      }
      response.sendRedirect(referer);
    }


  }
}
