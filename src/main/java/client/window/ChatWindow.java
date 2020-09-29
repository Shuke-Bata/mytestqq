package client.window;


import client.controller.Receive;
import client.controller.Send;
import client.window.conf.PageManger;
import client.window.conf.PageName;
import common.message.Message;
import common.message.MessageType;
import common.po.Account;
import common.po.Offline_message;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 聊天窗口界面
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title ChatWindow
 * @date 2020/5/24 11:27
 * @modified by:
 */
public class ChatWindow extends AbstractWindow {
    private Account me;
    private Account friend;
    private FriendItem friendItem;
    private ListView chatlist;

    private FileChooser fileChooser;

    public ChatWindow(Send send, Receive receive) {
        super(PageManger.getConfig(PageName.CHAT_PAGE), send, receive);
        chatlist = ((ListView) $("ChatList"));
        fileChooser = new FileChooser();
    }

    public void load(Account me, Account friend, FriendItem friendItem, List<Offline_message> messages) {
        this.me = me;
        this.friend = friend;
        this.friendItem = friendItem;
        if (messages != null) {
            for (Offline_message message : messages) {
                addLeft(friend.getAvatar(), message.getMessage(), message.getMessage_time());
                // TODO 接收到离线消息后删除数据库存储的消息，暂时关闭  query.delete(message);
            }
        }

        ((Button) $("friendMore")).setText(friendItem.getInformation().getText());
        this.show();

    }

    /**
     * 添加好友的消息到列表中
     *
     * @param fHead 好友头像
     * @param fMsg  好友消息
     */
    public void addLeft(String fHead, String fMsg, Date date) {
        chatlist.getItems().add(new ChatList().setLeft(fHead, fMsg,
                BubbleTool.getWidth(fMsg), BubbleTool.getHeight(fMsg), date));
    }

    /**
     * 添加好友的图片消息到列表中
     *
     * @param fHead 好友头像
     * @param fMsg  好友消息
     */
    public void addLeftImage(String fHead, String fMsg, Date date) {
        try {
            chatlist.getItems().add(new ChatList().setLeftImage(fHead, fMsg, date));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加我的图片消息到列表中
     *
     * @param fHead 好友头像
     * @param fMsg  好友消息
     */
    public void addRightImage(String fHead, String fMsg, Date date) {
        try {
            chatlist.getItems().add(new ChatList().setRightImage(fHead, fMsg, date));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加我的消息到消息列表中
     *
     * @param iHead 我的头像
     * @param iMsg  我的消息
     */
    public void addRight(String iHead, String iMsg, Date date) {
        chatlist.getItems().add(new ChatList().setRight(iHead, iMsg,
                BubbleTool.getWidth(iMsg), BubbleTool.getHeight(iMsg), date));
    }

    /**
     * 设置我的头像
     *
     * @param button 要设置的button
     * @param head   头像
     */
    public static void setHeadPorTrait(Button button, String head) {
        button.setStyle(String.format("-fx-background-image:url('/image/head/%s.jpg')", head));
    }

    public void setHead(String head) {
        setHeadPorTrait(((Button) $("myHead")), head);
    }


    /**
     * 发送消息按钮
     */
    private void sendMessage() {
        String text = ((TextField) $("input")).getText().trim();
        if (text.equals("")) {
            alter.setText("无法送内容！");
            alter.show();
        } else {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            addRight(me.getAvatar(), text, timestamp);
            if (friendItem.isOnline()) {
                ArrayList<Account> toAccounts = new ArrayList<>();
                toAccounts.add(friend);
                send.sendRequest(new Message(MessageType.CHAT, me, toAccounts, text, timestamp));
            } else {
                Offline_message offlineMessage = new Offline_message();
                offlineMessage.setFrom_account_id(me.getEid());
                offlineMessage.setTo_account_id(friend.getEid());
                offlineMessage.setMessage(text);
                offlineMessage.setMessage_time(timestamp);
                query.insert(offlineMessage);
            }
            ((TextField) $("input")).setText("");
        }
    }

    /**
     * 上传
     *
     * @param fileChooser fileChooser
     * @param type        上传类型 MessageType.CHAT_IMAGE 图片 MessageType.CHAT_FILE 文件
     * @param title       标题
     */
    private void upload(FileChooser fileChooser, MessageType type, String title) {
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File file = fileChooser.showOpenDialog(this);

        if (file != null) {
            String path = file.getAbsolutePath();

            Message message = new Message(type, me, new ArrayList<Account>() {{
                add(friend);
            }}, path, new Date());
            send.sendRequest(message);
            addRightImage(me.getAvatar(), path, new Date());
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public void closeWindow() {
        ((Button) $("close")).setTooltip(new Tooltip("关闭"));
        ((Button) $("close")).setOnAction(event -> {
            this.close();
        });
    }

    @Override
    public void miniWindow() {
        ((Button) $("mini")).setTooltip(new Tooltip("最小化"));
        ((Button) $("mini")).setOnAction(event -> {
            setIconified(true);
        });
    }

    @Override
    public void initEvent() {
        ((Button) $("quit2")).setOnAction(event -> {
            this.close();
        });
        ((Button) $("send")).setOnAction(event -> {
            sendMessage();
        });

        ((Button) $("friendMore")).setOnAction(event -> {
            // TODO 好友详情，待完成
        });

        ((Button) $("image")).setOnAction(event -> {
            upload(fileChooser, MessageType.CHAT_IMAGE, "选择图片");
        });
        ((Button) $("file")).setOnAction(event -> {
            upload(fileChooser, MessageType.CHAT_FILE, "选择文件");
        });


    }
}
