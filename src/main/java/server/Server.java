package server;


import common.orm.core.AbstractQuery;
import common.orm.core.QueryFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天服务器
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title main.server.Server
 * @date 2020/5/25 22:41
 * @modified by:
 */
public class Server {
    public static ConcurrentHashMap<String, Socket> clients = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Socket, DataOutputStream> outStream = new ConcurrentHashMap<>();
    public static AbstractQuery query = QueryFactory.createQuery();

    public static void main(String[] args) {
        try {
            System.out.println("服务器开启");
            // 创建服务端socket
            ServerSocket server = new ServerSocket(8088);
            while (true) {
                Socket client = server.accept();
                InetAddress address = client.getInetAddress();
                System.out.println("当前客户端的IP：" + address.getHostAddress());
                new Thread(new ServerThread(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
