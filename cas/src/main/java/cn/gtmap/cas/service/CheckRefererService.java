package cn.gtmap.cas.service;

/**
 * Created by JIBO on 2016/5/31.
 */
public interface CheckRefererService {

    /**
     * 检查referer
     * @param url
     * @return
     */
    boolean checkReferer(String url);
}
