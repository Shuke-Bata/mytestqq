package client.window;


import client.controller.Receive;
import client.controller.Send;
import client.window.conf.ConfigInfo;
import common.orm.core.AbstractQuery;
import common.orm.core.QueryFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * 所有窗口类的父类
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title AbstractWindow
 * @date 2020/5/23 9:01
 * @modified by:
 */
public abstract class AbstractWindow extends Stage {
    /**
     * 数据库查询类
     */
    AbstractQuery query;
    /**
     * 上一个页面
     */
    Stage backPage;
    Parent root;
    Alter alter;
    public Send send;
    public Receive receive;
    private double xOffset;
    private double yOffset;

    public AbstractWindow() {
    }

    public AbstractWindow(ConfigInfo configInfo) {
        if (configInfo != null) {
            try {
                root = FXMLLoader.load(getClass().getResource(configInfo.getFxml()));
                initWindow(configInfo);
                query = QueryFactory.createQuery();
                alter = new Alter();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public AbstractWindow(ConfigInfo configInfo, Send send, Receive receive) {
        this(configInfo);
        this.send = send;
        this.receive = receive;
    }


    /**
     * 窗口移动方法
     */
    private void moveWindow() {
        root.setOnMousePressed(event -> {
            xOffset = getX() - event.getScreenX();
            yOffset = getY() - event.getScreenY();
            root.setCursor(Cursor.CLOSED_HAND);

        });

        root.setOnMouseDragged(event -> {
            setX(event.getScreenX() + xOffset);
            setY(event.getScreenY() + yOffset);
        });

        root.setOnMouseReleased(event -> {
            root.setCursor(Cursor.DEFAULT);
        });
    }


    /**
     * 设置窗口图标
     */
    private void setIcon(String path) {
        getIcons().add(new Image(getClass().getResourceAsStream(path)));
    }

    /**
     * 初始化窗口，如设置窗口图标、设置窗口可移动、设置关闭、最小化
     */
    public void initWindow(ConfigInfo configInfo) {
        setScene(new Scene(root, configInfo.getWidth(), configInfo.getHeight()));
        initStyle(StageStyle.TRANSPARENT);
        setTitle("Chat");
        setIcon("/image/Icon/white-qq.jpg");
        clear();
        initEvent();
        moveWindow();
        closeWindow();
        miniWindow();
    }


    /**
     * 选择界面元素
     *
     * @param id 元素id
     * @return 该id对应的元素
     */
    public Object $(String id) {
        return (Object) root.lookup("#" + id);
    }


    /**
     * 清除各种输入框
     */
    public abstract void clear();

    /**
     * 关闭界面
     */
    public abstract void closeWindow();

    /**
     * 界面最小化
     */
    public abstract void miniWindow();

    /**
     * 初始化事件
     */
    public abstract void initEvent();
}
