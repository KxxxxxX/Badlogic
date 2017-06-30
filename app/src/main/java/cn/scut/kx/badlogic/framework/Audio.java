package cn.scut.kx.badlogic.framework;

/**
 * Created by DELL on 2016/12/1.
 */

public interface Audio {

    Music newMusic(String filename);

    Sound newSound(String filename);
}
