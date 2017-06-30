package scut.bps.messagedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by kx on 2017/4/25.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    // 手机号输入框
    private EditText inputPhoneEt;

    // 验证码输入框
    private EditText inputCodeEt;

    // 获取验证码按钮
    private Button requestCodeBtn;

    // 注册按钮
    private Button commitBtn;

    //
    int i = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    public void onClick(View v) {
        String phontNums = inputPhoneEt.getText().toString();
        switch (v.getId()){
            case R.id.login_request_code_btn:
                if (!judgePhoneNums(phontNums)){
                    return;
                }
                SMSSDK.getVerificationCode("86", phontNums);

                requestCodeBtn.setClickable(false);
                requestCodeBtn.setText("重新发送("+i+")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i >0 ; i--){
                            handler.sendEmptyMessage(-9);
                            if (i<=0){
                                break;
                            }
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
            case R.id.login_commit_btn:
                SMSSDK.submitVerificationCode("86", phontNums, inputCodeEt.getText().toString());
                createProgressBar();
                break;
        }
    }

    /**
     * 初始化控件
     */
    private void init(){
        inputPhoneEt = (EditText) findViewById(R.id.login_input_phone_et);
        inputCodeEt = (EditText) findViewById(R.id.login_input_code_et);
        requestCodeBtn = (Button) findViewById(R.id.login_request_code_btn);
        commitBtn = (Button) findViewById(R.id.login_commit_btn);
        requestCodeBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

        SMSSDK.initSDK(this,"1d3b18624d6d0","7be0e0f0137d7accff9701457772411c");
        SMSSDK.registerEventHandler(new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object o) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = o;
                handler.sendMessage(msg);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == -9){
                requestCodeBtn.setText("重新发送(" + i +")");
            }else if (msg.what == -8){
                requestCodeBtn.setText("获取验证马");
                requestCodeBtn.setClickable(true);
                i = 30;
            }else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object o = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE){
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        Toast.makeText(getApplicationContext(),"have been send", Toast.LENGTH_SHORT).show();
                    }else {
                        ((Throwable)o).printStackTrace();
                    }
                }
            }
        }
    };

    private boolean judgePhoneNums(String phoneNums){
        if (isMatchLength(phoneNums, 11)&& isMobileNO(phoneNums)){
            return true;
        }
        Toast.makeText(this, "phone number wrong!!!!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private static boolean isMatchLength(String str, int length){
        return !str.isEmpty() && str.length() == length;
    }

    private static boolean isMobileNO(String mobileNums){
        String telRegex = "[1][358]\\d{9}";
        //return !TextUtils.isEmpty(mobileNums) && mobileNums.matches(telRegex);
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    private void createProgressBar(){
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
