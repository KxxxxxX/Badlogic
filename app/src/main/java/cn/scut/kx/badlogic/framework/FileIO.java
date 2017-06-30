package cn.scut.kx.badlogic.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by DELL on 2016/12/1.
 */

public interface FileIO {
    InputStream readAsser(String fileName) throws IOException;

    InputStream readFile(String fileName) throws IOException;

    OutputStream writeFile(String fileName) throws IOException;
}
