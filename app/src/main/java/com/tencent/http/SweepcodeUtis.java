package com.tencent.http;


import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.elvishew.xlog.XLog;

import com.tencent.data.bean.EnumSweep;
import com.tencent.data.bean.EventCodeData;

import org.apache.commons.codec.binary.Hex;
import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.SerialPort;

public class SweepcodeUtis {

    private boolean canScan;
    private Handler mHandler;
    private SerialPort mScanSerialPort = null;
    private ExecutorService writeExcutor = Executors.newSingleThreadExecutor();
    private Context context;
    private Handler.Callback callback;
    private Handler handler;
    private HandlerThread ht;

    public SweepcodeUtis(Context context, HandlerThread ht, Handler handler) {

        this.context = context;
        this.ht = ht;
        this.handler = handler;
        new Thread() {
            @Override
            public void run() {
                super.run();
                initScan();
            }
        }.start();

    }

    //初始话扫码头
    public void initScan() {
        if (mScanSerialPort == null) {//优先串口
            try {
                File ttyACM0 = new File("/dev/ttyACM0");//串口
                if (!ttyACM0.canRead() || !ttyACM0.canWrite()) {
                    ttyACM0 = new File("/dev/ttyACM1");//u转串
                    if (!ttyACM0.canRead() || !ttyACM0.canWrite()) {
                        ttyACM0 = new File("/dev/ttyS4");
                        if (!ttyACM0.canRead() || !ttyACM0.canWrite()) {
                            return;
                        }
                    }
                }
                mHandler = new Handler(ht.getLooper(), callback);
                try {
                    mScanSerialPort = new SerialPort(ttyACM0, 9600, 0);
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                OutputStream out = mScanSerialPort.getOutputStream();
                //设置读取时间
                byte[] command = Hex.decodeHex("7E0008010006C8ABCD".toCharArray());
                out.write(command);
                out.flush();
                InputStream is = mScanSerialPort.getInputStream();
                StringBuilder sb = new StringBuilder();
                byte[] buf = new byte[7];
                is.read(buf);
                sb.append(Hex.encodeHex(buf));
                Log.d("luyun", "init 20s res:" + sb);

                if ("02000001003331".equals(sb.toString())) {
                    Log.d("luyun", "init 20s success");
                } else {
                    Log.d("luyun", "init 20s failed");
                }
            } catch (Exception e) {
                Log.e("luyun", e.toString());
            }
        }
    }

    public void openSweep() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    OutputStream out = mScanSerialPort.getOutputStream();
                    byte[] command = Hex.decodeHex("7E000801000201ABCD".toCharArray());//开启扫码
                    out.write(command);
                    out.flush();
                    EventBus.getDefault().post(EnumSweep.OPEN_SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



    public void readData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    InputStream is = mScanSerialPort.getInputStream();
                    if (is == null) {
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    byte[] buf = new byte[7];//u转串
                    int result = is.read(buf);
                    Log.d("luyun","qwe:  " + result);
                    sb.append(Hex.encodeHex(buf));
                    StringBuilder data = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                    while (br.ready()) {
                        data.append(br.readLine());
//                    }
                    Log.d("luyun", "abc" + data.toString());
                    EventCodeData codeData = new EventCodeData(data.toString());
                    EventBus.getDefault().post(codeData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    //关闭扫码头
    public void closeSweep() {
        writeExcutor.submit(() -> {
            try {
                OutputStream out = mScanSerialPort.getOutputStream();
                if (mScanSerialPort == null) {
                    return;
                }
                byte[] command = Hex.decodeHex("7E000801000200ABCD".toCharArray());

                out.write(command);
                out.flush();
                EventBus.getDefault().post(EnumSweep.CLOSE_SUCCESS);
//                InputStream is = mScanSerialPort.getInputStream();
//                StringBuilder sb = new StringBuilder();
//                byte[] buf = new byte[7];
//                is.read(buf);
//                sb.append(Hex.encodeHex(buf));
//                XLog.d("command res:" + sb);
//                if ("02000001003331".equals(sb.toString())) {
//                    canScan = false;
//                }
            } catch (Exception e) {
                XLog.d("egwgfgf", e);
            }finally {
            }
        });
    }
}
