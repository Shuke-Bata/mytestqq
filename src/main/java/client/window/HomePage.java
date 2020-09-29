package client.window;


import client.controller.Receive;
import client.controller.Send;
import client.util.PlaySound;
import client.window.conf.PageManger;
import client.window.conf.PageName;
import common.message.Message;
import common.message.MessageType;
import common.po.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.WindowEvent;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 主界面
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title HomePage
 * @date 2020/5/24 10:58
 * @modified by:
 */
public class HomePage extends AbstractWindow {
    public static Map<String, ChatWindow> chatWindow;
    public static Map<String, Boolean> chatOpen;
    private boolean isRunning;
    /**
     * 我的账号
     */
    private Account meAccount;
    /**
     * 好友列表
     */
    List<Account> friendAccountList;
    List<String> remarkList;
    /**
     * 好友面板map
     */
    Map<Account, FriendItem> friendItemMap;
    /**
     * 消息列表
     */
    Map<Integer, List<Offline_message>> messageListMap;
    List<Pane> messagePanList;

    private ListView messageList;
    private TreeView group;

    public HomePage(Send send, Receive receive) {
        super(PageManger.getConfig(PageName.HOME_PAGE), send, receive);
        messageList = ((ListView) $("message"));
        group = ((TreeView) $("FriendList"));
        group.setVisible(false);
        group.setManaged(false);
        friendAccountList = new ArrayList<>();
        remarkList = new ArrayList<>();
        friendItemMap = new HashMap<>();
        messageListMap = new HashMap<>();
        messagePanList = new ArrayList<>();
        chatWindow = new ConcurrentHashMap<>();
        chatOpen = new ConcurrentHashMap<>();
    }

    /**
     * 加载主页
     */
    public void loadHomePage(Account account) {
        this.meAccount = account;
        isRunning = true;
        // 初始化提示信息
        initToolTip();
        // 初始化个人信息
        initPersonInfo();

        // 初始化好友信息
        initFriendInfo();
        // 初始化消息列表
        initMessage();

        this.show();
        openReceiveTask();
    }

    /**
     * 初始化提示信息
     */
    private void initToolTip() {
        ((Button) $("addFriend")).setTooltip(new Tooltip("添加好友"));
        ((Button) $("myHead")).setTooltip(new Tooltip("更多"));
    }

    /**
     * 初始化页面个人账户信息
     */
    private void initPersonInfo() {
        // 设置头像
        ((Button) $("myHead")).setStyle(
                String.format("-fx-background-image:url('/image/head/%s.jpg')", meAccount.getAvatar()));
        // 设置昵称
        ((Label) $("myAccount")).setText(meAccount.getNickname());
        // 设置个性签名
        ((Label) $("myLabel")).setText(meAccount.getSignature());
        // 设置背景
        ((Pane) $("Me")).setStyle(
                String.format("-fx-background-image:url('/image/background/%s.jpg')", meAccount.getBackground()));
    }

    /**
     * 初始化好友信息
     */
    private void initFriendInfo() {
        String sql = "select * from `companion` where my_account=?";
        String userSql = "select * from `account` where eid=?";
        String groupSql = "select * from `grouping` where account=?";
        String onlineSql = "select * from `online` where account=?";

        assert query != null;
        List<?> companionList = query.queryRows(sql, Companion.class, new Object[]{meAccount.getEid()});
        List<?> groupList = query.queryRows(groupSql, Grouping.class, new Object[]{meAccount.getEid()});
        if (companionList == null) {
            companionList = new ArrayList<>();
        } else if (groupList == null) {
            groupList = new ArrayList<>();
        }

        for (Object companion : companionList) {
            Object uniqueRow = query.queryUniqueRow(userSql,
                    Account.class, new Object[]{((Companion) companion).getFriend_account()});
            friendAccountList.add((Account) uniqueRow);
            remarkList.add(((Companion) companion).getRemark());
        }

        TreeItem<Object> root = new TreeItem<>("");

        for (int i = 0; i < groupList.size(); i++) {
            Grouping grouping = (Grouping) groupList.get(i);
            TreeItem<Object> item = new TreeItem<>(grouping.getGroup_name());
            root.getChildren().add(item);

            for (int j = 0; j < companionList.size(); j++) {
                Companion companion = (Companion) companionList.get(i);
                if (companion.getMygroup().equals(grouping.getGroup_name())) {
                    FriendItem friendItem = new FriendItem(
                            friendAccountList.get(j).getAvatar(), friendAccountList.get(j).getAccount(),
                            companion.getRemark(), friendAccountList.get(j).getSignature());

                    // 设置在线状态
                    Object o = query.queryUniqueRow(onlineSql, Online.class,
                            new Object[]{friendAccountList.get(j).getAccount()});
                    if (o != null) {
                        friendItem.setOnline();
                    }

                    TreeItem<Object> sunItem = new TreeItem<>(friendItem.getPane());
                    item.getChildren().add(sunItem);

                    friendItemMap.put(friendAccountList.get(j), friendItem);
                }
            }

        }


        this.group.setRoot(root);
        this.group.setShowRoot(false);

        this.group.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    try {
                        TreeItem<Pane> item = (TreeItem<Pane>) group.getSelectionModel().getSelectedItem();
                        Label label = (Label) item.getValue().getChildren().get(6);
                        String text = label.getText();

                        for (Account account1 : friendItemMap.keySet()) {
                            if (account1.getAccount().equals(text)) {
                                if (!HomePage.chatWindow.containsKey(account1.getAccount())) {
                                    ChatWindow chat = new ChatWindow(send, receive);
                                    chat.load(meAccount, account1, friendItemMap.get(account1), null);
                                    HomePage.chatWindow.put(account1.getAccount(), chat);
                                    chat.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                        @Override
                                        public void handle(WindowEvent event) {
                                            HomePage.chatOpen.put(account1.getAccount(), false);
                                        }
                                    });
                                }
                                HomePage.chatOpen.put(account1.getAccount(), true);
                            }
                        }
                    } catch (Exception ignored) {

                    }
                }
            }
        });

    }

    /**
     * 初始化消息列表
     */
    private void initMessage() {
        String messageSql = "select * from `offline_message` where to_account_id=?";
        assert query != null;
        List<?> messageList = query.queryRows(messageSql, Offline_message.class, new Object[]{meAccount.getEid()});
        if (messageList == null) {
            messageList = new ArrayList<>();
        }

        for (Object o : messageList) {
            Offline_message offlineMessage = (Offline_message) o;
            Integer fromAccountId = offlineMessage.getFrom_account_id();
            if (messageListMap.containsKey(fromAccountId)) {
                messageListMap.get(fromAccountId).add(offlineMessage);
            } else {
                ArrayList<Offline_message> messages = new ArrayList<>();
                messages.add(offlineMessage);
                messageListMap.put(fromAccountId, messages);
            }
        }

        for (Integer integer : messageListMap.keySet()) {
            for (Account value : friendAccountList) {
                if (value.getEid().equals(integer)) {
                    messagePanList.add(friendItemMap.get(value).getPane());
                }
            }
        }
        ObservableList<Pane> observableList = FXCollections.observableList(messagePanList);
        this.messageList.setItems(observableList);

        this.messageList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int selectedIndex = this.messageList.getSelectionModel().getSelectedIndex();
                Label label = (Label) messagePanList.get(selectedIndex).getChildren().get(6);
                String text = label.getText();

                for (Account account1 : friendItemMap.keySet()) {
                    if (account1.getAccount().equals(text)) {
                        if (!HomePage.chatWindow.containsKey(account1.getAccount())) {
                            ChatWindow chat = new ChatWindow(send, receive);
                            chat.load(meAccount, account1, friendItemMap.get(account1), messageListMap.get(account1.getEid()));
                            HomePage.chatWindow.put(account1.getAccount(), chat);
                            chat.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                @Override
                                public void handle(WindowEvent event) {
                                    HomePage.chatOpen.put(account1.getAccount(), false);
                                }
                            });
                        } else {
                            HomePage.chatWindow.get(account1.getAccount()).show();
                        }
                        HomePage.chatOpen.put(account1.getAccount(), true);
                    }
                }
            }
        });

    }

    /**
     * 开启接收消息线程
     */
    private void openReceiveTask() {
        new Thread(() -> {
            while (isRunning) {
                //执行普通方法
                Message message = receive.receiveMessage();

                MessageType messageType = message.getMessageType();
                System.out.println(Thread.currentThread().getName() + "获取到消息");

                if (messageType == MessageType.ACCEPT) {
                    // 发送的好友请求被接受
                    initFriendInfo();
                } else if (messageType == MessageType.ADD_FRIEND) {
                    // 只会在空闲时刻执行
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // 接收到的好友请求
                            FriendRequestInfoPage friendRequestInfoPage = new FriendRequestInfoPage();
                            friendRequestInfoPage.loadPage(message);
                            // 告知对方已经接受
                            if (friendRequestInfoPage.accept) {
                                Message message1 = new Message();
                                message1.setMessageType(MessageType.ACCEPT);
                                message1.setFromAccount(meAccount);
                                message1.setToAccount(new ArrayList<Account>() {{
                                    add(message.getFromAccount());
                                }});
                                send.sendRequest(message1);

                                initFriendInfo();
                            }
                        }
                    });

                } else if (messageType == MessageType.LOGIN || messageType == MessageType.LOGOUT) {
                    // 好友告知己方，好友的状态，上线和离线，改变好友状态
                    for (Account value : friendAccountList) {
                        if (value.getAccount().equals(message.getFromAccount().getAccount())) {
                            FriendItem item = friendItemMap.get(value);
                            // 只会在空闲时刻执行
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (messageType == MessageType.LOGIN) {
                                        item.setOnline();
                                        PlaySound.playOnline();
                                    } else {
                                        item.setOutline();
                                    }
                                }
                            });
                            break;
                        }
                    }
                } else {
                    //  聊天消息接收处理

                    PlaySound.playMessage();
                    // 如果该消息是已经在消息列表中的好友发送的，就添加到消息队列
                    if (messageListMap.containsKey(message.getFromAccount().getEid())) {
                        Offline_message offlineMessage = new Offline_message();
                        offlineMessage.setMessage_time(new Timestamp(message.getDate().getTime()));
                        offlineMessage.setMessage(message.getMessage());
                        offlineMessage.setFrom_account_id(message.getFromAccount().getEid());
                        offlineMessage.setTo_account_id(meAccount.getEid());
                        // 将该消息添加到消息列表中
                        messageListMap.get(message.getFromAccount().getEid()).add(offlineMessage);

                        // 更新聊天界面
                        if (chatOpen.get(message.getFromAccount().getAccount())) {
                            // 只会在空闲时刻执行
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO 处理图片和文件未完成
                                    if (messageType == MessageType.CHAT_IMAGE) {
                                        chatWindow.get(message.getFromAccount().getAccount()).addLeftImage(
                                                message.getFromAccount().getAvatar(),
                                                message.getMessage(), message.getDate());
                                    } else if (messageType == MessageType.CHAT_FILE) {
                                        // chatWindow.get(message.getFromAccount().getAccount()).addLeftFile();
                                    } else {
                                        // 普通消息
                                        chatWindow.get(message.getFromAccount().getAccount()).addLeft(
                                                message.getFromAccount().getAvatar(),
                                                message.getMessage(), message.getDate());
                                    }

                                }
                            });
                        }

                    } else {
                        FriendItem friendItem = new FriendItem(message.getFromAccount().getAvatar(),
                                message.getFromAccount().getAccount(),
                                remarkList.get(friendAccountList.indexOf(message.getFromAccount())),
                                message.getFromAccount().getSignature());

                        friendItem.setOnline();

                        friendItemMap.put(message.getFromAccount(), friendItem);

                        Offline_message offlineMessage = new Offline_message();
                        offlineMessage.setMessage_time(new Timestamp(message.getDate().getTime()));
                        offlineMessage.setMessage(message.getMessage());
                        offlineMessage.setFrom_account_id(message.getFromAccount().getEid());
                        offlineMessage.setTo_account_id(meAccount.getEid());
                        ArrayList<Offline_message> messageArrayList = new ArrayList<>();
                        messageArrayList.add(offlineMessage);
                        messageListMap.put(message.getFromAccount().getEid(), messageArrayList);

                        // 只会在空闲时刻执行
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                messageList.getItems().add(friendItem.getPane());
                            }
                        });
                    }
                }
            }
        }, "主页消息线程").start();
    }


    /**
     * 初始化事件
     */
    @Override
    public void initEvent() {
        // 查找好友搜索框
        ((TextField) $("search")).setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchMyFriend();
            }
        });

        // 添加好友
        ((Button) $("addFriend")).setOnAction(event -> {
            new AddFriendPage(send, receive).loadAddFriendPage(meAccount);
        });

        ((Button) $("tabMessage")).setOnAction(event -> {
            ((Line) $("messageLine")).setVisible(true);
            ((Line) $("ListLine")).setVisible(false);
            ((Line) $("TrendLine")).setVisible(false);
            ((ListView) $("message")).setVisible(true);
            ((ListView) $("message")).setManaged(true);
            ((TreeView) $("FriendList")).setVisible(false);
            ((TreeView) $("FriendList")).setManaged(false);
        });

        ((Button) $("tabList")).setOnAction(event -> {
            ((Line) $("messageLine")).setVisible(false);
            ((Line) $("ListLine")).setVisible(true);
            ((Line) $("TrendLine")).setVisible(false);
            ((ListView) $("message")).setVisible(false);
            ((ListView) $("message")).setManaged(false);
            ((TreeView) $("FriendList")).setVisible(true);
            ((TreeView) $("FriendList")).setManaged(true);
        });
    }

    /**
     * 查找我的好友
     */
    private void searchMyFriend() {
        String account = ((TextField) $("search")).getText();
        ((TextField) $("search")).clear();
        // TODO 主面板查找好友待完善
    }


    @Override
    public void clear() {
    }

    @Override
    public void closeWindow() {
        ((Button) $("close")).setTooltip(new Tooltip("关闭"));
        ((Button) $("close")).setOnAction(event -> {
            Message logout = new Message();
            logout.setMessageType(MessageType.LOGOUT);
            logout.setFromAccount(meAccount);
            send.sendRequest(logout);
            this.isRunning = false;
            System.exit(0);
        });
    }

    @Override
    public void miniWindow() {
        ((Button) $("mini")).setTooltip(new Tooltip("最小化"));
        ((Button) $("mini")).setOnAction(event -> {
            setIconified(true);
        });
    }
}
