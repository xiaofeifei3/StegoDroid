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
        Signature sig = new Signature(signatureMessage.getBytes());

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        yuv = Utils.getYuv(pixels, width, height);
        luminance = yuv.get(0);


    }
}
