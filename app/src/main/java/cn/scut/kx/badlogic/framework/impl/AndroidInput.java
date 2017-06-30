package cn.scut.kx.badlogic.framework.impl;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.scut.kx.badlogic.framework.Input;

/**
 * Created by DELL on 2017/2/8.
 */

public class AndroidInput implements Input {
    AccelerometerHandler accelHandler;
    TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float sacleY) {
        accelHandler = new AccelerometerHandler(context);
        touchHandler = new MultiTouchHandler(view, scaleX, sacleY);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public float getAccelX() {
        return accelHandler.getAccelX();
    }

    @Override
    public float getAccelY() {
        return accelHandler.getAccelY();
    }

    @Override
    public float getAccelZ() {
        return accelHandler.getAccelZ();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}
