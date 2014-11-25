package cong; /**
 * Created by cong on 2014/10/23.
 */

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


public class ContextListener implements ServletContextListener,
    HttpSessionListener, HttpSessionAttributeListener {

  // Public constructor is required by servlet spec
  public ContextListener() {
  }

  // -------------------------------------------------------
  // ServletContextListener implementation
  // -------------------------------------------------------
  public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
//    SQLCache.getSqlCache(ArtClass.class, null, "classID", null, null, null, null);
//    SQLCache.getSqlCache(Article.class, null, "autoID", null, null, null, null);
//    SQLCache.getSqlCache(Admin.class, null, "auto_id", null, null, null, null);
  }

  public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
  }

  // -------------------------------------------------------
  // HttpSessionListener implementation
  // -------------------------------------------------------
  public void sessionCreated(HttpSessionEvent se) {
      /* Session is created. */
  }

  public void sessionDestroyed(HttpSessionEvent se) {
      /* Session is destroyed. */
  }

  // -------------------------------------------------------
  // HttpSessionAttributeListener implementation
  // -------------------------------------------------------

  public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
  }

  public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
  }

  public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attibute
         is replaced in a session.
      */
  }
}
