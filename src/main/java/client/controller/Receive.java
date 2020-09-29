package client.controller;

import com.alibaba.fastjson.JSON;
import common.message.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title Receive
 * @date 2020/5/30 15:29
 * @modified by:
 */
public class Receive {
    private DataInputStream dataInputStream;

    public Receive(InputStream inputStream) {
        dataInputStream = new DataInputStream(inputStream);
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    /**
     * 接收消息
     *
     * @return Message
     */
    public synchronized Message receiveMessage() {
        try {
            String text = dataInputStream.readUTF();
            System.out.println(Thread.currentThread().getName() + "读取到消息" + text);
            return JSON.parseObject(text, Message.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
