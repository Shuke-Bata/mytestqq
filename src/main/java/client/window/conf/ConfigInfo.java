package client.window.conf;

/**
 * 单个页面配置信息，如fxml文件位置、窗口宽度，长度
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title ConfigInfo
 * @date 2020/5/27 12:13
 * @modified by:
 */
public class ConfigInfo {
    private String fxml;
    private Integer width;
    private Integer height;

    public ConfigInfo() {
    }

    public ConfigInfo(String fxml, Integer width, Integer height) {
        this.fxml = fxml;
        this.width = width;
        this.height = height;
    }

    public String getFxml() {
        return fxml;
    }

    public void setFxml(String fxml) {
        this.fxml = fxml;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
