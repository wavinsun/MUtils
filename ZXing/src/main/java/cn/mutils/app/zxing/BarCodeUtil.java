package cn.mutils.app.zxing;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;

@SuppressWarnings("unused")
public class BarCodeUtil {

    public static final int DEFAULT_IMAGE_WIDTH = 300;

    public static final int DEFAULT_IMAGE_HEIGHT = 150;

    public static String convert(Bitmap bitmap) {
        return QRCodeUtil.convert(bitmap);
    }

    public static Bitmap convert(String content) {
        return convert(content, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
    }

    public static Bitmap convert(String content, int width, int height) {
        return QRCodeUtil.convert(content, width, height,
                BarcodeFormat.CODE_128);
    }
}
