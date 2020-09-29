package client.window;

import client.window.conf.PageManger;
import client.window.conf.PageName;
import common.message.Message;
import common.po.Account;
import common.po.Companion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * 好友申请信息界面
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title FriendRequestInfoPage
 * @date 2020/6/6 16:14
 * @modified by:
 */
public class FriendRequestInfoPage extends AbstractWindow {
    boolean accept;
    private Account account;
    private Message message;

    public FriendRequestInfoPage() {
        super(PageManger.getConfig(PageName.FRIEND_REQUEST_INFO_PAGE));
    }

    public void loadPage(Message message) {
        this.message = message;
        this.account = message.getFromAccount();
        ((Button) $("head")).setStyle(
                String.format("-fx-background-image:url('/image/head/%s.jpg')", account.getAvatar()));
        ((Label) $("nickname")).setText(account.getNickname());
        ((Label) $("message")).setText(message.getMessage());

        this.show();
    }

    @Override
    public void clear() {

    }

    @Override
    public void closeWindow() {
        ((Button) $("cancel")).setOnAction(event -> {
            this.close();
        });
    }

    @Override
    public void miniWindow() {
    }

    @Override
    public void initEvent() {
        ((Button) $("submit")).setOnAction(event -> {
            String remark = ((TextField) $("remark")).getText().trim();
            // TODO 选择分组，未做
//            String group = ((ComboBox) $("group")).getSelectionModel().getSelectedItem().toString();
            Companion companion1 = new Companion();
            companion1.setMy_account(message.getToAccount().get(0).getEid());
            companion1.setFriend_account(message.getFromAccount().getEid());
            companion1.setRemark(remark);
            companion1.setMygroup("我的好友");
            query.insert(companion1);

            Companion companion2 = new Companion();
            companion2.setMy_account(message.getFromAccount().getEid());
            companion2.setFriend_account(message.getToAccount().get(0).getEid());
            companion2.setRemark(message.getToAccount().get(0).getNickname());
            companion2.setMygroup("我的好友");
            query.insert(companion2);
            accept = true;
            this.close();
        });

    }
}
