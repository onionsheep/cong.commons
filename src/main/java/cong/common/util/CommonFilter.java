package cong.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet Filter implementation class RouterFilter
 */
public class CommonFilter implements Filter {

  private static final Logger log = LoggerFactory.getLogger(CommonFilter.class);

  private static Pattern nologURLPattern = Pattern.compile("^(.+[.])(png|gif|jpg|js(?!p)|css|jpeg)");

  /**
   * Default constructor.
   */
  public CommonFilter() {

  }

  /**
   * @see Filter#destroy()
   */
  @Override
  public void destroy() {

  }

  /**
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    //HttpServletResponse resp = (HttpServletResponse)response;

    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = req.getParameterMap();
    req.setAttribute("parameterMap", parameterMap);
    Map<String, String> paramMap = new HashMap<String, String>();
    for (Entry<String, String[]> e : parameterMap.entrySet()) {
      if (e.getValue().length > 0) {
        paramMap.put(e.getKey(), e.getValue()[0]);
      }
    }
    req.setAttribute("paramMap", paramMap);
    //String requestURI = req.getRequestURI();
    //根据uri进行分别处理

    if (log.isDebugEnabled()) {
      final String remoteHost = request.getRemoteHost();
      final StringBuffer requestURL = req.getRequestURL();
      log.debug("{} <= {}", remoteHost, requestURL);
      int paramSize = parameterMap.entrySet().size();
      if (paramSize > 0) {
        StringBuffer sb = new StringBuffer();
        for (Entry<String, String[]> e : parameterMap.entrySet()) {
          if (paramSize > 1) {
            sb.append("\n");
          }
          sb.append(e.getKey());
          sb.append(Arrays.toString(e.getValue()));
        }
        log.debug("param lists :{}", sb.toString());
      }

      Matcher matcher = nologURLPattern.matcher(requestURL);
      boolean matches = matcher.find();
      if (!matches) {
        //
      }
    }

    chain.doFilter(request, response);

  }

  /**
   * @see Filter#init(FilterConfig)
   */
  @Override
  public void init(FilterConfig fConfig) throws ServletException {
    fConfig.getServletContext().setAttribute("contextPath", fConfig.getServletContext().getContextPath());
  }

}
