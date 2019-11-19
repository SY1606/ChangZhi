package com.tencent.utis;


import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import com.elvishew.xlog.XLog;

import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.File;
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
    public SweepcodeUtis(Context context, HandlerThread ht, Handler handler){

        this.context = context;
        this.ht = ht;
        this.handler = handler;
        initScan();

    }
    //初始话扫码头
    public void initScan(){
        if (mScanSerialPort == null) {//优先串口
            writeExcutor.submit(()->{
                try {
                    File ttyACM0 = new File("/dev/ttyACM0");//串口
                    if(!ttyACM0.canRead() || !ttyACM0.canWrite()){
                        ttyACM0 = new File("/dev/ttyACM1");//u转串
                        if(!ttyACM0.canRead() || !ttyACM0.canWrite()){
                            ttyACM0 = new File("/dev/ttyS4");
                            if(!ttyACM0.canRead() || !ttyACM0.canWrite()){
                                return;
                            }
                        }
                    }

                    mHandler = new Handler(ht.getLooper(), callback);
                    mScanSerialPort = new SerialPort(ttyACM0, 9600, 0);
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
                    XLog.d("init 20s res:"+ sb);

                    if("02000001003331".equals(sb.toString())){
                        XLog.d("init 20s success");
                    }
                } catch (Exception e) {
                    XLog.e("", e);
                }
            });
        }
    }

    public void openSweep(){
        mHandler.post(()->{
            try {
                OutputStream out = mScanSerialPort.getOutputStream();
                byte[] command = Hex.decodeHex("7E000801000201ABCD".toCharArray());//开启扫码
                out.write(command);
                out.flush();
                InputStream is = mScanSerialPort.getInputStream();
                StringBuilder sb = new StringBuilder();
                byte[] buf = new byte[7];//u转串
                is.read(buf);
                sb.append(Hex.encodeHex(buf));


                if (is == null) {
                    return;
                }
//                if("02000001003331".equals(sb.toString())) {
//
//                }
                StringBuilder data = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while (br.ready()) {
                    data.append(br.readLine());
                }
                Message mg = new Message();
                if (!TextUtils.isEmpty(data)) {
                    mg.obj = data;
                    mg.what=2;

                }else{
                    mg.what=3;
                }
                handler.sendMessage(mg);
//

            }catch (Exception e){


            }
        });
    }


    //关闭扫码头
    public void closeSweep(){
        writeExcutor.submit(()->{
            try {
                if(mScanSerialPort == null){
                    return;
                }
                OutputStream out = mScanSerialPort.getOutputStream();
                byte[] command = Hex.decodeHex("7E000801000200ABCD".toCharArray());
                out.write(command);
                out.flush();
                InputStream is = mScanSerialPort.getInputStream();
                StringBuilder sb = new StringBuilder();
                byte[] buf = new byte[7];
                is.read(buf);
                sb.append(Hex.encodeHex(buf));
                XLog.d("command res:"+ sb);
                if("02000001003331".equals(sb.toString())){
                    canScan = false;
                }
            }catch (Exception e){
                XLog.d("",e);
            }
        });
    }
}
