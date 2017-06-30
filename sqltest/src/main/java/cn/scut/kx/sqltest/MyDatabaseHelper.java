package cn.scut.kx.sqltest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DELL on 2017/2/17.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    final String CREATE_TABLE_SQL = "create table dict(id integer primary key autoincrement , word varchar(255) , detail varchar(255))";

    public MyDatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("sql", "update called" + oldVersion + "---->" + newVersion);
    }
}
