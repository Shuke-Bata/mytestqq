package client.window;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Vector;

/**
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title FriendItem
 * @date 2020/6/4 19:05
 * @modified by:
 */
public class FriendItem {
    /**
     * 好友头像
     */
    private Button head;
    /**
     * 信息属性，用来设置好友备注显示
     */
    private Label information;
    /**
     * 个性标签
     */
    private Label label;
    /**
     * 面板
     */
    private Pane pane;
    /**
     * 右键点击的按钮
     */
    private Button send;
    private String friendHead;
    /**
     * 消息提示
     */
    private Button MsgTip;
    /**
     * 在线状态
     */
    private Button state;
    /**
     * 好友账号
     */
    private String friendName;
    /**
     * 右键菜单
     */
    Vector<MenuItem> items;
    /**
     * 是否在线标识
     */
    private boolean isOnline;

    public FriendItem() {
        this.head = new Button();
        this.head.setPrefSize(46, 46);
        this.head.setLayoutX(2);
        this.head.setLayoutY(2);
        this.head.getStyleClass().add("head");

        this.information = new Label();
        this.information.setPrefSize(120, 30);
        this.information.setLayoutX(55);
        this.information.setLayoutY(5);
        this.information.getStyleClass().add("information");

        this.label = new Label();
        this.label.setPrefSize(200, 20);
        this.label.setLayoutX(55);
        this.label.setLayoutY(30);
        this.label.getStyleClass().add("label");

        this.send = new Button();
        this.send.setPrefSize(310, 50);
        this.send.setLayoutY(0);
        this.send.setLayoutX(0);
        this.send.getStyleClass().add("sendMsg");

        this.MsgTip = new Button();
        this.MsgTip.setPrefSize(17, 17);
        this.MsgTip.setLayoutX(280);
        this.MsgTip.setLayoutY(34);
        this.MsgTip.getStyleClass().add("no-MsgTip");

        this.state = new Button();
        this.state.setPrefSize(15, 15);
        this.state.setLayoutX(33);
        this.state.setLayoutY(32);
        this.state.getStyleClass().add("outline");

        // TODO 右键菜单待完善
        this.items = new Vector<>();
        this.items.add(new MenuItem("删除好友"));

        this.pane = new Pane();
        this.pane.setPrefSize(310, 50);
        this.pane.getStyleClass().add("listItem");

        this.pane.getChildren().add(this.head);
        this.pane.getChildren().add(this.information);
        this.pane.getChildren().add(this.label);
        this.pane.getChildren().add(this.send);
        this.pane.getChildren().add(this.MsgTip);
        this.pane.getChildren().add(this.state);

        this.pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {

                }
            }
        });

    }

    public FriendItem(String head, String account, String remark, String label) {
        this();
        this.friendName = account;
        this.head.setStyle(String.format("-fx-background-image:url('/image/head/%s.jpg')", head));
        this.information.setText(remark);
        this.label.setText(label);

        Label label1 = new Label(account);
        label1.setVisible(false);
        this.pane.getChildren().add(label1);
    }

    /**
     * 设置上线状态
     */
    public void setOnline() {
        state.getStyleClass().clear();
        state.getStyleClass().add("online");
        isOnline = true;
    }

    /**
     * 设置下线状态
     */
    public void setOutline() {
        state.getStyleClass().clear();
        state.getStyleClass().add("outline");
        isOnline = false;
    }

    /**
     * @return 获取好友在线状态
     */
    public boolean isOnline() {
        return isOnline;
    }


    public Button getHead() {
        return head;
    }

    public void setHead(Button head) {
        this.head = head;
    }

    public Label getInformation() {
        return information;
    }

    public void setInformation(Label information) {
        this.information = information;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public Button getSend() {
        return send;
    }

    public void setSend(Button send) {
        this.send = send;
    }

    public String getFriendHead() {
        return friendHead;
    }

    public void setFriendHead(String friendHead) {
        this.friendHead = friendHead;
    }

    public Button getMsgTip() {
        return MsgTip;
    }

    public void setMsgTip(Button msgTip) {
        MsgTip = msgTip;
    }

    public void setState(Button state) {
        this.state = state;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public Vector<MenuItem> getItems() {
        return items;
    }

    public void setItems(Vector<MenuItem> items) {
        this.items = items;
    }
}
