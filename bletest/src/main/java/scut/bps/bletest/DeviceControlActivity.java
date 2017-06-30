/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scut.bps.bletest;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    
    private LinearLayout layout;
    private volatile List<Point> plist;
    private volatile List<Double> mlist = new ArrayList<Double>();
    private volatile double py;
    private int ii = 0;
    private int chartH, chartW;
    private DrawChart view;
    private int xxx = 15;
    private int xInterval = 20;
    
     @SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
    	public void handleMessage(Message msg) {
    		if(msg.what == 1) {
				double ecg = Judge((String)msg.obj);
    			mlist.add(chartH/2-ecg*400);
    			py = mlist.get(ii++);
    		}
    	};
    };
    
    
    
    public class DrawChart extends View {
		public DrawChart(Context context) {
			super(context);
			plist = new ArrayList<Point>();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			chartH = (int) (layout.getHeight()*0.8);// layout的高200
			chartW = layout.getWidth();// layout的宽490
			xInterval = (int) (layout.getWidth() * 0.040816);
			// 根据设备屏幕宽度设置画图两点间距，暂不完善
			if (getWindowManager().getDefaultDisplay().getWidth() > 600) {
				xxx = 15;
			} else if (getWindowManager().getDefaultDisplay().getWidth() <= 600
					&& getWindowManager().getDefaultDisplay().getWidth() >= 300) {
				xxx = 10;
			} else {
				xxx = 5;
			}

			prepareLine();
			drawCurve(canvas);
			invalidate();
		}

		// 画线
		@SuppressWarnings("deprecation")
		private void drawCurve(Canvas canvas) {
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			// 根据设备屏幕宽度设置画笔大小，暂不完善
			if (getWindowManager().getDefaultDisplay().getWidth() > 600) {
				paint.setStrokeWidth(3);
			} else if (getWindowManager().getDefaultDisplay().getWidth() <= 600
					&& getWindowManager().getDefaultDisplay().getWidth() >= 300) {
				paint.setStrokeWidth(2);
			} else {
				paint.setStrokeWidth(1);
			}
			paint.setAntiAlias(true);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStyle(Style.STROKE);
			Path path = new Path();
			path.reset();
			path.moveTo(plist.get(0).x, plist.get(0).y);
			if (plist.size() >= 2) {
				for (int i = 0; i < plist.size() - 1; i++) {
					path.lineTo(plist.get(i + 1).x, plist.get(i + 1).y);
					// 绘制贝赛尔曲线（Path）
					canvas.drawPath(path, paint);
				}
			}
		}

		// 动态移动
		private void prepareLine() {
			Point p = new Point(chartW, (int) py);
			if (plist.size() > chartW / xxx) {

				plist.remove(0);
				for (int i = 0; i < chartW / xxx; i++) {
					if (i == 0)
						plist.get(i).x -= (xInterval - 2);
					else
						plist.get(i).x -= xInterval;
				}
				plist.add(p);
			} else {
				for (int i = 0; i < plist.size() - 1; i++) {
					plist.get(i).x -= xInterval;
				}
				plist.add(p);
			}
		}
	}

	protected double Judge(String c) {
		// mDataField.append(c+'\n');
		double num [] = new double[6];
		for(int i= 0;i<=5;i++) {
			if (c.charAt(i) >= 'A' && c.charAt(0) <= 'F') {
				num[i] = (c.charAt(i) - 'A') + 10;
			} else {
				num[i] = c.charAt(i) - '0';
			}
			
		}
		return ((num[0]*1048576.0+num[1]*65536.0+num[2]*4096.0+num[3]*256.0+num[4]*16.0+num[5])-4194304.0)/3058.346;
	}
	
	
	
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            
            Log.e(TAG, "mBluetoothLeService is okay");
            // Automatically connects to the device upon successful start-up initialization.
            //mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    
    private StringBuilder s1 = new StringBuilder();
    public String s2 = null;
    
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {  //连接成功
            	Log.e(TAG, "Only gatt, just wait");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //断开连接
                mConnected = false;
                invalidateOptionsMenu();
            }else if(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) //可以开始干活了
            {
            	mConnected = true;
            	ShowDialog();
            	Log.e(TAG, "In what we need");
            	invalidateOptionsMenu();
            	
            }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //收到数据
            	Log.e(TAG, "RECV DATA");
            	
            	//String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
            	String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
            	if (data != null) {
            		String[] s = data.split("55AA09", 2);
            		s1.append(s[0]);
            		if (s1.length() == 16) {
            			handler.sendMessage(handler.obtainMessage(1, s1.toString()));

            		} else if (s1.length() > 16){
            			String[] sin = s1.toString().split("55AA09");
            			if (sin[0].length() == 16) {
                			handler.sendMessage(handler.obtainMessage(1, sin[0]));

            			}
            			if (sin[1].length() == 16) {
                			handler.sendMessage(handler.obtainMessage(1, sin[1]));

            			}
            		} else {
            		}
            		s1.setLength(0);
            		s1.append(s[1]);
            	}
            	
            	
 /*           	if (data != null) {
                	if (mDataField.length() > 500)
                		mDataField.setText("");
                    mDataField.append(data+"\n"); 
                    //Log.d("kxtest", Integer.toString(data.length()));
                    svResult.post(new Runnable() {
            			public void run() {
            				svResult.fullScroll(ScrollView.FOCUS_DOWN);
            			}
            		});
                }  */
            }
        }
    };

    
    
    private void clearUI() {
   //     mDataField.setText(R.string.no_data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {                                        //初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        layout = (LinearLayout) findViewById(R.id.ecg);
        view = new DrawChart(this);
        
    	layout.addView(view);
    	
    	
    	

        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        Log.d(TAG, "Try to bindService=" + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));
        
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //this.unregisterReceiver(mGattUpdateReceiver);
        //unbindService(mServiceConnection);
        if(mBluetoothLeService != null)
        {
        	mBluetoothLeService.close();
        	mBluetoothLeService = null;
        }
        Log.d(TAG, "We are in destroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                              //点击按钮
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
            	if(mConnected)
            	{
            		mBluetoothLeService.disconnect();
            		mConnected = false;
            	}
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void ShowDialog()
    {
    	Toast.makeText(this, "连接成功，现在可以正常通信！", Toast.LENGTH_SHORT).show();
    }

    /*
 // 按钮事件
	class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == btnSend) {
				if(!mConnected) return;
				
				if (edtSend.length() < 1) {
					Toast.makeText(DeviceControlActivity.this, "请输入要发送的内容", Toast.LENGTH_SHORT).show();
					return;
				}
				mBluetoothLeService.WriteValue(edtSend.getText().toString());
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if(imm.isActive())
					imm.hideSoftInputFromWindow(edtSend.getWindowToken(), 0);
				//todo Send data
			}
		}

	}	*/
	
    private static IntentFilter makeGattUpdateIntentFilter() {                        //注册接收的事件
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }
}
