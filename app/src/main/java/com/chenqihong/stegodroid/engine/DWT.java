package com.chenqihong.stegodroid.engine;

import android.graphics.Bitmap;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by chenqihong on 2017/7/7.
 */

public class DWT {

    private static Map<Integer, FilterGH> mFilterGHMap = null;
    private String mFilterFile = "assets/filters.xml";
    private FilterGH[] mFilters = null;
    private int mCols = 0;
    private int mRows = 0;
    private int mLevel = 0;
    private int mMethod = 0;

    public DWT(int cols, int rows, int filterId, int level, int method, InputStream is){
        if(mFilterGHMap == null){
            mFilterGHMap = FilterXMLReader.parse(is);
        }

        mFilters = new FilterGH[level + 1];
        for(int i = 0; i <= level; i++){
            mFilters[i] = mFilterGHMap.get(new Integer(filterId));
        }

        mLevel = level;
        mMethod = method;
        mCols = cols;
        mRows = rows;
    }

    public ImageTree forwardDWT(int[][] pixels){
        Bitmap bitmap = null;
        ImageTree tree = null;

        bitmap = Bitmap.createBitmap(mCols, mRows, Bitmap.Config.ARGB_8888);

        for(int i = 0; i < mRows; i++){
            for(int j = 0; j < mCols; j++){
                DWTUtils.setPixel(bitmap, i, j, pixels[i][j]);
            }
        }

        tree = DWTUtils.waveletTransform(bitmap, mLevel, mFilters, mMethod);
        return tree;
    }
}
