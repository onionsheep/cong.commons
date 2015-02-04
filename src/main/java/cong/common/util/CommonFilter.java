package cong.common.util;

import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 把contextPath设置到ServletContext中<br>
 * request中的参数列表parameterMap设置到request的parameterMap属性中，此参数的值为String[]<br>
 * 把parameterMap中的值的第一个拿出来，形成一个单一值的paramMap，并设置到request的paramMap属性中<br>
 * 读取初始化参数defaultCharset，并设置编码，若为空，则默认为UTF-8
 */
public class CommonFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CommonFilter.class);

    private String defaultCharset = "UTF-8";

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
        HttpServletResponse resp = (HttpServletResponse) response;

        req.setCharacterEncoding(defaultCharset);
        resp.setCharacterEncoding(defaultCharset);

        Map<String, String[]> parameterMap = req.getParameterMap();
        req.setAttribute("parameterMap", parameterMap);
        Map<String, String> paramMap = new HashMap<String, String>();
        for (Entry<String, String[]> e : parameterMap.entrySet()) {
            if (e.getValue().length > 0) {
                paramMap.put(e.getKey(), e.getValue()[0]);
            }
        }
        req.setAttribute("paramMap", paramMap);


        if (log.isDebugEnabled()) {
            final String remoteHost = request.getRemoteHost();
            final StringBuffer requestURL = req.getRequestURL();
            final String xRealIP = req.getHeader("X-Real-IP");
            if(StringUtil.isNotBlank(xRealIP)){
                log.debug("{} <= {} <= {}",xRealIP, remoteHost, requestURL);
            }else{
                log.debug("{} <= {}", remoteHost, requestURL);
            }
            int paramSize = parameterMap.entrySet().size();
            if (paramSize > 0) {
                StringBuffer sb = new StringBuffer();
                for (Entry<String, String[]> e : parameterMap.entrySet()) {
                    sb.append(e.getKey());
                    sb.append(Arrays.toString(e.getValue()));
                    sb.append(" | ");
                }
                log.debug("param lists :{}", sb.toString());
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
        String charset = fConfig.getInitParameter("defaultCharset");
        if (StringUtil.isBlank(charset)) {
            charset = "UTF-8";
        } else {
            try {
                Charset.forName(charset);
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
                charset = "UTF-8";
            }
        }
        defaultCharset = charset;
    }

}
