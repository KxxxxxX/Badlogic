package cn.scut.kx.badlogic.framework;

/**
 * Created by DELL on 2016/12/1.
 */

public interface Music {
        void play();

        void stop();

        void pause();

        void setLooping(boolean looping);

        void setVolume(float volume);

        boolean isPlaying();

        boolean isStopped();

        boolean isLooping();

        void dispose();
}
