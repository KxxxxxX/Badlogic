package cn.scut.kx.badlogic.framework;

import cn.scut.kx.badlogic.framework.Graphics.PixmapFormat;
/**
 * Created by DELL on 2016/12/1.
 */

public interface Pixmap {
    int getWidth();

    int getHeight();

    PixmapFormat getFormat();

    void dispose();
}
