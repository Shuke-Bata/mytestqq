package client.window.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title PageManger
 * @date 2020/5/27 11:56
 * @modified by:
 */
public class PageManger {
    /**
     * 配置信息
     */
    private static final Configuration conf;

    static {
        conf = new Configuration();
        try {
            JSONObject jsonObject = JSON.parseObject(FileUtils.readFileToString(
                    new File(System.getProperty("user.dir") + "\\src\\main\\resources\\pageConfig.json")));
            for (String key : jsonObject.keySet()) {
                conf.appendConfig(key, jsonObject.getObject(key, ConfigInfo.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PageManger() {
    }

    /**
     * 返回页面的 ConfigInfo
     *
     * @return ConfigInfo
     */
    public static ConfigInfo getConfig(PageName pageName) {
        switch (pageName) {
            case LOGIN_PAGE:
                return conf.getConfigMap().get("loginPage");
            case REGISTER_PAGE:
                return conf.getConfigMap().get("registerPage");
            case HOME_PAGE:
                return conf.getConfigMap().get("homePage");
            case CHAT_PAGE:
                return conf.getConfigMap().get("chatPage");
            case ADD_FRIEND_PAGE:
                return conf.getConfigMap().get("addFriendPage");
            case ALTER_PAGE:
                return conf.getConfigMap().get("alterPage");
            case SURE_ADD_PAGE:
                return conf.getConfigMap().get("sureAddPage");
            case FRIEND_REQUEST_INFO_PAGE:
                return conf.getConfigMap().get("friendRequestInfoPage");
            default:
                return null;
        }

    }

}
