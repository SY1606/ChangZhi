package com.tencent.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.http.TTSUtils;
import com.tencent.wxpay.imagefacesign.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class SuccessActivity extends AppCompatActivity {
    private Button mlogin;
    private int time=3;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time--;
            mlogin.setText("回到首页"+time+"s");
            if (time==0){
                Intent intent = new Intent(SuccessActivity.this,TwoActivity.class);
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
        setContentView(R.layout.activity_success);
         mlogin = findViewById(R.id.mlogin);

        handler.sendEmptyMessageDelayed(0,1000);

        TextView moneys = findViewById(R.id.moneys);
        Intent intent = getIntent();
        int total =  intent.getIntExtra("moneys",0);
        Log.i("egadvaxa",total+"");

        store = intent.getIntExtra("store_ids",0);

        appids = intent.getStringExtra("appids");
        key = intent.getStringExtra("keys");


        //moneys.setText("￥"+total);
        double d = (double)total/100;
        moneys.setText("￥"+d+"");

        TTSUtils.speak("支付成功"    +d+"元");
    }
}


