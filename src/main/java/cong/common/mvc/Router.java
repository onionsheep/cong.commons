/**
 * 创建 @ 2013年7月23日 上午11:57:31
 * 
 */
package cong.common.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * 
 */
public class Router implements Filter {

    protected String appContextPath;
    protected int appContextPathLength;
    
    private Logger log = LoggerFactory.getLogger(Router.class);
    
    protected String removeAppContectPath(String uri){
        String path = null;
        if(uri != null && uri.length() > appContextPathLength){
            path = uri.substring(appContextPathLength);
        }
        return path;
    }
    
    @Override
    public void init(FilterConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        appContextPath = context.getContextPath();
        appContextPathLength = appContextPath.length();
        log.debug("application context path is : {}", appContextPath);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 如果不是http请求，忽略
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        // 转化为HttpServlet请求与响应
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String requestPath = removeAppContectPath(req.getRequestURI());
        log.debug("request path is : {}", requestPath);
        
        ActionHandler handler = new ActionHandler();
        
        if(handler.handle(req, resp)){
            log.debug("action is performed.");
            return;
        }else{
            log.debug("action is not performed.");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
