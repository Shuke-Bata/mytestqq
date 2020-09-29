package client.window;

import client.controller.Receive;
import client.controller.Send;
import client.window.conf.PageManger;
import client.window.conf.PageName;
import common.po.Account;
import common.po.Grouping;
import javafx.scene.control.*;

/**
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title RegisterDialog
 * @date 2020/6/4 14:07
 * @modified by:
 */
public class RegisterDialog extends AbstractWindow {
    public RegisterDialog(Send send, Receive receive) {
        super(PageManger.getConfig(PageName.REGISTER_PAGE), send, receive);

        ToggleGroup group = new ToggleGroup();
        RadioButton radioButton = ((RadioButton) $("man"));
        radioButton.setToggleGroup(group);
        ((RadioButton) $("women")).setToggleGroup(group);
        radioButton.setSelected(true);
        radioButton.requestFocus();
    }


    /**
     * 返回登录页面
     */
    private void back() {
        this.hide();
        clear();
        backPage.show();
    }

    /**
     * 点击注册按钮
     */
    private void register() {
        resetErrorTip();
        String nickname = ((TextField) $("account")).getText().trim();
        String name = ((TextField) $("name")).getText().trim();
        String password = ((PasswordField) $("password")).getText().trim();
        String rePassword = ((PasswordField) $("rePassword")).getText().trim();
        String sex;
        RadioButton radioButton = ((RadioButton) $("man"));
        if ("".equals(nickname)) {
            setErrorTip("accountError", "昵称不能为空！");
        } else if ("".equals(name)) {
            setErrorTip("nameError", "姓名不能为空！");
        } else if ("".equals(password)) {
            setErrorTip("passwordError", "密码不能为空！");
        } else if ("".equals(rePassword)) {
            setErrorTip("rePasswordError", "请确认密码！");
        } else {
            if (password.equals(rePassword)) {
                if (radioButton.isSelected()) {
                    sex = "男";
                } else {
                    sex = "女";
                }


                Account account = new Account();
                account.setAccount((int) ((Math.random() * 9 + 1) * 10000) + "");
                account.setPassword(password);
                account.setNickname(nickname);
                account.setUser_name(name);
                account.setAvatar("head6");
                account.setBackground("background3");

                assert query != null;
                query.insert(account);

                String sql = "select * from account where account=?";
                Account account1 = (Account) query.queryUniqueRow(sql, Account.class, new Object[]{account.getAccount()});

                Grouping grouping = new Grouping();
                grouping.setAccount(String.valueOf(account1.getEid()));
                grouping.setGroup_name("我的好友");
                query.insert(grouping);

                alter.setText("注册成功，你的账号是" + account.getAccount());
                alter.show();
                // 完成后返回登录页面
                back();


            } else {
                setErrorTip("rePasswordError", "两次输入的密码不一致，请重新输入！");
            }

        }
    }

    @Override
    public void clear() {
        ((TextField) $("account")).clear();
        ((TextField) $("name")).clear();
        ((PasswordField) $("password")).clear();
        ((PasswordField) $("rePassword")).clear();
        RadioButton radioButton = ((RadioButton) $("man"));
        radioButton.setSelected(true);
        radioButton.requestFocus();
        resetErrorTip();
    }

    @Override
    public void closeWindow() {

    }

    @Override
    public void miniWindow() {

    }

    @Override
    public void initEvent() {
        // 注册
        ((Button) $("register")).setOnAction(event -> register());
        // 返回登录
        ((Button) $("back")).setOnAction(event -> back());
    }

    /**
     * 重置错误提示
     */
    private void resetErrorTip() {
        ((Label) $("accountError")).setText("");
        ((Label) $("nameError")).setText("");
        ((Label) $("passwordError")).setText("");
        ((Label) $("rePasswordError")).setText("");
    }

    /**
     * 设置错误提示
     *
     * @param id
     * @param Text 提示消息
     */
    public void setErrorTip(String id, String Text) {
        ((Label) $(id)).setText(Text);
    }
}
