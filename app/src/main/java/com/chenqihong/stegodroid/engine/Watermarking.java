package com.chenqihong.stegodroid.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

/**
 * Created by chenqihong on 2017/7/7.
 */

public class Watermarking {
    private static final String WM_MARKER = "MUSTEGO";
    public static EncodedObject embedMark(Bitmap bitmap, InputStream filterIs, String signatureMessage){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int size = width * height;
        int pixels[] = new int[size];
        List<int[][]> yuv = null;
        int[][] luminance = null;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        Object[] vals = null;
        DWT dwt = null;
        ImageTree dwtTree = null;
        Signature sig = null;
        sig = new Signature(generateSignature(signatureMessage));

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        yuv = Utils.getYuv(pixels, width, height);
        luminance = yuv.get(0);
        dwt = new DWT(width, height, sig.getmFileterId(), sig.getmDecompositionLevel(), sig.getmWaveletFilterMethod(), filterIs);
        dwtTree = dwt.forwardDWT(luminance);
        ImageTree s = dwtTree;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeBytes(WM_MARKER);
            oos.writeInt(sig.getmDecompositionLevel());
            oos.writeDouble(sig.getmAlpha());

            for (int i = 0; i < sig.getmDecompositionLevel(); i++) {
                vals = invWmSubBand(s.getHorizontal().getImage(), sig.getmWatermark(), sig.getmWatermarkLength(), sig.getmDetectionThreshold());
                oos.writeInt((Integer) vals[0]);
                oos.writeDouble((Double) vals[1]);
                oos.writeDouble((Double) vals[2]);

                vals = invWmSubBand(s.getVertical().getImage(), sig.getmWatermark(), sig.getmWatermarkLength(), sig.getmDetectionThreshold());
                oos.writeInt((Integer) vals[0]);
                oos.writeDouble((Double) vals[1]);
                oos.writeDouble((Double) vals[2]);

                vals = invWmSubBand(s.getDiagonal().getImage(), sig.getmWatermark(), sig.getmWatermarkLength(), sig.getmDetectionThreshold());
                oos.writeInt((Integer) vals[0]);
                oos.writeDouble((Double) vals[1]);
                oos.writeDouble((Double) vals[2]);

                s = s.getCoarse();
            }

            oos.flush();
            oos.close();
            byte[] data = baos.toByteArray();
            EncodedObject object = new EncodedObject(BitmapFactory.decodeByteArray(data, 0, data.length));
            return object;
        } catch (IOException ioEx) {
            return null;
        }
    }

    public static Bitmap withInput(@NonNull String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    private static Object[] invWmSubBand(Bitmap img, double[] wm, int n, double threshold) {
        int m = 0;
        double z = 0.0;
        double v = 0.0;
        for(int i = 0; i < img.getWidth(); i++){
            for(int j = 0; j < img.getHeight(); i++){
                z += (img.getPixel(i, j) * wm[i % n]);
                v += Math.abs(img.getPixel(i, j) );
                m++;
            }
        }

        return new Object[] { m, z, v };
    }

    public static byte[] generateSignature(String message)  {
        Random rand = null;
        Signature sig = null;

        rand = new Random(Utils.passwordHash(message));
        sig = new Signature(rand);

        return sig.getSigData();
    }


}
