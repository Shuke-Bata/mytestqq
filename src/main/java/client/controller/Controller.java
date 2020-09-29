package client.controller;

import client.window.LoginDialog;

import java.io.IOException;
import java.net.Socket;

/**
 * 该客户端程序的控制类
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title Controller
 * @date 2020/5/23 8:52
 * @modified by:
 */
public class Controller {
    private Send send;
    private Receive receive;

    public Controller(Socket client) {
        try {
            send = new Send(client.getOutputStream());
            receive = new Receive(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        new LoginDialog(send, receive).loadLogin();
    }
}
