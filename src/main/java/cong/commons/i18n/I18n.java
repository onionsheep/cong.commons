/**
 * 创建 @ 2013年7月25日 下午1:52:05
 * 
 */
package cong.commons.i18n;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * 
 */
public class I18n {

    private static Logger              log                   = LoggerFactory.getLogger(I18n.class);

    /**
     * 语言文件存放默认路径
     */
    protected static String            defaultResourcePrefix = "i18n.";

    /**
     * 静态的Map，保存不同模块 不同语言的 I18n实例
     */
    protected static HashMap<String, HashMap<Locale, I18n>> i18nMap               = new HashMap<String, HashMap<Locale, I18n>>();
    
    protected ResourceBundle           rb;

    /**
     * 获取模块对应的I18n,不存在自动加载
     * 
     * @param bundle 模块名称
     * @param locale 地区 例如zh_CN en_US等
     * @return I18n
     */
    public static I18n getI18n(String bundle, Locale locale) {
        HashMap<Locale, I18n> li18n = getLocalI18nmap(bundle);
        I18n i18n = li18n.get(locale);
        if (i18n == null) {
            i18n = new I18n(bundle, locale);
        }
        return i18n;
    }

    /**
     * 获取内部静态存储《区域，I18n》的Map
     * @param bundle 模块
     * @return 《区域，I18n》Map
     */
    protected static HashMap<Locale, I18n> getLocalI18nmap(String bundle) {
        HashMap<Locale, I18n> li18n = i18nMap.get(bundle);
        if(li18n == null){
            li18n = new HashMap<Locale, I18n>();
            i18nMap.put(bundle, li18n);
        }
        return li18n;
    }
    

    /**
     * 私有构造函数，同步的，新建后放入静态map一遍取出
     * 
     * @param bundle 模块
     * @param locale 区域
     */
    protected I18n(String bundle, Locale locale) {
        synchronized (this) {
            String baseName = defaultResourcePrefix + bundle;
            rb = ResourceBundle.getBundle(baseName, locale);
            log.debug("new i18n bundle of {} in {}", baseName, locale);
            HashMap<Locale, I18n> li18n = getLocalI18nmap(bundle);
            li18n.put(locale, this);
            log.debug("{}", this);
        }
    }

    /**
     * 获取key对应的字符串
     * @param key
     * @return key对应的字符串
     */
    public String get(String key) {
        return rb.getString(key);
    }

    /**
     * 
     * @return 当前对象使用的区域
     */
    public Locale getLocale() {
        return this.rb.getLocale();
    }

    /**
     * 更改语言文件默认存放位置，从新的路径加载语言文件
     * @param path 存放路径相对于classpath的 比如 WEB-INF/classes/resource/i18n 就是resource.i18n
     */
    public static void changeResourcePath(String path) {
        defaultResourcePrefix = path + ".";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("I18n : {rb:");
        Enumeration<String> e = rb.getKeys();
        while(e.hasMoreElements()){
            String key = e.nextElement();
            builder.append(key);
            builder.append(":");
            builder.append(rb.getString(key));
            builder.append(",");
        }
        builder.delete(builder.length() - 1, builder.length());
        builder.append("}");
        return builder.toString();
    }


}
