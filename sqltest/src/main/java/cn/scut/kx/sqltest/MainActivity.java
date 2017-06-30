package cn.scut.kx.sqltest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    MyDatabaseHelper dbHelper;
    Button insert = null;
    Button search = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this, "myDict.db3", 1);
        insert = (Button) findViewById(R.id.insert);
        search = (Button) findViewById(R.id.search);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = ((EditText) findViewById(R.id.word)).getText().toString();
                String detail = ((EditText)findViewById(R.id.detail)).getText().toString();
                insertData(dbHelper.getWritableDatabase(), word, detail);
                Toast.makeText(MainActivity.this, "添加生词成功", Toast.LENGTH_SHORT).show();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = ((EditText) findViewById(R.id.key)).getText().toString();
                Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from dict where word like ? or detail like ?"
                        , new String[] {"%" + key + "%", "%" + key + "%"});
                Bundle data = new Bundle();
                data.putSerializable("data", converCursorToList(cursor));
                Intent intent  = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }
    protected ArrayList<Map<String, String>> converCursorToList(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();
        while (cursor.moveToNext()){
            Map<String, String> map = new HashMap<>();
            map.put("word", cursor.getString(1));
            map.put("detail", cursor.getString(2));
            result.add(map);
        }
        return result;
    }

    private void insertData(SQLiteDatabase db, String word, String detail) {
        db.execSQL("insert into dict values(null , ? , ?)" , new String[] {word, detail});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
