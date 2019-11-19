package com.tencent.wxpay.imagefacesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class IFSMainActivity extends Activity implements View.OnClickListener {

    Button mExample;
    Button mFactoryTest;
    Button mExit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mExample = (Button) findViewById(R.id.example);
        mExample.setOnClickListener(this);
        mFactoryTest = (Button) findViewById(R.id.factory_test);
        mFactoryTest.setOnClickListener(this);
        mExit = (Button) findViewById(R.id.exit);
        mExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.example:
                intent = new Intent(this, IFSExampleActivity.class);
                startActivity(intent);
                break;
            case R.id.factory_test:
                intent = new Intent(this, IFSFactoryTestActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                finish();
                break;
            default:
                break;
        }
    }
}