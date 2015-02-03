/**
 * 2013年12月1日 下午10:48:36
 */
package cong.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import jodd.bean.BeanUtil;
import jodd.datetime.JDateTime;
import jodd.typeconverter.TypeConversionException;
import jodd.typeconverter.TypeConverterManager;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cong 刘聪 onion_sheep@163.com|onionsheep@gmail.com
 */
public class WebUtil {


    public static final String PARAM_SESSION_NAME = "_param_storage";
    private static final Logger LG = LoggerFactory.getLogger(WebUtil.class);

    public static <T> void saveParameterInSession(final HttpServletRequest req, final String name, final T t) {
        if (StringUtil.isNotBlank(name)) {
            final HttpSession session = req.getSession();
            ConcurrentHashMap<String, Object> param = (ConcurrentHashMap<String, Object>) session.getAttribute(PARAM_SESSION_NAME);
            if (param == null) {
                param = new ConcurrentHashMap<>();
                session.setAttribute(PARAM_SESSION_NAME, param);
            }
            if (t == null) {
                param.remove(name);
            } else {
                param.put(name, t);
            }
        }
    }

    public static void removeParameterFromSession(final HttpServletRequest req, final String name) {
        saveParameterInSession(req, name, null);
    }

    public static void removeAllParametersInSession(final HttpServletRequest req) {
        req.getSession().setAttribute(PARAM_SESSION_NAME, null);
    }

    public static <T> T getParameterFromSession(final HttpServletRequest req, final Class<T> clazz, final String name) {
        T t = null;
        final HttpSession session = req.getSession();
        ConcurrentHashMap<String, Object> param = (ConcurrentHashMap<String, Object>) session.getAttribute(PARAM_SESSION_NAME);
        if (param != null && StringUtil.isNotBlank(name)) {
            t = (T) param.get(name);
        }
        return t;
    }


    /**
     * 从request中取出某种类型的参数，取出第一个不是null的参数则返回，顺序按照名称列表中给出的参数
     *
     * @param req   Http请求
     * @param clazz 要取出的对象的类型
     * @param names 参数名称列表
     * @param <T>   泛型类型
     * @return clazz类型的对象，如果请求中无此对象，返回null
     */
    public static <T> T getParameter(final ServletRequest req, final Class<T> clazz, final String... names) {
        T t = null;
        for (final String name : names) {
            t = getParameter(req, clazz, name);
            if (t != null) {
                break;
            }
        }
        return t;
    }


    /**
     * 从request中取出某种类型的参数，把取出和转换包装了一下，仅支持基本类型对应的包装类型，以及其他常见非集合类型。
     *
     * @param req   Http请求
     * @param clazz 要取出的对象的类型
     * @param name  参数名称
     * @param <T>   泛型类型
     * @return clazz类型的对象，如果请求中无此对象，返回null
     */
    public static <T> T getParameter(final ServletRequest req, final Class<T> clazz, final String name) {
        T t = null;

        String paramString = req.getParameter(name);
        if (StringUtil.isBlank(paramString)) {
            if (Boolean.class.equals(clazz)) {
                t = TypeConverterManager.convertType(false, clazz);
            }
        } else if (clazz.equals(Date.class) && paramString.indexOf('T') == 10) {
            JDateTime jDateTime = new JDateTime();
            jDateTime.parse(paramString, "YYYY-MM-DDThh:mm");
            t = clazz.cast(jDateTime.convertToDate());
        } else {
            try{
                t = TypeConverterManager.convertType(paramString, clazz);
            }catch(TypeConversionException e){
                e.printStackTrace();
                //格式转换异常，捕获，返回null
            }

        }
        return t;
    }

    /**
     * 从request中获取指定类型的对象，支持JSON和参数列表两种方式，JSON优先。<br>
     * JSON 的情况，要求参数中json字符串的参数名与对象的类名严格相等。<br>
     * 参数列表的情况，要求参数中的参数名跟字段名一一对应。<br>
     * 仅支持比较简单的对象。
     *
     * @param req   Http请求
     * @param clazz 要取出的对象的类型
     * @param <T>   泛型类型
     * @return clazz类型的对象， 找不到或者发生异常，返回null
     */
    public static <T> T getParameter(final ServletRequest req, final Class<T> clazz) {

        T t = null;
        String tJSONString = req.getParameter(clazz.getSimpleName());
        //首先尝试读取参数为对象名称的整个的JSON字符串
        if (StringUtil.isNotBlank(tJSONString)) {
            t = JSON.parseObject(tJSONString, clazz);
        } else {
            //尝试从Request流中获取字符串，并转为JSON解析
            t = getParameterFromRequestStream(req, clazz);

            //如果没有从流中获取到对象，则新建一个
            if (t == null) {
                try {
                    t = clazz.newInstance();
                } catch (InstantiationException e) {
                    LG.error("创建对象异常");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    LG.error("创建对象异常");
                    e.printStackTrace();
                }
            }

            //如果无json字符串，Request流中也没有对象，则读取每个字段对应的值，传进一个新对象
            if (t != null) {
                Field[] fileds = clazz.getDeclaredFields();
                for (Field field : fileds) {
                    Class<?> fieldClass = field.getType();
                    String fieldName = field.getName();
                    Object fieldValue = getParameter(req, fieldClass, fieldName);
                    if (fieldValue != null) {
                        BeanUtil.setProperty(t, fieldName, fieldValue);
                    }
                }
            }
        }
        return t;
    }

    /**
     * 从Request流中获取JSON格式的参数，流应为JSON字符串，否则解析失败
     *
     * @param req   HttpRequest
     * @param clazz 参数对象类
     * @param <T>   参数对象类型
     * @return 如果有, 则返回该类型的一个对象，否则为null
     */
    public static <T> T getParameterFromRequestStream(ServletRequest req, Class<T> clazz) {
        T t = null;
        try {
            ServletInputStream is = req.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            char[] buf = new char[4096];
            StringBuffer sb = new StringBuffer();
            for (int len = 0; len != -1; len = br.read(buf)) {
                sb.append(buf, 0, len);
            }
            br.close();
            is.close();
            String requestString = sb.toString();
            if (StringUtil.isNotBlank(requestString)) {
                LG.debug("Request流字符串： {}", requestString);
                t = JSON.parseObject(requestString, clazz);
            }
        } catch (IOException e) {
            LG.error("读取Request流异常");
            e.printStackTrace();
        } catch (JSONException e) {
            LG.error("JSON解析错误");
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 从http request中取出某种类型的数组，如果request中取出来的值长度为1，则作为json字符串解析，大于1按照数组处理
     *
     * @param req   http request
     * @param clazz 类型
     * @param name  参数名称
     * @param <T>   泛型类型
     * @return T类型的List，或者null
     */
    public static <T> List<T> getParamerterAsList(final ServletRequest req, final Class<T> clazz, final String name) {
        List<T> tList = null;
        String[] stringValueArray = req.getParameterValues(name);
        if (stringValueArray.length == 1) {
            // 长度等于1，作为json字符串处理
            String tListString = stringValueArray[0];
            tList = JSON.parseArray(tListString, clazz);
        } else if (stringValueArray.length > 1) {
            // 长度大于1，作为数组处理
            tList = new ArrayList<T>();
            for (String tString : stringValueArray) {
                tList.add(TypeConverterManager.convertType(tString, clazz));
            }
        }
        return tList;
    }

    /**
     * 获取页码参数<br>
     * 页码对应参数名称：p，页码大小对应参数名称：ps。<br>
     * 默认页码1，默认页码大小10<br>
     *
     * @param req HTTP请求
     * @return PageParam&lt;页码，页大小&gt;
     */
    public static PageParam getPageParam(final ServletRequest req) {
        return getPageParam(req, 10);
    }

    public static PageParam getPageParam(final ServletRequest req, int defaultPageSize) {
        Integer page = getParameter(req, Integer.class, "p");
        Integer pageSize = getParameter(req, Integer.class, "ps");
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = defaultPageSize;
        }
        return new PageParam(page, pageSize);
    }
}
