package client.controller;


import com.alibaba.fastjson.JSON;
import common.message.Message;

import java.io.*;

/**
 * 发送消息
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title Send
 * @date 2020/5/27 15:00
 * @modified by:
 */
public class Send {
    private final DataOutputStream dataOutputStream;

    public Send(OutputStream outputStream) {
        dataOutputStream = new DataOutputStream(outputStream);
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void sendRequest(Message message) {
        try {
            String text = JSON.toJSONString(message);
            dataOutputStream.writeUTF(text);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
