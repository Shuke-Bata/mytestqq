package client.window;

import client.controller.Send;
import client.window.conf.PageManger;
import client.window.conf.PageName;
import common.message.Message;
import common.message.MessageType;
import common.po.Account;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.util.ArrayList;

/**
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title SureAddPage
 * @date 2020/6/6 14:51
 * @modified by:
 */
public class SureAddPage extends AbstractWindow {
    private Account me;
    private Account friend;

    public SureAddPage(Send send) {
        super(PageManger.getConfig(PageName.SURE_ADD_PAGE), send, null);
    }

    public void loadPage(Account me, Account friend) {
        this.me = me;
        this.friend = friend;
        this.show();
    }

    @Override
    public void clear() {
        ((TextArea) $("message")).setText("你好,我是,,,");
    }

    @Override
    public void closeWindow() {
        ((Button) $("cancel")).setOnAction(event -> {
            clear();
            this.close();
        });
    }

    @Override
    public void miniWindow() {

    }

    @Override
    public void initEvent() {
        ((Button) $("submit")).setOnAction(event -> {
            Message message = new Message();
            message.setMessageType(MessageType.ADD_FRIEND);
            message.setFromAccount(me);
            message.setToAccount(new ArrayList<Account>() {{
                add(friend);
            }});
            message.setMessage(((TextArea) $("message")).getText().trim());

            send.sendRequest(message);

            clear();
            this.close();
        });
    }
}
