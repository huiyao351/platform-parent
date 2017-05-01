package cn.gtmap.config;

import java.util.Collections;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 11-5-31
 */
public class ConfigPlaceholderResolver  {

    private Map props;

    public ConfigPlaceholderResolver(Map props) {
        this.props = props;
    }

    public ConfigPlaceholderResolver() {
        props = Collections.EMPTY_MAP;
    }

    public void setProps(Map props) {
        this.props = props;
    }

    public String resolvePlaceholder(String placeholderName) {
        String propVal = (String) props.get(placeholderName);
        if (propVal == null) {
            propVal = System.getProperty(placeholderName);
            if (propVal == null) {
                propVal = System.getenv(placeholderName);
            }
        }
        return propVal;
    }
}
