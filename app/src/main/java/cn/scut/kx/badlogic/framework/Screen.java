package cn.scut.kx.badlogic.framework;

/**
 * Created by DELL on 2016/12/1.
 */

public abstract class Screen {
    protected final Game game;

    public Screen (Game game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
}
