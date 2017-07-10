package com.chenqihong.stegodroid.engine;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqihong on 2017/7/7.
 */

public class Utils {
    private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
            (byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f' };
    public static List<int[][]> getYuv(int[] pixels, int width, int height){
        List<int[][]> yuv = new ArrayList<int[][]>();
        int y, u, v;
        int[][] yMap = new int[height][width];
        int[][] uMap = new int[height][width];
        int[][] vMap = new int[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                //屏蔽透明度值
                int rgb = pixels[i * width + j] & 0x00FFFFFF;
                // 像素的颜色顺序为bgr，移位运算。
                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;
                // 套用公式
                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
                // 调整
                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);
                // 赋值
                yMap[i][j]=  y;
                uMap[i][j] = u;
                vMap[i][j] =  v;
            }
        }
        yuv.add(yMap);
        yuv.add(uMap);
        yuv.add(vMap);
        return yuv;
    }

    public static long passwordHash(String password) {
        final long DEFAULT_HASH = 98234782; // Default to a random (but constant) seed
        byte[] byteHash = null;
        String hexString = null;

        if (password == null || password.equals("")) {
            return DEFAULT_HASH;
        }

        try {
            byteHash = MessageDigest.getInstance("MD5").digest(password.getBytes());
            hexString = getHexString(byteHash);

            // Hex string will be 32 bytes long whereas parsing to long can handle only 16 bytes, so trim it
            hexString = hexString.substring(0, 15);
            return Long.parseLong(hexString, 16);
        } catch (NoSuchAlgorithmException nsaEx) {
            return DEFAULT_HASH;
        }
    }

    public static String getHexString(byte[] raw) {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;
        int byteVal;

        for (int i = 0; i < raw.length; i++) {
            byteVal = raw[i] & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[byteVal >>> 4];
            hex[index++] = HEX_CHAR_TABLE[byteVal & 0xF];
        }
        return new String(hex);
    }
}
