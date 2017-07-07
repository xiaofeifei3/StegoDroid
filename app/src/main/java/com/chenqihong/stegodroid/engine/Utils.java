package com.chenqihong.stegodroid.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqihong on 2017/7/7.
 */

public class Utils {
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
}
