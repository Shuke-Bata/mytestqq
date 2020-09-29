package client.window;

import client.controller.Receive;
import client.controller.Send;
import client.window.conf.PageManger;
import client.window.conf.PageName;
import common.po.Account;
import common.po.Companion;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

/**
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title AddFriendPage
 * @date 2020/6/4 15:20
 * @modified by:
 */
public class AddFriendPage extends AbstractWindow {
    private Account me;
    private ListView friendlist;
    private Account findAccount;

    public AddFriendPage(Send send, Receive receive) {
        super(PageManger.getConfig(PageName.ADD_FRIEND_PAGE), send, receive);
        friendlist = ((ListView) $("friendList"));
        ((TextField) $("textInput")).setTooltip(new Tooltip("输入账号，回车查询"));
    }

    /**
     * 加载新增好友界面
     */
    public void loadAddFriendPage(Account me) {
        this.me = me;
        this.show();
    }

    private void searchFriend() {
        clear();
        String account = ((TextField) $("textInput")).getText().trim();
        ((TextField) $("textInput")).clear();
        if (account.equals("")) {
            alter.setText("您未输入账号！");
            alter.show();
        } else if (account.equals(me.getAccount())) {
            alter.setText("不能输入你自己的账号！");
            alter.show();
        } else {
            String sql = "select * from `account` where account=?";
            findAccount = (Account) query.queryUniqueRow(sql, Account.class, new Object[]{account});
            if (findAccount == null) {
                alter.setText("账号不存在！");
                alter.show();
            } else {
                SearchFriendItem searchFriendItem = new SearchFriendItem(me, findAccount);

                SureAddPage sureAddPage = new SureAddPage(send);
                sureAddPage.backPage = this;

                this.friendlist.getItems().add(searchFriendItem.getPane());
                this.friendlist.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 2) {
                            int selectedIndex = friendlist.getSelectionModel().getSelectedIndex();
                            if (selectedIndex == 0) {
                                String sql = "select * from `companion` where my_account=? and friend_account=?";
                                if (query.queryUniqueRow(sql, Companion.class,
                                        new Object[]{me.getEid(), findAccount.getEid()}) == null) {
                                    sureAddPage.loadPage(me, findAccount);
                                } else {
                                    alter.setText("该账号已经是你的好友！");
                                    alter.show();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void initEvent() {
        ((TextField) $("textInput")).setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchFriend();
            }
        });
    }

    @Override
    public void clear() {
        if (friendlist != null) {
            friendlist.getItems().clear();
        }
    }

    @Override
    public void closeWindow() {
        ((Button) $("close")).setTooltip(new Tooltip("关闭"));
        ((Button) $("close")).setOnAction(event -> {
            this.close();
            this.clear();
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
