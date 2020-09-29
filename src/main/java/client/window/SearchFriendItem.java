package client.window;

import common.po.Account;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

/**
 * 查询好友的展示元素类
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title SearchFriendItem
 * @date 2020/6/6 14:25
 * @modified by:
 */
public class SearchFriendItem {
    private Account me;
    private Account friend;

    private Button head;
    private Alter alter;
    private Label information;
    private Button friend_add;
    private Pane pane;

    public SearchFriendItem(Account me, Account friend) {
        this.me = me;
        this.friend = friend;

        alter = new Alter();
        head = new Button();
        friend_add = new Button();
        information = new Label();
        pane = new Pane();
        pane.getChildren().add(head);
        pane.getChildren().add(information);
        pane.setPrefSize(470, 60);
        head.setPrefSize(56, 56);
        friend_add.setPrefSize(32, 32);
        friend_add.getStyleClass().add("add");
        friend_add.setLayoutX(400);
        friend_add.setLayoutY(14);
        friend_add.setTooltip(new Tooltip("添加好友"));
        pane.getChildren().add(friend_add);
        head.setLayoutX(2);
        head.setLayoutY(2);
        information.setPrefSize(200, 32);
        information.setLayoutX(65);
        information.setLayoutY(5);
        head.getStyleClass().add("head");
        information.getStyleClass().add("information");
        pane.getStyleClass().add("ListItem");

        this.head.setStyle(String.format("-fx-background-image:url('/image/head/%s.jpg')", friend.getAvatar()));
        this.information.setText(friend.getNickname());
    }

    public Pane getPane() {
        return pane;
    }

}
