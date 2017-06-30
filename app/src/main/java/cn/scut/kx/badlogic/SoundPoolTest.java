package cn.scut.kx.badlogic;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import cn.scut.kx.badlogic.framework.Audio;

/**
 * Created by DELL on 2016/12/3.
 */

public class SoundPoolTest extends Activity implements View.OnTouchListener{
    SoundPool soundPool;
    int explosionId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        setContentView(textView);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = initSoundPool();

        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd("explosion.ogg");
            explosionId = soundPool.load(descriptor, 1);
            textView.setText("success" + explosionId);
        } catch (IOException e) {
            textView.setText(R.string.load_file_fail + e.getMessage());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            soundPool.play(explosionId, 1, 1, 0, 0, 1);
        }
        return true;
    }

    private SoundPool initSoundPool() {
        if(Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(20);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            return builder.build();
        } else {
            return new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        }
    }
}
