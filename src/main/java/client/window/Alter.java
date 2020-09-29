package client.window;

import client.window.conf.ConfigInfo;
import client.window.conf.PageManger;
import client.window.conf.PageName;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * 弹窗消息提醒类
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title Alter
 * @date 2020/6/4 17:23
 * @modified by:
 */
public class Alter extends Stage {
    private Parent root;

    public Alter() {
        ConfigInfo config = PageManger.getConfig(PageName.ALTER_PAGE);
        try {
            assert config != null;
            root = FXMLLoader.load(getClass().getResource(config.getFxml()));
            setScene(new Scene(root, config.getWidth(), config.getHeight()));
            initStyle(StageStyle.TRANSPARENT);
            getIcons().add(new Image(getClass().getResourceAsStream("/image/Icon/white-qq.jpg")));
            initEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置消息
     *
     * @param text 消息
     */
    public void setText(String text) {
        ((Label) $("information")).setText(text);
    }

    public void clear() {
        ((Label) $("information")).setText("");
    }

    public void initEvent() {
        ((Button) $("submit")).setOnAction(event -> {
            clear();
            this.close();
        });
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

}
