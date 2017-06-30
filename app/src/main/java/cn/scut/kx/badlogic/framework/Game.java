package cn.scut.kx.badlogic.framework;

/**
 * Created by DELL on 2016/12/1.
 */

public interface Game {
    Input getInput();

    FileIO getFileIO();

    Graphics getGraphics();

    Audio getAudio();

    void setScreen(Screen screen);

    Screen getCurrentScreen();

    Screen getStartScreen();

}
