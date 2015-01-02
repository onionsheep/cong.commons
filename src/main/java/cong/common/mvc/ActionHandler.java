/**
 * 创建 @ 2013年7月24日 上午11:35:54
 *
 */
package cong.common.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 */
public class ActionHandler {

    protected Map<String, Method> actionMap;

    /**
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     * @return 是否处理
     */
    public boolean handle(HttpServletRequest req, HttpServletResponse resp) {

        return false;
    }

}
