package client;

import client.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;


/**
 * 聊天程序客户端入口
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title client.Client
 * @date 2020/5/23 8:49
 */
public class Client extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Socket client = new Socket("localhost", 8088);

            Controller controller = new Controller(client);
            controller.execute();
        } catch (IOException e) {
            System.out.println("无法连接服务器！");
            System.exit(0);
        }
    }
}
