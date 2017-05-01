package cn.gtmap.cas.service.impl;

import cn.gtmap.cas.service.CheckRefererService;
import cn.gtmap.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by JIBO on 2016/5/31.
 */
public class CheckRefererServiceImpl implements CheckRefererService,InitializingBean {

    boolean enabled=false;

    String referers;

    List<String> refererList;

    final static String RefererCheck="cas.referer.check";
    final static String Referer="cas.referer";

    public void afterPropertiesSet() throws Exception {
        enabled=AppConfig.getBooleanProperty(RefererCheck,false);
        referers=AppConfig.getProperty(Referer);
        if (StringUtils.isNoneBlank(referers)){
            refererList= Arrays.asList(referers.split(","));
        }
    }

    public boolean checkReferer(String url) {
        if (enabled && refererList!=null) {
            String hostName = getServerNameFromUrl(url);
            return refererList.contains(hostName);
        }
        return true;
    }

    private static String getServerNameFromUrl(String url){
        try {
            java.net.URL urlObj = new URL(url);
            return urlObj.getHost();
        }catch (Exception ex){
            return null;
        }
    }
}
