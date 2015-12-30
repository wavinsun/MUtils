package cn.mutils.app.zxing;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import java.util.HashMap;

public class QRCodeUtil {

    public static final int COLOR_BLACK = 0xFF000000;

    public static final int COLOR_WHITE = 0xFFFFFFFF;

    public static final int DEFAULT_IMAGE_SIZE = 300;

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String DEFAULT_IMAGE_FORMAT = "PNG";

    public static final int DEAULT_MARGIN = 0;

    protected static Bitmap convert(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * width + x] = COLOR_BLACK;
                } else {
                    pixels[y * width + x] = COLOR_WHITE;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static Bitmap convert(String content) {
        return convert(content, DEFAULT_IMAGE_SIZE, DEFAULT_IMAGE_SIZE);
    }

    public static Bitmap convert(String content, int width, int height) {
        return convert(content, width, height, BarcodeFormat.QR_CODE);
    }

    protected static Bitmap convert(String content, int width, int height,
                                    BarcodeFormat format) {
        try {
            HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARSET);
            hints.put(EncodeHintType.MARGIN, DEAULT_MARGIN);
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(content, format,
                    width, height, hints);
            return convert(bitMatrix);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convert(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(
                    width, height, pixels);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    rgbLuminanceSource));
            HashMap<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, DEFAULT_CHARSET);
            MultiFormatReader multiFormatReader = new MultiFormatReader();
            Result result = multiFormatReader.decode(binaryBitmap, hints);
            return result.getText();
        } catch (Exception e) {
            return null;
        }
    }

}
