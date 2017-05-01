package cn.gtmap.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 用来读取系统配置项.
 * <p/>
 *
 */
public final class AppConfig {

    protected final static Log logger = LogFactory.getLog(AppConfig.class);

    private static ConfigPlaceholderResolver resolver = new ConfigPlaceholderResolver();
    private static Map appProperties = new HashMap();

    public static void setConfiguration(final Map properties) {
        resolver.setProps(appProperties = properties);
    }

    /**
     * 获取配置文件或系统参数，系统环境中的参数值，如server.url
     *
     * @param key 参数名称.
     * @return 参数值.
     */
    public static String getProperty(final String key) {
        Assert.notNull(key, "Argument 'key' must not be null.");
        return resolver.resolvePlaceholder(key);
    }

    /**
     * 获取配置文件中的整数参数值.
     *
     * @param key 参数名称.
     * @return 整数参数值.
     */
    public static int getIntProperty(final String key) {
        return getIntProperty(key, 0);
    }

    /**
     * 获取配置文件中的整数参数.如果文件中没有该参数就返回defaultValue.
     *
     * @param key          参数名称.
     * @param defaultValue 参数默认值.
     * @return 整数参数值.
     */
    public static int getIntProperty(final String key, int defaultValue) {
        String property = getProperty(key);
        int value = defaultValue;

        try {
            value = Integer.parseInt(property);
        } catch (NumberFormatException e) {
            logger.warn(e.toString());
        }

        return value;
    }

    /**
     * 获取配置文件中的布尔参数值.
     *
     * @param key 参数名称.
     * @return 布尔参数值.
     */
    public static boolean getBooleanProperty(final String key) {
        return getBooleanProperty(key, false);
    }

    /**
     * 获取配置文件中的布尔参数.如果文件中没有该参数就返回defaultValue.
     *
     * @param key          参数名称.
     * @param defaultValue 参数默认值.
     * @return 布尔参数值.
     */
    public static boolean getBooleanProperty(final String key, final boolean defaultValue) {
        String value = getProperty(key);

        if (null != value) {
            return "true".equalsIgnoreCase(value)
                    || "on".equalsIgnoreCase(value)
                    || "yes".equalsIgnoreCase(value)
                    || "1".equalsIgnoreCase(value);
        } else {
            return defaultValue;
        }
    }

    public static Map getProperties() {
        return appProperties;
    }


}
