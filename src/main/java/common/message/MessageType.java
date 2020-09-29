package common.message;

/**
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title MessageType
 * @date 2020/6/5 16:21
 * @modified by:
 */
public enum MessageType {
    /**
     * 登录
     */
    LOGIN,
    /**
     * 注销
     */
    LOGOUT,
    /**
     * 聊天
     */
    CHAT,
    /**
     * 聊天图片
     */
    CHAT_IMAGE,
    /**
     * 聊天文件
     */
    CHAT_FILE,
    /**
     * 加好友
     */
    ADD_FRIEND,
    /**
     * 对方接受好友申请
     */
    ACCEPT

}
