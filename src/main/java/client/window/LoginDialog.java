package client.window;


import client.controller.Receive;
import client.controller.Send;
import client.window.conf.PageManger;
import client.window.conf.PageName;
import common.message.Message;
import common.message.MessageType;
import common.po.Account;
import common.po.Online;
import javafx.scene.control.*;

/**
 * 登录界面
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title LoginDialog
 * @date 2020/5/23 9:12
 * @modified by:
 */
public class LoginDialog extends AbstractWindow {
    public LoginDialog(Send send, Receive receive) {
        super(PageManger.getConfig(PageName.LOGIN_PAGE), send, receive);
    }


    /**
     * 加载登录界面
     */
    public void loadLogin() {
        this.show();
    }

    /**
     * 登录
     */
    private void login() {
        String userName = ((TextField) $("UserName")).getText().trim();
        String password = ((TextField) $("Password")).getText().trim();
        if ("".equals(userName)) {
            setErrorTip("accountError", "未输入账号！");
        } else if ("".equals(password)) {
            setErrorTip("passwordError", "未输入密码！");
        } else {
            String sql = "select * from account where account=?";
            Account account = (Account) query.queryUniqueRow(sql, Account.class, new Object[]{userName});
            if (account == null) {
                setErrorTip("accountError", "账号未注册!");
            } else if (password.equals(account.getPassword())) {
                // 查询是否已经登录
                String onlineSql = "select * from `online` where account=?";
                Object o = query.queryUniqueRow(onlineSql, Online.class, new Object[]{account.getAccount()});
                if (o == null) {
                    // 设置在线
                    Online online = new Online();
                    online.setAccount(account.getAccount());
                    query.insert(online);
                    send.sendRequest(new Message(MessageType.LOGIN, account));
                    this.hide();
                    new HomePage(send, receive).loadHomePage(account);
                } else {
                    setErrorTip("passwordError", "该账号已经登录！");
                }
            } else {
                setErrorTip("passwordError", "你输入的密码错误！");
            }
        }

    }

    /**
     * 点击立即注册，进入注册界面
     */
    private void register() {
        this.hide();
        RegisterDialog registerDialog = new RegisterDialog(send, receive);
        registerDialog.backPage = this;
        registerDialog.show();
    }

    /**
     * 设置错误提示
     *
     * @param id
     * @param text 提示内容
     */
    public void setErrorTip(String id, String text) {
        ((Label) $(id)).setText(text);
    }

    /**
     * 重置错误题醒
     */
    public void resetErrorTip() {
        setErrorTip("accountError", "");
        setErrorTip("passwordError", "");
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

    @Override
    public void clear() {
        resetErrorTip();
        ((TextField) $("UserName")).clear();
        ((PasswordField) $("Password")).clear();
    }

    /**
     * 初始化事件
     */
    @Override
    public void initEvent() {
        // 立即注册
        ((Button) $("register")).setOnAction(event -> register());
        // 登录
        ((Button) $("login")).setOnAction(event -> login());

        ((Button) $("getBack")).setOnAction(event -> {
            // TODO 忘记密码功能待完成
        });


    }
}
