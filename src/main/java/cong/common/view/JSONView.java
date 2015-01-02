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

    public static void render(final HttpServletRequest req, final HttpServletResponse resp, final Object obj)
            throws IOException {
        if (lg.isDebugEnabled()) {
            lg.debug("JSONView: " + JSON.toJSONString(obj));
        }
        JSON.writeJSONStringTo(obj, resp.getWriter(), SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.PrettyFormat, SerializerFeature.BrowserCompatible,
                SerializerFeature.WriteNullStringAsEmpty);
        resp.getWriter().close();

    }

    public static void render(final HttpServletRequest req, final HttpServletResponse resp, final Object obj,
                              final PropertyFilter filter) throws IOException {
        SerializeWriter sw = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(sw);
        serializer.getPropertyFilters().add(filter);
        serializer.write(obj);
        String jSONString = sw.toString();
        PrintWriter writer = resp.getWriter();
        writer.append(jSONString);
        writer.close();
        if (lg.isDebugEnabled()) {
            lg.debug("JSONView: " + jSONString);
        }
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
