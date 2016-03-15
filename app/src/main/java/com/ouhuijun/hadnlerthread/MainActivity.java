package com.ouhuijun.hadnlerthread;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTVServiceInfo;
    private HandlerThread mCheckMsgThread;
    private Handler mCheckMsgHandler;
    private boolean isUpdateInfo;
    public static final int MSG_UPDATE_INFO=0x110;
    private Handler mHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTVServiceInfo= (TextView) findViewById(R.id.tv_info);
        initBackThread();
    }

    private void initBackThread() {
      mCheckMsgThread =new HandlerThread("check-messahe-coming");
        mCheckMsgThread.start();
        mCheckMsgHandler=new Handler(mCheckMsgThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                checkForUpdate();

                if(isUpdateInfo)
                    mCheckMsgHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO,1000);
            }
        };

    }

    private void checkForUpdate() {

        try {
            Thread.sleep(1000);
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    String result = "<font color='red'>%d</font>";
                    result = String.format(result, (int) (Math.random() * 100 ));
                    mTVServiceInfo.setText(Html.fromHtml(result));
                }
            });


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isUpdateInfo=true;
        mCheckMsgHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO,1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isUpdateInfo=false;
        mCheckMsgHandler.removeMessages(MSG_UPDATE_INFO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCheckMsgThread.quit();

    }
}
