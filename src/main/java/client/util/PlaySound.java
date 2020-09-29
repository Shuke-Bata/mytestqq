package client.util;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title PlaySound
 * @date 2020/6/6 12:31
 * @modified by:
 */
public class PlaySound {
    private static Player player;

    public PlaySound() {

    }

    /**
     * 上线提示音
     */
    public static void playOnline() {

        try {
            BufferedInputStream buffer = new BufferedInputStream(
                    new FileInputStream(new File("src/main/resources/online.mp3")));
            player = new Player(buffer);
            player.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }

    }

    /**
     * 消息提示音
     */
    public static void playMessage() {

        try {
            BufferedInputStream buffer = new BufferedInputStream(
                    new FileInputStream(new File("src/main/resources/message.mp3")));
            player = new Player(buffer);
            player.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }

    }

}
