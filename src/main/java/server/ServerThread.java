package server;

import com.alibaba.fastjson.JSON;
import common.message.Message;
import common.po.Account;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title ServerThread
 * @date 2020/5/27 22:21
 * @modified by:
 */
public class ServerThread implements Runnable {
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Socket client;
    private boolean isRunning;
    private List<Message> addFriendMessage;

    public ServerThread(Socket client) {
        this.client = client;
        try {
            dataInputStream = new DataInputStream(client.getInputStream());
            dataOutputStream = new DataOutputStream(client.getOutputStream());
            isRunning = true;
            addFriendMessage = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                String text = dataInputStream.readUTF();
                Message message = JSON.parseObject(text, Message.class);
                switch (message.getMessageType()) {
                    case LOGIN:
                        login(message);
                        tellFriendLoginOrLogout(message);
                        break;
                    case CHAT:
                    case CHAT_FILE:
                    case CHAT_IMAGE:
                        chat(message);
                        break;
                    case LOGOUT:
                        logout(message.getFromAccount().getAccount());
                        tellFriendLoginOrLogout(message);
                        break;
                    case ADD_FRIEND:
                    case ACCEPT:
                        addFriend(message);
                        break;


                    default:
                }
            } catch (IOException e) {
                for (String account : Server.clients.keySet()) {
                    if (client.equals(Server.clients.get(account))) {
                        logout(account);
                    }

                }
            }
        }
    }

    /**
     * 处理登录
     *
     * @param message 消息
     */
    private void login(Message message) {
        Server.outStream.put(client, dataOutputStream);
        Server.clients.put(message.getFromAccount().getAccount(), client);
        System.out.println(message.getFromAccount().getAccount() + "登录");

        // 如果添加好友信息不为空，遍历添加好友信息列表，看是否是用户登录了，登录了就把好友申请发过去
        if (!addFriendMessage.isEmpty()) {
            int index = 0;
            boolean find = false;
            for (int i = 0; i < addFriendMessage.size(); i++) {
                if (addFriendMessage.get(i).getToAccount().get(0) == message.getFromAccount()) {
                    chat(message);
                    index = i;
                    find = true;
                    break;
                }
            }
            if (find) {
                addFriendMessage.remove(index);
            }
        }
    }

    /**
     * 处理注销
     *
     * @param account 账号
     */
    private void logout(String account) {
        Server.clients.remove(account);
        String sql = "delete from `online` where account=?";
        Server.query.delete(sql, new Object[]{account});
        System.out.println(account + "客户端退出");
        isRunning = false;
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理聊天,转发消息给其他用户
     *
     * @param message 消息
     */
    private void chat(Message message) {
        List<Account> toAccount = message.getToAccount();
        for (Account account : toAccount) {
            Socket socket = Server.clients.get(account.getAccount());
            try {
                String string = JSON.toJSONString(message);
                Server.outStream.get(socket).writeUTF(string);
                Server.outStream.get(socket).flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 告诉好友自己已经上线或者离线
     *
     * @param message
     */
    private void tellFriendLoginOrLogout(Message message) {
        String sql = "select * from `account` where account in(select account from `online` where account " +
                "in(select account from `account` where eid in(" +
                "select friend_account from `companion` where my_account=?)))";
        List<?> list = Server.query.queryRows(sql,
                Account.class, new Object[]{message.getFromAccount().getEid()});
        if (list != null) {
            message.setToAccount((List<Account>) list);
            // 告诉好友自己已经上线或者离线
            chat(message);
        }
    }

    /**
     * 添加好友信息，如果好友在线就把好友信息发送过去，离线就添加到新增好友信息列表
     */
    private void addFriend(Message message) {
        if (Server.clients.containsKey(message.getToAccount().get(0).getAccount())) {
            chat(message);
        } else {
            addFriendMessage.add(message);
        }

    }

}
