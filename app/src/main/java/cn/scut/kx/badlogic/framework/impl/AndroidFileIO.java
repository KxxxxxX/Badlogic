package cn.scut.kx.badlogic.framework.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.scut.kx.badlogic.framework.FileIO;

/**
 * Created by DELL on 2016/12/6.
 */

public class AndroidFileIO implements FileIO{
    Context context;
    AssetManager assetManager;
    String externalStoragePath;

    public AndroidFileIO(Context context) {
        this.context = context;
        this.assetManager = context.getAssets();
        this.externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    @Override
    public InputStream readAsser(String fileName) throws IOException {
        return assetManager.open(fileName);
    }

    @Override
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(externalStoragePath + fileName);
    }

    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath + fileName);
    }

    public SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
