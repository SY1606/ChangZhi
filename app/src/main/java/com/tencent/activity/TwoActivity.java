package com.tencent.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;

import android.text.TextUtils;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.geekmaker.paykeyboard.DefaultKeyboardListener;
import com.geekmaker.paykeyboard.IPayRequest;
import com.tencent.ToastUtil;
import com.tencent.data.bean.Auto;


import com.tencent.data.bean.EnumSweep;
import com.tencent.data.bean.EventCodeData;
import com.tencent.data.bean.PayBean;
import com.tencent.data.bean.ScanPay;
import com.tencent.di.presenter.BaseInterface;
import com.tencent.di.presenter.Mypresenter;
import com.tencent.http.Md5ut;
import com.tencent.http.SweepcodeUtis;
import com.tencent.http.TTSUtils;
import com.tencent.http.Url;

import com.tencent.ui.PayKeyboard;
import com.tencent.ui.WxfacePayLoadingDialog;
import com.tencent.wxpay.imagefacesign.R;
import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TwoActivity extends AppCompatActivity implements BaseInterface.AutoinfoInterface, BaseInterface.PayInterface, BaseInterface.ScanInterface {


    @BindView(R.id.button2)
    ImageView button2;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.button3)
    ImageView button3;
    @BindView(R.id.totals)
    EditText totals;

    private PayKeyboard keyboard;
    BaseInterface.PInterface pInterface;

    private int store;
    private String appid;
    private String key;

    String nonce_str = "abcd";
    String method = "wx_faceAuthInfo";
    private String rawdata;
    private String sign;
    private TextView textview;
    private String appid1;
    private String mch_id;
    private String out_trade_no;
    private String authinfo;
    private String nonce_str1;
    private String device_id;
    private String sub_mch_id;
    private String str1;
    private String codes;
    private String aaa;
    private String scanpays;
    private WxfacePayLoadingDialog wxfacePayLoadingDialog;
    private SweepcodeUtis sw;
    private ScheduledExecutorService heartService = Executors.newScheduledThreadPool(1);
    private String scans;
    private String moneys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        initHeart();
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initLocalVideo();

        TTSUtils.init(getApplicationContext());
        HandlerThread ht = new HandlerThread("Fast-Pay");
        ht.start();
        sw = new SweepcodeUtis(TwoActivity.this, ht, handler);

        totals.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.contains(".")) {
                    int index = text.indexOf(".");
                    if (index + 3 < text.length()) {
                        text = text.substring(0, index + 3);
                        totals.setText(text);
                        totals.setSelection(text.length());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        Intent intent = getIntent();
        store = intent.getIntExtra("store_ids", 0);
        appid = intent.getStringExtra("appids");
        key = intent.getStringExtra("keys");


        pInterface = new Mypresenter(this);


        //初始化
        WxPayFace.getInstance().initWxpayface(MyApplication.context, new IWxPayfaceCallback() {
            @Override
            public void response(Map info) throws RemoteException {
            }
        });


        WxPayFace.getInstance().getWxpayfaceRawdata(new IWxPayfaceCallback() {
            @Override
            public void response(Map info) throws RemoteException {
                if (info == null) {
                    new RuntimeException("调用返回为空").printStackTrace();
                    return;
                }
                String code = (String) info.get("return_code");
                String msg = (String) info.get("return_msg");
                String rawdata = info.get("rawdata").toString();
                Log.d("agadga", rawdata);
                if (code == null || rawdata == null || !code.equals("SUCCESS")) {
                    new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                    return;
                }
            }
        });

    }

    private void initHeart() {

        heartService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
            openKeyboard();
            }
        },0,5, TimeUnit.SECONDS);
    }

    private void initLocalVideo() {
        //设置有进度条可以拖动快进
        // MediaController localMediaController = new MediaController(this);
        //videoView.setMediaController(localMediaController);
        String uri = ("android.resource://" + getPackageName() + "/" + R.raw.videos);
        VideoView videoView = findViewById(R.id.button1);
        videoView.setVideoURI(Uri.parse(uri));
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
    }

    public void getWxpayfaceRawdata() {
        /**
         * 		作用：获取到验证信息后，才可调用获取人识别。
         * 		参数：
         * 			callback:回调返回人脸验证信息。
         */
        WxPayFace.getInstance().getWxpayfaceRawdata(new IWxPayfaceCallback() {
            @Override
            public void response(Map info) throws RemoteException {
                if (info == null) {
                    new RuntimeException("调用返回为空").printStackTrace();
                    return;
                }
                String code = (String) info.get("return_code");
                String msg = (String) info.get("return_msg");
                rawdata = info.get("rawdata").toString();
                if (code == null || rawdata == null || !code.equals("SUCCESS")) {
                    new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
                    return;
                }

                String aaa = "nonce_str=" + nonce_str + "&rawdata=" + rawdata + "&store_id=" + store + "&key=" + key;

                String bbb = Md5ut.md5Password(aaa);
                String sign = bbb.toUpperCase();

                Map<String, String> maps = new HashMap<>();
                maps.put("nonce_str", nonce_str);
                maps.put("rawdata", rawdata);
                maps.put("store_id", String.valueOf(store));


                Map<String, Object> map = new HashMap<>();

                map.put("appid", appid);
                map.put("method", method);
                map.put("sign", sign);
                map.put("data", maps);


                pInterface.Autoinfo(Url.auth_URL, map, Auto.class);

            }
        });
    }
    //调用小键盘
   /* private void initHeart() {
        openKeyboard();
    }*/

    //打开小键盘
    private void openKeyboard() {

        if (keyboard == null || keyboard.isReleased()) {
            keyboard = PayKeyboard.get(getApplicationContext());

            if (keyboard != null) {
                //keyboard.setLayout(getKeyboardLayout());
                keyboard.setLayout("1:5,2:4,3:9,4:8,5:7,6: ,7: ,8: ,9:0,12: ,13:Backspace,14:3,15:2,16:6,19:.,20:  ,31:1,21: ,23: ,28: ,29: ,30:  ");
                //keyboard.setLayout("1:5,2:4,3:9,4:8,5:7,6:F3,7:F2,8:F1,9:0,12:REFUND,13:Backspace,14:3,15:2,16:6,19:.,20:CANCEL,31:1,21:SCAN_PAY,23:FACE_PAY,28:LIST,29:OPT,30:CLEAR");
                keyboard.setBaudRate(9600);
                keyboard.setListener(new DefaultKeyboardListener() {
                    @Override
                    public void onRelease() {
                        openKeyboard();
                    }

                    @Override
                    public void onAvailable() {
                        openKeyboard();
                    }

                    @Override
                    public void onException(Exception e) {
                        openKeyboard();
                    }

                    @Override
                    public void onPay(final IPayRequest request) {

                    }

                    @Override
                    public void onKeyDown(final int keyCode, String keyName) {

                        String hnv = totals.getText().toString();

                        TTSUtils.speak(keyName);

                        if (keyCode == 19) {
                            TTSUtils.speak("点");
                        }

                        //keycode23是刷脸付款
                        //如果keycode=23,如果total值为空
                        if (keyCode == 23) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (hnv.equals("") || hnv.equals("0") || hnv.equals("0.") || hnv.equals("0.00") || hnv.equals(".0") || hnv.equals(".") || hnv.equals("0.0")) {
                                        TTSUtils.speak("请输入正确的付款金额");
                                        //不为空走扫码付款接口
                                    } else if (Double.parseDouble(hnv) >= 1000) {
                                        ToastUtil.showToast(TwoActivity.this, "单笔支付金额不能大于1000", Toast.LENGTH_SHORT);
                                    } else {
                                        getWxpayfaceRawdata();
                                    }
                                }
                            });
                        }
                        //keycode21是扫码付款
                        //如果keycode=21,如果total值为空
                        if (keyCode == 21) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (hnv.equals("") || hnv.equals("0") || hnv.equals("0.") || hnv.equals("0.00") || hnv.equals(".0") || hnv.equals(".") || hnv.equals("0.0")) {
                                        TTSUtils.speak("请输入正确的付款金额");
                                        //不为空走付款接口
                                    } else {
                                        stopCodeScanner();
                                        double sacnpay = Double.valueOf(totals.getText().toString());
                                        BigDecimal bg = new BigDecimal(sacnpay * 100);
                                        double doubleValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                        scans = String.valueOf((int) doubleValue);
                                        TTSUtils.speak("请出示二维码付款");
                                        //sw.openSweep();

                                        startCodeScanner();
                                    }
                                }
                            });
                        }

                        Message mgs = new Message();
                        mgs.what = 4;

                        if (keyCode == 13) {
                            TTSUtils.speak("");
                            mgs.obj = keyName.substring(0, keyName.length() - 9);
                            handler.sendMessage(mgs);
                        }


                        if (keyCode == 20) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //sw.closeSweep();
                                    stopCodeScanner();
                                    totals.setText("");
                                }
                            });
                        }


                        if (keyCode == 30) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    stopCodeScanner();
                                    TTSUtils.speak("清除");
                                    totals.setText("");
                                }
                            });
                        }

                        Message mg = new Message();
                        mg.what = 5;
                        if (keyCode == 13) {
                            stopCodeScanner();
                            mg.obj = keyName.substring(0, keyName.length() - 9);
                            handler.sendMessage(mg);
                        } else {
                            mg.obj = keyName;
                            handler.sendMessage(mg);
                        }
                    }

                    @Override
                    public void onKeyUp(int keyCode, String keyName) {

                    }
                });
                keyboard.open();
            }else {
                button3.setVisibility(View.GONE);
                //ToastUtil.showToast(TwoActivity.this, "小键盘连接失败", Toast.LENGTH_SHORT);

            }

        }

    }

    private String getKeyboardLayout() {
        Map<String, String> keyboardMap = new HashMap<>();
        keyboardMap.put("9", "0");
        keyboardMap.put("31", "1");
        keyboardMap.put("15", "2");
        keyboardMap.put("14", "3");
        keyboardMap.put("2", "4");
        keyboardMap.put("1", "5");
        keyboardMap.put("16", "6");
        keyboardMap.put("5", "7");
        keyboardMap.put("4", "8");
        keyboardMap.put("3", "9");
        keyboardMap.put("19", ".");
        keyboardMap.put("13", "BACKSPACE");
        keyboardMap.put("12", "REFUND");
        keyboardMap.put("28", "AUDIT");
        keyboardMap.put("29", "SET");
        keyboardMap.put("30", "CLEAR");
        keyboardMap.put("20", "CANCEL");
        keyboardMap.put("21", "SCAN_CODE");
        keyboardMap.put("23", "SCAN_FACE");
        keyboardMap.put("8", "F1");
        keyboardMap.put("7", "F2");
        keyboardMap.put("6", "F3");
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : keyboardMap.entrySet()) {
            stringBuffer.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }
        return stringBuffer.toString();
    }


    @OnClick({R.id.button2, R.id.button3})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.button3:
                String moneys = totals.getText().toString();

                if (moneys.equals("") || moneys.equals("0") || moneys.equals("0.") || moneys.equals("0.00") || moneys.equals(".0") || moneys.equals(".") || moneys.equals("0.0")) {
                    TTSUtils.speak("请输入正确的付款金额");
                } else if (Double.parseDouble(moneys) >= 1000) {
                    ToastUtil.showToast(TwoActivity.this, "单笔金额不能超过1000元", Toast.LENGTH_SHORT);

                } else {
                    getWxpayfaceRawdata();
                }
                break;

            case R.id.button2:

                /*String money = totals.getText().toString();
                if (money.equals("") || money.equals("0") || money.equals("0.") || money.equals("0.00") || money.equals(".0") || money.equals(".") || money.equals("0.0")) {
                    TTSUtils.speak("请输入正确的付款金额");
                } else {
                    double sacnpay = Double.valueOf(totals.getText().toString());
                    BigDecimal bg = new BigDecimal(sacnpay * 100);
                    double doubleValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    scans = String.valueOf((int) doubleValue);
                    sw.openSweep();
                }*/

                stopCodeScanner();
                String pays = totals.getText().toString();
                if (pays.equals("")||pays.equals("0")||pays.equals("0.")||pays.equals("0.00")||pays.equals(".0")||pays.equals(".")||pays.equals("0.0")){
                    TTSUtils.speak("请输入正确的支付金额");
                } else {
                    TTSUtils.speak("请出示微信或支付宝付款码");
                    startCodeScanner();
                }

                break;
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            try {
                switch (msg.what) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:
                        CharSequence str3 = totals.getText();
                        String str = str3 + (msg.obj.toString());
                        String s4 = str.substring(0, str.length() - 1);
                        totals.setText(s4);
                        keyboard.updateDisplay(totals.getText().toString());
                        break;
                    case 5:

                        CharSequence str4 = totals.getText();


                        /*if (msg.obj.toString().startsWith("0") && str4.toString().trim().length() >1) {
                            if (!msg.obj.toString().substring(1,2).equals(".")) {
                                totals.setText(msg.obj.toString().subSequence(0, 1));
                                keyboard.updateDisplay(totals.getText().toString());
                                break;
                            }
                        }*/

                        if (str4.toString().equals("0") && msg.obj.toString().equals("0")) {
                            break;
                        }
                        if (str4.equals("") && msg.obj.toString().equals(".")) {
                            break;
                        }

                        if (msg.obj.toString().equals(".")) {
                            //判断str4里有没有点有点就返回点的位置，没点就返回-1
                            if (String.valueOf(str4).indexOf(".") != -1) {
                                //如果str4存在点就不值行下面代码
                                break;
                            }

                        }

                        str4 = str4 + (msg.obj.toString());
                        if (!str4.equals(".")) {
                            totals.setText(str4);
                            keyboard.updateDisplay(totals.getText().toString());
                        } else {
                            totals.setText("0.");
                        }

                        //totals.setText(str4);
                        //keyboard.updateDisplay(totals.getText().toString());

                        break;
                }
            } catch (Exception e) {

            }
        }
    };
    private Runnable task = new Runnable() {
        public void run() {

            sw.openSweep();

            handler.postDelayed(this, 1000);//设置延迟时间，此处是1秒
              /*
                所需要执行的任务
              */
        }
    };


    public void getWxpayfaceCode() {
        Map<String, String> m2 = new HashMap<>();
        m2.put("appid", appid); // 公众号，必填

        double money = Double.valueOf(totals.getText().toString());

        //(Math.Round((decimal)order.Amount * 100, 0)).ToString()

        BigDecimal bg = new BigDecimal(money * 100);
        double doubleValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        aaa = String.valueOf((int) doubleValue);


        String face_code_type = "0";
        //m2.put("sub_appid", subAppcaseId); // 子商户公众账号ID(非服务商模式不填)
        m2.put("mch_id", mch_id); // 商户号，必填
        m2.put("face_code_type", face_code_type);
        m2.put("sub_mch_id", sub_mch_id); // 子商户号(非服务商模式不填)
        m2.put("store_id", store + ""); // 门店编号，必填
        m2.put("out_trade_no", out_trade_no); // 商户订单号， 必填
        m2.put("total_fee", aaa + ""); // 订单金额（数字），单位：分，必填
        m2.put("face_authtype", "FACEPAY"); // FACEPAY：人脸凭证，常用于人脸支付    FACEPAY_DELAY：延迟支付   必填
        m2.put("ask_face_permit", "0"); // 展开人脸识别授权项，详情见上方接口参数，必填
        m2.put("authinfo", authinfo); // 展开人脸识别授权项，详情见上方接口参数，必填
        m2.put("ask_ret_page", "1"); // 是否展示微信支付成功页，可选值："0"，不展示；
        WxPayFace.getInstance().getWxpayfaceCode(m2, new IWxPayfaceCallback() {
            @Override
            public void response(Map map) throws RemoteException {
                if (map == null) {
                    new RuntimeException("调用返回为空").printStackTrace();
                    return;
                }
                if ("SUCCESS".equals(map.get("return_code"))) {
                    //wxFaceOrder((String) map.get("openid"), (String) map.get("face_code"));

                    String code = (String) map.get("return_code"); // 错误码
                    String msg = (String) map.get("return_msg"); // 错误码描述
                    String faceCode = map.get("face_code").toString(); // 人脸凭证，用于刷脸支付
                    String openid = map.get("openid").toString(); // openid

                    String pays = "faceCode=" + faceCode + "&nonce_str=" + nonce_str + "&openId=" + openid + "&out_trade_no=" + out_trade_no + "&storeId=" + store + "&total=" + aaa + "&key=" + key;

                    String bbb = Md5ut.md5Password(pays);
                    String signs = bbb.toUpperCase();
                    String method1 = "wx_facePay";

                    Map<String, String> mapPayResults = new HashMap<>();
                    mapPayResults.put("storeId", store + "");
                    mapPayResults.put("total", aaa + "");//单位 分
                    mapPayResults.put("openId", openid);
                    mapPayResults.put("faceCode", faceCode);
                    mapPayResults.put("out_trade_no", out_trade_no);
                    mapPayResults.put("nonce_str", nonce_str);

                    Map<String, Object> pay = new HashMap<>();
                    pay.put("appid", appid);
                    pay.put("method", method1);
                    pay.put("sign", signs);
                    pay.put("data", mapPayResults);
                    pInterface.Pay(Url.auth_Pay, pay, PayBean.class);
                    finish();

                }
            }
        });
    }


    @Override
    public void Success(Object o) {
        Auto auto = (Auto) o;
        if (auto.getCode() == 100) {
            //appid1 = auto.getData().getAppid();
            mch_id = auto.getData().getMch_id();
            out_trade_no = auto.getData().getOut_trade_no();
            authinfo = auto.getData().getAuth_info();
            nonce_str1 = auto.getData().getNonce_str();
            device_id = auto.getData().getDevice_id();
            sub_mch_id = auto.getData().getSub_mch_id();
            getWxpayfaceCode();
        } else {
            Toast.makeText(this, auto.getMsg(), Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    public void SuccessFul(Object o) {
        PayBean payBean = (PayBean) o;

        if (payBean.getCode() == 100) {

            Intent intent = new Intent(TwoActivity.this, SuccessActivity.class);
            int moneys = payBean.getData().getTotal_fee();
            intent.putExtra("moneys", moneys);

            intent.putExtra("store_ids", store);

            intent.putExtra("appids", appid);
            intent.putExtra("keys", key);
            startActivity(intent);
            finish();
            totals.setText("");
        } else {
            Intent intent = new Intent(TwoActivity.this, FailActivity.class);
            intent.putExtra("store_ids", store);
            intent.putExtra("appids", appid);
            intent.putExtra("keys", key);
            startActivity(intent);
            finish();

            totals.setText("");

        }
    }

    @Override
    public void ScanSuccess(Object o) {
        ScanPay scanPay = (ScanPay) o;

        if (scanPay.getCode() == 100) {

            Intent intent = new Intent(TwoActivity.this, SuccessFulActivity.class);
            int moneys = scanPay.getData().getTotal();
            intent.putExtra("totals", moneys);
            intent.putExtra("store_ids", store);
            intent.putExtra("appids", appid);
            intent.putExtra("keys", key);

            startActivity(intent);
            finish();
            totals.setText("");

        } else {

            Intent intent = new Intent(TwoActivity.this, FailActivity.class);
            intent.putExtra("store_ids", store);
            intent.putExtra("appids", appid);
            intent.putExtra("keys", key);
            startActivity(intent);
            finish();

            totals.setText("");
        }
    }

    @Override
    public void showPayLoading() {
        wxfacePayLoadingDialog = new WxfacePayLoadingDialog(this);
        wxfacePayLoadingDialog.show();
    }

    @Override
    public void dismissPayLoading() {
        wxfacePayLoadingDialog.dismiss();
    }

    public void startCodeScanner() {

        double sacnpay = Double.valueOf(totals.getText().toString());
        BigDecimal bg = new BigDecimal(sacnpay * 100);
        double doubleValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        scanpays = String.valueOf((int) doubleValue);


        WxPayFace.getInstance().startCodeScanner(new IWxPayfaceCallback() {
            @Override
            public void response(Map map) throws RemoteException {
                //Toast.makeText(ShowActivity.this, "startCodeScanner", Toast.LENGTH_SHORT).show();
                if (map != null) {
                    String return_code = (String) map.get("return_code");
                    String return_msg = (String) map.get("return_msg");
                    final String code_msg = (String) map.get("code_msg");
                    final String resultString = "startCodeScanner, return_code : " + return_code + " return_msg : " + return_msg + " code_msg: " + code_msg;

                    Log.i("agaxax", code_msg);

                    TwoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String pays = "authCode=" + code_msg + "&nonce_str=" + nonce_str + "&store_id=" + store + "&total=" + scanpays + "&key=" + key;
                            String bbb = Md5ut.md5Password(pays);

                            String signs = bbb.toUpperCase();
                            String method1 = "auto_pay";

                            Map<String, String> scanpay = new HashMap<>();
                            scanpay.put("authCode", code_msg + "");
                            scanpay.put("nonce_str", nonce_str);
                            scanpay.put("store_id", store + "");//单位 分
                            scanpay.put("total", scanpays);


                            Map<String, Object> map1 = new HashMap<>();
                            map1.put("appid", appid);
                            map1.put("method", method1);
                            map1.put("sign", signs);
                            map1.put("data", scanpay);

                            pInterface.ScanPay(Url.auth_ScanPay, map1, ScanPay.class);

                        }
                    });

                }
            }
        });


        /*WxPayFace.getInstance().getWxpayfaceUserInfo(params2, new IWxPayfaceCallback() {
            @Override
            public void response(final Map info) throws RemoteException {
                if (mFaceCallback != null) {
                    mFaceCallback.post(new Runnable() {
                        @Override
                        public void run() {
                            mFaceCallback.setText("response | getWxpayfaceUserInfo " + info.toString());
                        }
                    });
                }
                Log.d(TAG, "response | getWxpayfaceUserInfo " + info.toString());
            }

        });*/

    }

    public void stopCodeScanner() {
        WxPayFace.getInstance().stopCodeScanner();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleOpenSweep(EnumSweep enumSweep) {
        switch (enumSweep) {
            case OPEN_SUCCESS:
                sw.readData();
                break;
            case CLOSE_SUCCESS:
                sw.readData();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventCodeData(EventCodeData eventCodeData) {

        Log.d("Auchcode", eventCodeData.getCodeData());

        String pays = "authCode=" + eventCodeData.getCodeData() + "&nonce_str=" + nonce_str + "&store_id=" + store + "&total=" + scans + "&key=" + key;
        String bbb = Md5ut.md5Password(pays);


        String method1 = "auto_pay";
        String signs = bbb.toUpperCase();
        Map<String, String> scan = new HashMap<>();
        scan.put("authCode", eventCodeData.getCodeData() + "");
        scan.put("nonce_str", nonce_str);
        scan.put("store_id", store + "");//单位 分
        scan.put("total", scans);

        Log.d("gavax", scan + "");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("appid", appid);
        map1.put("method", method1);
        map1.put("sign", signs);
        map1.put("data", scan);
        Log.d("gsfgdf", map1 + "");
        pInterface.ScanPay(Url.auth_ScanPay,map1, ScanPay.class);

    }

}
