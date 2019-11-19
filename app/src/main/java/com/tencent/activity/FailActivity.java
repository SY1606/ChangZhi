package com.tencent.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.http.TTSUtils;
import com.tencent.wxpay.imagefacesign.R;

public class FailActivity extends AppCompatActivity {


    private TextView moneys;
    private Button backs;
    private int time=3;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time--;
            backs.setText("回到首页"+time+"s");
            if (time==0){
                Intent intent = new Intent(FailActivity.this,TwoActivity.class);
                intent.putExtra("store_ids",store);
                intent.putExtra("appids",appids);
                intent.putExtra("keys",key);
                startActivity(intent);
                finish();
                handler.removeMessages(0);
            }
            handler.sendEmptyMessageDelayed(0,1000);
        }
    };
    private int store;
    private String appids;
    private String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail);

        backs = findViewById(R.id.backs);
        handler.sendEmptyMessageDelayed(0,1000);
        Intent intent = getIntent();
        store = intent.getIntExtra("store_ids",0);

        appids = intent.getStringExtra("appids");
        key = intent.getStringExtra("keys");

        TTSUtils.speak("支付失败");
    }
}
