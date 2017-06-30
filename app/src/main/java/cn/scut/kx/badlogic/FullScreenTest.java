package cn.scut.kx.badlogic;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by DELL on 2016/12/4.
 */

public class FullScreenTest extends SingleTouchTest {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }
}
