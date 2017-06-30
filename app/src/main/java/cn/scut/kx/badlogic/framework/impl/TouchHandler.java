package cn.scut.kx.badlogic.framework.impl;

import android.view.View;

import java.util.List;

import cn.scut.kx.badlogic.framework.Input;

/**
 * Created by DELL on 2017/2/8.
 */

public interface TouchHandler extends View.OnTouchListener {
    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public List<Input.TouchEvent> getTouchEvents();
}
