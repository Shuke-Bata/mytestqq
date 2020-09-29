package client.window.conf;

import java.util.HashMap;
import java.util.Map;

/**
 * 页面配置信息，map存放
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title Configuration
 * @date 2020/5/27 12:06
 * @modified by:
 */
public class Configuration {
    private final Map<String, ConfigInfo> configMap;

    public Configuration() {
        this.configMap = new HashMap<>();
    }

    /**
     * 往配置信息的map中
     *
     * @param key        键
     * @param configInfo 配置信息
     */
    public void appendConfig(String key, ConfigInfo configInfo) {
        configMap.put(key, configInfo);
    }

    public Map<String, ConfigInfo> getConfigMap() {
        return configMap;
    }
}
