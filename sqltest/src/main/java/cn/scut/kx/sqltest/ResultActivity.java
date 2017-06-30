package cn.scut.kx.sqltest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2017/2/17.
 */

public class ResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        ListView listView = (ListView) findViewById(R.id.show);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        List<Map<String, String>> list = (List<Map<String, String>>) data.getSerializable("data");
        SimpleAdapter adapter = new SimpleAdapter(ResultActivity.this, list, R.layout.line, new String[] {"word", "detail"}, new int[] {R.id.word, R.id.detail});
        listView.setAdapter(adapter);
    }
}
