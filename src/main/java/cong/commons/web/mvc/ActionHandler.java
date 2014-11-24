/**
 * 创建 @ 2013年7月24日 上午11:35:54
 * 
 */
package cong.commons.web.mvc;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * 
 */
public class ActionHandler {

    protected Map<String, Method> actionMap;

    /**
     * @param req
     * @param resp
     * @return
     */
    public boolean handle(HttpServletRequest req, HttpServletResponse resp) {

        return false;
    }

}
