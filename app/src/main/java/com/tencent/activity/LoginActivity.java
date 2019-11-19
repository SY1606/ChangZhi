package com.tencent.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.ToastUtil;
import com.tencent.data.bean.LoginBean;

import com.tencent.di.presenter.BaseInterface;

import com.tencent.di.presenter.Mypresenter;
import com.tencent.http.Md5ut;
import com.tencent.http.Url;
import com.tencent.wxpay.imagefacesign.R;

import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;
import com.tencent.wxpayface.WxfacePayCommonCode;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements BaseInterface.LoginInterface  {

    @BindView(R.id.medit_phone)
    EditText meditPhone;
    @BindView(R.id.medit_pass)
    EditText meditPass;
    @BindView(R.id.yin)
    CheckBox yin;
    @BindView(R.id.mlogin)
    Button mlogin;
    private String mAuthInfo;
    public static final String RETURN_CODE = "return_code";
    public static final String RETURN_MSG = "return_msg";
    String type = "mch";
    String terminal = "wxface_app";
    BaseInterface.PInterface pInterface;
    public static SharedPreferences sp;

    private SharedPreferences.Editor edit;
    private String tel;
    private String pass;
    private String mima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);


        pInterface = new Mypresenter(this);


        yin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    meditPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    meditPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
        sp = getSharedPreferences("user", MODE_PRIVATE);

        meditPhone.setText(sp.getString("mch_seller_number", ""));
        meditPass.setText(sp.getString("password", ""));
        edit = sp.edit();


        Map<String, String> m1 = new HashMap<>();
//                m1.put("ip", "192.168.1.1"); //若没有代理,则不需要此行
//                m1.put("port", "8888");//若没有代理,则不需要此行

    }

    @OnClick({R.id.yin, R.id.mlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.yin:

                break;
            case R.id.mlogin:

                tel = meditPhone.getText().toString();
                mima = meditPass.getText().toString();

                String md1 = Md5ut.md5Password(mima);
                Log.i("fxcaaxa", md1);

                String sss = Md5ut.md5Password(md1 + "zzxunlong");
                Log.i("tttt", sss);

                pass = sss.toUpperCase();
                Map<String, String> map = new HashMap<>();
                map.put("type", type);
                map.put("terminal", terminal);
                map.put("tel", tel);
                map.put("pass", pass);


                pInterface.Login(Url.LOGIN_URL,map, LoginBean.class);

                break;
        }

    }

    @Override
    public void Success(Object o) {
        LoginBean loginBean = (LoginBean) o;
        if (loginBean.getMsg().equals("登录成功")) {

            Toast.makeText(this, loginBean.getMsg(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, TwoActivity.class);
            intent.putExtra("store_ids",loginBean.getData().getSid());
            intent.putExtra("appids",loginBean.getData().getAppid());
            intent.putExtra("keys",loginBean.getData().getKey());
            intent.putExtra("names",loginBean.getData().getMch_name());
            Log.i("gafxaa",loginBean.getData().getKey());

            SharedPreferences.Editor edit = sp.edit();
            edit.putString("mch_seller_number", tel);
            edit.putString("password", mima);
            edit.commit();

            startActivity(intent);
        } else {
            Toast.makeText(this, loginBean.getMsg(), Toast.LENGTH_SHORT).show();

        }
    }

}