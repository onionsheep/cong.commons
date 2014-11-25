package cong.common.tag;

import cong.common.dao.BaseDao;
import jodd.util.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cong on 2014/5/17.
 */
public class DataTag extends SimpleTagSupport {

  private static final ConcurrentHashMap<String, Class<?>> _clazzMap = new ConcurrentHashMap<String, Class<?>>();

  static {
//    _clazzMap.put("Article", Article.class);
//    _clazzMap.put("ArtClass", ArtClass.class);
//    _clazzMap.put("Site", Site.class);
  }

  private String id;

  private String className;

  private String var;

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public void setId(String id) {
    this.id = id;
  }


  public void doTag() throws JspException, IOException {
    Integer idInt = Integer.parseInt(id);
    if (idInt == null || idInt <= 0) {
      idInt = 1;
    }

    if (StringUtil.isBlank(className)) {
      className = "Article";
    }

    Class<?> clazz = _clazzMap.get(className);

    //JspWriter out = getJspContext().getOut();
    BaseDao dao = new BaseDao();
    Object obj = dao.queryById(clazz, idInt);
    if (var != null) {
      getJspContext().setAttribute(var, obj);
    }
  }
}
