package com.tencent.ui;

import android.util.Log;

import com.aill.androidserialport.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Handler;

public class SerialPortUtil {


    /**
     * 标记当前串口状态(true:打开,false:关闭)
     **/
    public static boolean isFlagSerial = false;

    public static SerialPort serialPort = null;
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;
    public static Thread receiveThread = null;
    public static String strData = "";
    public static Handler mHandler;


    /**
     * 打开串口
     */
    public static boolean open() {
        boolean isopen = false;
        if(isFlagSerial){
//            LogUtils.e(TAG,"串口已经打开,打开失败");
            return false;
        }
            try {
                File ttyACM0 = new File("/dev/ttyXRUSB0");
            if(!ttyACM0.canRead() || !ttyACM0.canWrite()){
                ttyACM0 = new File("/dev/ttyS4");
            }
            serialPort = new SerialPort(ttyACM0, 9600, 0);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            receive();
            isopen = true;
            isFlagSerial = true;
        } catch (IOException e) {
            e.printStackTrace();
            isopen = false;
        }
        return isopen;
    }

    /**
     * 关闭串口
     */
    public static boolean close() {
        if(isFlagSerial){
//            LogUtils.e(TAG,"串口关闭失败");
            return false;
        }
        boolean isClose = false;
//        LogUtils.e(TAG, "关闭串口");
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            isClose = true;
            isFlagSerial = false;//关闭串口时，连接状态标记为false
        } catch (IOException e) {
            e.printStackTrace();
            isClose = false;
        }
        return isClose;
    }

    /**
     * 发送串口指令
     */
    public static void sendString(String data) {
//        mHandler = handler;
        if (!isFlagSerial) {
//            LogUtils.e(TAG, "串口未打开,发送失败" + data);
            return;
        }
        try {
            outputStream.write(data.getBytes("UTF-8"));

            outputStream.flush();
//            LogUtils.e(TAG, "sendSerialData:" + data);
        } catch (IOException e) {
            e.printStackTrace();
//            LogUtils.e(TAG, "发送指令出现异常");
        }
    }

    /**
     * 接收串口数据的方法
     */
    public static void receive() {
        if (receiveThread != null && !isFlagSerial) {
            return;
        }
        receiveThread = new Thread() {
            @Override
            public void run() {
                while (isFlagSerial) {
                    try {
                        byte[] readData = new byte[32];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);
                        if (size > 0 && isFlagSerial) {

                            strData = new String(readData,"UTF-8");
//                            LogUtils.e(TAG, "readSerialData:" + strData);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveThread.start();
    }
}
