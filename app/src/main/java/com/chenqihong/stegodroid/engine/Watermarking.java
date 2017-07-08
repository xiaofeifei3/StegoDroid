package com.chenqihong.stegodroid.engine;

import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by chenqihong on 2017/7/7.
 */

public class Watermarking {

    public static void embedMark(Bitmap bitmap, String signatureMessage){
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
        Signature sig = new Signature(signatureMessage.getBytes());

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        yuv = Utils.getYuv(pixels, width, height);
        luminance = yuv.get(0);
        dwt = new DWT(width, height, sig.getmFileterId(), sig.getmDecompositionLevel(), sig.getmWaveletFilterMethod());
        dwtTree = dwt.forwardDWT(luminance);

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeBytes(WM_MARKER);
            oos.writeInt(sig.decompositionLevel);
            oos.writeDouble(sig.alpha);

            for (int i = 0; i < sig.decompositionLevel; i++) {
                vals = invWmSubBand(s.getHorizontal().getImage(), sig.watermark, sig.watermarkLength, sig.detectionThreshold);
                oos.writeInt((Integer) vals[0]);
                oos.writeDouble((Double) vals[1]);
                oos.writeDouble((Double) vals[2]);

                vals = invWmSubBand(s.getVertical().getImage(), sig.watermark, sig.watermarkLength, sig.detectionThreshold);
                oos.writeInt((Integer) vals[0]);
                oos.writeDouble((Double) vals[1]);
                oos.writeDouble((Double) vals[2]);

                vals = invWmSubBand(s.getDiagonal().getImage(), sig.watermark, sig.watermarkLength, sig.detectionThreshold);
                oos.writeInt((Integer) vals[0]);
                oos.writeDouble((Double) vals[1]);
                oos.writeDouble((Double) vals[2]);

                s = s.getCoarse();
            }

            oos.flush();
            oos.close();
            return baos.toByteArray();
        } catch (IOException ioEx) {
            return null
        }

    }
}
