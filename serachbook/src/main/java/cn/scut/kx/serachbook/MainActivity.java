package cn.scut.kx.serachbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    ProgressDialog mProgressDialog;
    Button button;
    DownloadHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new DownloadHandler(this);
        button = (Button) findViewById(R.id.main_start_scan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanner();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if ((result == null) || (result.getContents() == null)) {
            return;
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.communicating));
        mProgressDialog.show();

        DownloadThread thread = new DownloadThread(BookAPI.URL_ISBN_BASE + result.getContents());
        thread.start();
    }

    private void startScanner() {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.initiateScan();
    }

    private class DownloadThread extends Thread {
        private String url;

        public DownloadThread(String url) {
            super();
            this.url = url;
        }

        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.obj = Utils.download(url);
            mHandler.sendMessage(msg);
        }
    }

    private static class DownloadHandler extends Handler {

        private MainActivity activity;

        public DownloadHandler(MainActivity activity) {
            super();
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            if ((msg.obj == null) || (activity.mProgressDialog == null) || (!activity.mProgressDialog.isShowing())) {
                return;
            }
            activity.mProgressDialog.dismiss();
            NetResponse response = (NetResponse) msg.obj;

            if (response.getCode() != BookAPI.RESPONSE_CODE_SUCCEED) {
                Toast.makeText(activity, "[" + response.getCode() + "]:" + activity.getString((Integer)response.getMessage()), Toast.LENGTH_LONG).show();
            }else {
                activity.startBookInfoDetailActivity((BookInfo) response.getMessage());
            }
        }
    }

    private void startBookInfoDetailActivity(BookInfo bookInfo) {
        if (bookInfo == null) {
            return;
        }

        Intent intent = new Intent(this, BookInfoDetailActivity.class);
        intent.putExtra(BookInfo.class.getName(), bookInfo);
        startActivity(intent);
    }
}
