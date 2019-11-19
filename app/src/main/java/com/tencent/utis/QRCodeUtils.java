package com.tencent.utis;

import android.graphics.Bitmap;

import com.elvishew.xlog.XLog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 二维码工具
 */
public class QRCodeUtils {

    private static final String TAG = "QRCodeUtils";

    /**
     * 生成二维码 要转换的地址或字符串,可以是中文
     *
     * @param content 二维码内容
     * @param width   宽
     * @param height  高
     * @return 二维码图片
     */
    public static Bitmap createQRImage(String content, final int width, final int height) {
        try {
            // 判断URL合法性
            if (content == null || "".equals(content) || content.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            XLog.w(TAG, e);
        }
        return null;
    }

    /**
     * 解析二维码图片
     *
     * @param srcBitmap 图片码
     * @return 二维码内容
     */
    public static String decodeQR(Bitmap srcBitmap) {
        if (srcBitmap != null) {
            int width = srcBitmap.getWidth();
            int height = srcBitmap.getHeight();
            int[] pixels = new int[width * height];
            srcBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            // 新建一个RGBLuminanceSource对象
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            // 将图片转换成二进制图片
            BinaryBitmap binaryBitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
            QRCodeReader reader = new QRCodeReader();// 初始化解析对象
            try {
                Map<DecodeHintType, Object> decodeHints = new EnumMap<>(DecodeHintType.class);
                List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
                formats.add(BarcodeFormat.QR_CODE);
                decodeHints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
                Result result = reader.decode(binaryBitmap, decodeHints);// 开始解析
                return result.getText();
            } catch (Exception e) {
                XLog.w(TAG, e);
            }
        }
        return "";
    }

}
