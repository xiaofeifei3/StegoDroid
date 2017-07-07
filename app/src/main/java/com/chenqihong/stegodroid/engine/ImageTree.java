package com.chenqihong.stegodroid.engine;

import android.graphics.Bitmap;

/**
 * Created by chenqihong on 2017/7/7.
 */

public class ImageTree {
    private double entropy = 0.0;
    private ImageTree coarse = null;
    private ImageTree horizontal = null;
    private ImageTree vertical = null;
    private ImageTree diagonal = null;
    private ImageTree doubleTree = null;
    private Bitmap image = null;
    private int level = 0;
    private int flag = 0;

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public ImageTree getCoarse() {
        return coarse;
    }

    public void setCoarse(ImageTree coarse) {
        this.coarse = coarse;
    }

    public ImageTree getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(ImageTree horizontal) {
        this.horizontal = horizontal;
    }

    public ImageTree getVertical() {
        return vertical;
    }

    public void setVertical(ImageTree vertical) {
        this.vertical = vertical;
    }

    public ImageTree getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(ImageTree diagonal) {
        this.diagonal = diagonal;
    }

    public ImageTree getDoubleTree() {
        return doubleTree;
    }

    public void setDoubleTree(ImageTree doubleTree) {
        this.doubleTree = doubleTree;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
