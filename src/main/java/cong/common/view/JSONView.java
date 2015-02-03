package cong.common.view;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 */
public class JSONView {

    private static final Logger lg = LoggerFactory.getLogger(JSONView.class);

    public static void render(final HttpServletRequest req, final HttpServletResponse resp, final Object obj,
                              final PropertyFilter filter, SerializerFeature... features) throws IOException {
        setJSONHeader(req, resp);
        if (lg.isDebugEnabled()) {
            lg.debug("JSONView: " + JSON.toJSONString(obj));
        }

        PrintWriter writer = resp.getWriter();
        SerializeWriter sw = new SerializeWriter(writer);
        JSONSerializer serializer = new JSONSerializer(sw);

        if(filter != null){
            serializer.getPropertyFilters().add(filter);
        }
        for (SerializerFeature feature : features) {
            serializer.config(feature, true);
        }
        serializer.write(obj);

        sw.close();
        writer.close();
    }

    private static void setJSONHeader(HttpServletRequest req, HttpServletResponse resp) {
        final String userAgent = req.getHeader("User-Agent");
        final int msieIndex = userAgent.indexOf("MSIE");
        boolean supportApplicationJSON = true;
        if(msieIndex >= 0){
            final char c = userAgent.charAt(msieIndex + 5);
            if(c <= '9' && c >= '5'){
                //根据UserAgent判断浏览器为IE5-IE9，不支持 application/json
                supportApplicationJSON = false;
            }
        }
        if(supportApplicationJSON) {
            resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        }else{
            resp.setHeader("Content-Type", "text/plain;charset=UTF-8");
        }
    }

    public static void render(final HttpServletRequest req, final HttpServletResponse resp, final Object obj)
            throws IOException {
        render(req, resp, obj, null, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.PrettyFormat, //SerializerFeature.BrowserCompatible,
                SerializerFeature.WriteNullStringAsEmpty);
    }

    public static void renderIgnoreFields(final HttpServletRequest req, final HttpServletResponse resp, final Object obj,
                                          final String... fields) throws IOException {
        JSONView.render(req, resp, obj, new PropertyFilter() {

            // @Override
            public boolean apply(final Object object, final String name, final Object value) {
                for (String field : fields) {
                    if (field.equals(name)) {
                        return false;
                    }
                }
                return true;
            }
        });
    }

    public static void renderFields(final HttpServletRequest req, final HttpServletResponse resp, final Object obj,
                                    final String... fields) throws IOException {
        JSONView.render(req, resp, obj, new PropertyFilter() {

            // @Override
            public boolean apply(final Object object, final String name, final Object value) {
                for (String field : fields) {
                    if (field.equals(name)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

}
