package cn.scut.kx.badlogic.framework.impl;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

import cn.scut.kx.badlogic.framework.Audio;
import cn.scut.kx.badlogic.framework.Music;
import cn.scut.kx.badlogic.framework.Sound;

/**
 * Created by DELL on 2016/12/7.
 */

public class AndroidAudio implements Audio {
    AssetManager assetManager;
    SoundPool soundPool;

    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assetManager = activity.getAssets();
        if(Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(20);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            this.soundPool = builder.build();
        } else {
            this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        }
    }

    public Music newMusic(String filename) {
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filename);
            return new AndroidMusic(assetFileDescriptor);
        } catch (IOException e) {
            throw new RuntimeException("can not load music" + filename );
        }
    }

    @Override
    public Sound newSound(String filename) {
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filename);
            int soundId = soundPool.load(assetFileDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException("can not load sound");
        }
    }
}
