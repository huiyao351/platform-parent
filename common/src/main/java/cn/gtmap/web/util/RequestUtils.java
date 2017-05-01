
package cn.gtmap.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:yangxin@gtmap.cn">yangxin</a>
 * @version V1.0, 11-12-2
 */
public final class RequestUtils {
    static final Logger logger= LoggerFactory.getLogger(RequestUtils.class);
    private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");

    public static String getParameter(HttpServletRequest request, String name) {
        String query = request.getQueryString();
        if (query != null) {
            Matcher m = QUERY_PARAM_PATTERN.matcher(query);
            while (m.find()) {
                if (name.equals(m.group(1))) {
                    return m.group(3);
                }
            }
        }
        return null;
    }

    public static UrlPathHelper URL_PATH_HELPER = new UrlPathHelper() {
        @Override
        public String getLookupPathForRequest(HttpServletRequest request) {
            String key = request.getRequestURI() + "_lookupPath";
            String path = (String) request.getAttribute(key);
            if (path == null) {
                request.setAttribute(key, path = super.getPathWithinApplication(request));
            }
            return path;
        }
    };

    public static PathMatcher PATH_MATCHER = new AntPathMatcher();

    public static String getClientIP(HttpServletRequest request) {
        String xForwardedFor;
        xForwardedFor = StringUtils.trimToNull(request.getHeader("$wsra"));
        if (xForwardedFor != null) {
            return xForwardedFor;
        }
        xForwardedFor = StringUtils.trimToNull(request.getHeader("X-Real-IP"));
        if (xForwardedFor != null) {
            return xForwardedFor;
        }
        xForwardedFor = StringUtils.trimToNull(request.getHeader("X-Forwarded-For"));
        if (xForwardedFor != null) {
            int spaceIndex = xForwardedFor.indexOf(',');
            if (spaceIndex > 0) {
                return xForwardedFor.substring(0, spaceIndex);
            } else {
                return xForwardedFor;
            }
        }
        return request.getRemoteAddr();
    }

    public static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        int end = url.indexOf(".");
        if (end == -1)
            return "";
        int start = url.indexOf("//");
        return url.substring(start + 2, end);
    }

    public static String toJSON(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String formatUrl(String base, String path) {
        StringBuilder sb = new StringBuilder();
        if (!"/".equals(base)) {
            sb.append(base);
        }
        if (path.charAt(0) != '/') {
            sb.append("/");
        }
        sb.append(path);
        return sb.toString();
    }

//    public static String buildUrl(String path, Map<?, ?> args) {
//        if (StringUtils.isEmpty(path)) {
//            return Env.getUrl();
//        }
//        String url = path.startsWith("/") ? Env.getUrl(path) : path;
//        StringBuilder sb = new StringBuilder(url);
//        boolean isFirst = true;
//        if (args != null) {
//            for (Map.Entry<?, ?> entry : args.entrySet()) {
//                if (isFirst) {
//                    isFirst = false;
//                    sb.append(url.contains("?") ? "&" : "?");
//                } else {
//                    sb.append("&");
//                }
//                String key = encode(entry.getKey());
//                Object value = entry.getValue();
//                if (value == null) {
//                    sb.append(key);
//                } else if (value instanceof Iterable) {
//                    boolean isFirst1 = true;
//                    for (Object value1 : (Iterable) value) {
//                        if (isFirst1) {
//                            isFirst1 = false;
//                        } else {
//                            sb.append("&");
//                        }
//                        sb.append(key).append("=").append(encode(value1));
//                    }
//                } else if (value.getClass().isArray()) {
//                    boolean isFirst1 = true;
//                    for (Object value1 : (Object[]) value) {
//                        if (isFirst1) {
//                            isFirst1 = false;
//                        } else {
//                            sb.append("&");
//                        }
//                        sb.append(key).append("=").append(encode(value1));
//                    }
//                } else {
//                    sb.append(key).append("=").append(encode(value));
//                }
//            }
//        }
//        return sb.toString();
//    }

    public static String encode(Object url) {
        try {
            return URLEncoder.encode(url.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(),e);
        }
        return url.toString();
    }

    public static String decode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(),e);
        }
        return url;
    }

    public static boolean isPost(HttpServletRequest request) {
        return "POST".equals(request.getMethod());
    }

    public static boolean matchAny(HttpServletRequest request, UrlPathHelper urlPathHelper, PathMatcher pathMatcher, String[] patterns) {
        if (ArrayUtils.isNotEmpty(patterns)) {
            String lookupPath = urlPathHelper.getLookupPathForRequest(request);
            for (String pattern : patterns) {
                if (pathMatcher.match(pattern, lookupPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean matchAll(HttpServletRequest request, UrlPathHelper urlPathHelper, PathMatcher pathMatcher, String[] patterns) {
        if (ArrayUtils.isNotEmpty(patterns)) {
            String lookupPath = urlPathHelper.getLookupPathForRequest(request);
            for (String pattern : patterns) {
                if (!pathMatcher.match(pattern, lookupPath)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getString(HttpServletRequest request, String name) {
        return request.getParameter(name);
    }

    public static String getString(HttpServletRequest request, String name, String def) {
        String value = getString(request, name);
        return StringUtils.isEmpty(value) ? def : value;
    }

    public static Boolean getBool(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return StringUtils.isEmpty(value) ? null : BooleanUtils.toBooleanObject(value);
    }

    public static boolean getBool(HttpServletRequest request, String name, boolean def) {
        Boolean value = getBool(request, name);
        return value == null ? def : value;
    }

    public static Integer getInt(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return StringUtils.isEmpty(value) ? null : NumberUtils.createInteger(value);
    }

    public static int getInt(HttpServletRequest request, String name, int def) {
        Integer value = getInt(request, name);
        return value == null ? def : value;
    }

    public static Double getDouble(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return StringUtils.isEmpty(value) ? null : NumberUtils.createDouble(value);
    }

    public static double getDouble(HttpServletRequest request, String name, double def) {
        Double value = getDouble(request, name);
        return value == null ? def : value;
    }
}
