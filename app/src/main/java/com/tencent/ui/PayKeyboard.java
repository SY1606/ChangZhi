package com.tencent.ui;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.elvishew.xlog.XLog;
import com.geekmaker.paykeyboard.DefaultKeyboardListener;
import com.geekmaker.paykeyboard.I2C;
import com.geekmaker.paykeyboard.IKeyboardListener;
import com.geekmaker.paykeyboard.IResponse;
import com.geekmaker.paykeyboard.Layout;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.hoho.android.usbserial.util.SerialInputOutputManager.Listener;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class PayKeyboard implements Listener {
    private final Context context;
    private final UsbSerialDriver driver;
    private final UsbDevice device;
    private BroadcastReceiver deattachReceiver;
    private Timer timer;
    private IKeyboardListener listener;
    private static AtomicInteger SEQ = new AtomicInteger(0);
    private SerialInputOutputManager serialManager;
    private static final String ACTION_USB_PERMISSION = "com.geekmaker.USB_PERMISSION";
    private static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    private static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private Map<Integer, IResponse> requests = new HashMap<>();
    private Map<Integer, String> keyNameMap = new HashMap<>();
    private boolean lock = false;

    private boolean released = false;
    private RetryTask lastUpdateTask;
    private UsbDeviceConnection connection;
    private UsbSerialPort port;
    private BroadcastReceiver usbReceiver;
    private int baudRate = 9600;

    private PayKeyboard(UsbSerialDriver driver, Context context) {
        this.context = context;
        this.driver = driver;
        this.device = driver.getDevice();
        this.timer = new Timer();
        this.listener = new DefaultKeyboardListener();
        this.setLayout(0);
    }

    public void setLayout(int index) {
        this.setLayout(Layout.getLayout(index));
    }

    public void setLayout(String layoutString) {
        this.setLayout(Layout.parse(layoutString));
    }

    public void setLayout(Map<Integer, String> layout) {
        this.keyNameMap = layout;
    }

    public void setBaudRate(int rate) {
        this.baudRate = rate;
    }

    public Map<Integer, String> getLayout() {
        return this.keyNameMap;
    }

    public void open() {
        Log.i("KeyboardSDK", "try open keyboard");
        UsbManager manager = (UsbManager)this.context.getSystemService(Context.USB_SERVICE);
        if (manager != null && manager.hasPermission(this.device)) {
            this.initPort();
        } else {
            Log.i("KeyboardSDK", "try request usb permission");
            this.tryGetPermisson();
        }
    }

    private void tryGetPermisson() {
        PendingIntent permissionIntent = PendingIntent.getBroadcast(this.context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        this.usbReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action)) {
                    UsbDevice device = intent.getParcelableExtra("device");
                    if (intent.getBooleanExtra("permission", false)) {
                        if (device != null) {
                            PayKeyboard.this.initPort();
                        } else {
                            PayKeyboard.this.listener.onException(new PermissonDeniedException("no permisson"));
                        }
                    } else {
                        Log.d("KeyboardSDK", "permission denied for device " + device);
                        PayKeyboard.this.listener.onException(new PermissonDeniedException("no permisson"));
                    }
                }

            }
        };
        this.context.registerReceiver(this.usbReceiver, filter);
        UsbManager manager = (UsbManager)this.context.getSystemService(Context.USB_SERVICE);
        if(manager != null){
            manager.requestPermission(this.device, permissionIntent);
        }
    }

    private void initPort() {
        UsbManager manager = (UsbManager)this.context.getSystemService(Context.USB_SERVICE);
        if(manager != null){
            this.connection = manager.openDevice(this.device);
        }
        this.port = this.driver.getPorts().get(0);

        try {
            this.port.open(this.connection);
        } catch (Exception var4) {
            this.listener.onException(var4);
            return;
        }

        try {
            this.port.setParameters(this.baudRate, 0, 0, 0);
        } catch (Exception var3) {
            this.listener.onException(var3);
        }

        this.serialManager = new SerialInputOutputManager(this.port, this);
        (new Thread(this.serialManager)).start();
        this.reset();
        this.deattachReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (ACTION_USB_DETACHED.equals(action)) {
                    UsbDevice device = intent.getParcelableExtra("device");
                    if (device != null) {
                        Log.i("KeyboardSDK", "USB detached !!!!!!");
                        PayKeyboard.this.release();
                        PayKeyboard.this.listener.onRelease();
                    }
                }
                if (ACTION_USB_ATTACHED.equals(action)) {
                    UsbDevice device = intent.getParcelableExtra("device");
                    if (device != null) {
                        Log.i("KeyboardSDK", "USB attached !!!!!!");
                        PayKeyboard.this.open();
                        PayKeyboard.this.listener.onAvailable();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_DETACHED);
        filter.addAction(ACTION_USB_ATTACHED);
        this.context.registerReceiver(this.deattachReceiver, filter);
        this.listener.onAvailable();
    }

    public void showTip(String tip) {
        Log.d("KeyboardSDK", String.format("show tip %s", tip));
        if (tip.length() > 8) {
            tip = tip.substring(0, 8);
        }
        if (tip.length() < 7) {
            StringBuilder tmp = new StringBuilder(tip);
            int padLen = (8 - tip.length()) / 2;

            for(int i = 0; i < padLen; ++i) {
                tmp.append(" ");
            }
            tip = tmp.toString();
        }
        this.sendRequest((new I2C(tip, false)).toBytes(), (byte)28);
    }

    public void updateDisplay(String string) {
        Log.d("KeyboardSDK", String.format("last update %s", string));
        if(string.length() <=0){
            string = "0";
        }
        if (string.length() > 8) {
            string = string.substring(0, 8);
        }
        if (this.lastUpdateTask != null) {
            this.lastUpdateTask.cancel();
            this.lastUpdateTask = null;
        }

        this.sendRequest(new I2C(string).toBytes(), (byte)28);
    }

    public static PayKeyboard get(Context context) {
        UsbManager manager = (UsbManager)context.getSystemService(Context.USB_SERVICE);
        if(manager == null){
            return null ;
        }
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        UsbSerialDriver driver = null;

        for(int i = 0; i < availableDrivers.size(); ++i) {
            if ((availableDrivers.get(i)).getDevice().getVendorId() == 6790
                    && (availableDrivers.get(i)).getDevice().getProductId() == 29987) {
                driver = availableDrivers.get(i);
                break;
            }
        }

        if (driver == null) {
            Log.i("KeyboardSDK", "no  keyboard attached");
            return null;
        } else {
            Log.i("KeyboardSDK", "keyboard is attached");
            return new PayKeyboard(driver, context);
        }
    }

    public void setListener(IKeyboardListener listener) {
        this.listener = listener;
    }

    public void onNewData(byte[] rawData) {
        Log.d("KeyboardSDK", "new data :" + HexDump.dumpHexString(rawData));
        if (rawData[0] == 6 || rawData[0] == 2) {
            ByteBuffer buffer = ByteBuffer.wrap(rawData);
            while(buffer.remaining() > 0) {
                byte head = buffer.get();
                boolean isResponse = head == 6;
                if (isResponse) {
                    head = buffer.get();
                }
                if (head != 2) {
                    return;
                }
                int len = buffer.getShort() & 255;
                int lrc = this.lrc(rawData, buffer.position() - 2, buffer.position() + len);
                int seq = buffer.get() & 255;
                int type = buffer.get() & 255;
                short errorCode = (short)(buffer.getShort() & 255);
                byte[] resp = new byte[len - 4];
                buffer.get(resp);
                if (buffer.get() != 3) {
                    return;
                }

                if ((buffer.get() & 255) != lrc) {
                    return;
                }

                if (isResponse) {
                    if (this.requests.containsKey(seq)) {
                        IResponse response = this.requests.remove(seq);
                        if (this.lastUpdateTask != null && this.lastUpdateTask.seq <= seq) {
                            this.lastUpdateTask.cancel();
                            this.lastUpdateTask = null;
                        }

                        if (errorCode != 0) {
                            Log.w("KeyboardSDK", String.format("response error!!!! %S", errorCode));
                            response.onError(errorCode, seq);
                        } else {
                            response.onResult(resp, seq);
                        }
                    }
                } else if (type == 163) {
                    int keyCode = resp[1] & 255;
                    if (!this.keyNameMap.containsKey(keyCode)) {
                        return;
                    }
                    String keyName = this.keyNameMap.get(keyCode);
                    if (this.lock) {
                        this.listener.onKeyDown(keyCode, keyName);
                        return;
                    }
                    if (this.listener != null) {
                        if (resp[0] != 1) {
                            this.listener.onKeyUp(keyCode, keyName);
                        } else {
                            this.listener.onKeyDown(keyCode, keyName);
                        }
                    }
                }
            }
        }

    }

    public void reset() {
        this.lock = false;
        this.updateDisplay("0");

    }

    private int lrc(byte[] data, int start, int end) {
        Log.d("KeyboardSDK", "lrc start " + start + ",end " + end);
        int lrc = 0;

        for(int i = start; i <= end; ++i) {
            lrc = (lrc ^ data[i] & 255) & 255;
        }

        return lrc;
    }

    private int sendRequest(byte[] data, byte type) {
        return this.sendRequest(data, type, new IResponse() {
            public void onResult(byte[] data, int seq) {
            }

            public void onError(short code, int seq) {
            }
        });
    }

    private synchronized int sendRequest(byte[] data, byte type, IResponse response) {
        byte[] command = new byte[data.length + 7];
        command[0] = 2;
        int len = data.length + 2;
        command[1] = (byte)(len >> 8 & 255);
        command[2] = (byte)(len & 255);
        int seq = SEQ.getAndIncrement();
        command[3] = (byte)(seq & 255);
        command[4] = type;

        System.arraycopy(data, 0, command, 5, data.length);

        command[command.length - 2] = 3;
        command[command.length - 1] = (byte)this.lrc(command, 1, command.length - 2);
        Log.i("KeyboardSDK", "write command " + HexDump.dumpHexString(command));
        this.requests.put(seq, response);

        try {
            if(serialManager != null){
                this.serialManager.writeAsync(command);
            }
        } catch (Exception var8) {
            this.listener.onException(var8);
            Log.w("KeyboardSDK", "write to keyboard fail,please check connection!");
        }

        if (this.lastUpdateTask != null) {
            this.lastUpdateTask.cancel();
        }

        this.lastUpdateTask = new RetryTask(seq, data, type);
        if(this.timer != null){
            this.timer.schedule(this.lastUpdateTask, 400L);
        }
        return seq;
    }

    public void release() {
        this.released = true;
        if (this.serialManager != null) {
            this.serialManager.stop();
            this.serialManager = null;
        }

        if (this.connection != null) {
            this.connection.close();
            this.connection = null;
        }

        if (this.port != null) {
            try {
                this.port.close();
            } catch (IOException var2) {
                //
            }

            this.port = null;
        }

        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }

        if (this.usbReceiver != null) {
            Log.i("KeyboardSDK", "unregister permission recevier");
            this.context.unregisterReceiver(this.usbReceiver);
            this.usbReceiver = null;
        }

        if (this.deattachReceiver != null) {
            Log.i("KeyboardSDK", "unregister deattatch recevier");
            this.context.unregisterReceiver(this.deattachReceiver);
            this.deattachReceiver = null;
        }

    }

    public boolean isReleased() {
        return this.released;
    }

    public void onRunError(Exception e) {
        this.listener.onException(e);
    }

    private class RetryTask extends TimerTask {
        final int seq;
        private final byte[] data;
        private final byte type;

        RetryTask(int seq, byte[] data, byte type) {
            this.seq = seq;
            this.data = data;
            this.type = type;
        }

        public void run() {
            Log.w("KeyboardSDK", "retry....");
            PayKeyboard.this.requests.clear();
            PayKeyboard.this.sendRequest(this.data, this.type);
        }
    }

    class PermissonDeniedException extends Exception {
        PermissonDeniedException(String no_permisson) {
            super(no_permisson);
        }
    }

}
