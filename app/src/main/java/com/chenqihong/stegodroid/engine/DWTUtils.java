package com.chenqihong.stegodroid.engine;

import android.graphics.Bitmap;

/**
 * Created by chenqihong on 2017/7/7.
 */

public class DWTUtils {
    public static void setPixel(Bitmap image, int x, int y, double val){
        if (!(image == null || x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight())) {
            image.setPixel(x, y * image.getWidth(), (int)val);
        }
    }

    public static ImageTree waveletTransform(Bitmap origImg, int level, FilterGH[] filterGHList, int method){
        int width = 0;
        int height = 0;
        int min = 0;
        int maxLevel = 0;
        Bitmap coarseImage = null;
        Bitmap horizontalImage = null;
        Bitmap verticalImage = null;
        Bitmap diagonalImage = null;
        Bitmap tempImage = null;
        ImageTree returnTree = null;
        ImageTree tempTree = null;

        width = origImg.getWidth();
        height = origImg.getHeight();

        tempImage = origImg.copy(Bitmap.Config.ARGB_8888, true);

        returnTree = new ImageTree();
        tempTree = returnTree;
        returnTree.setLevel(0);

        min = origImg.getWidth();
        if(origImg.getHeight() < min){
            min = origImg.getHeight();
        }

        maxLevel = ((int)(Math.log(min) / Math.log(2))) - 2;
        if(maxLevel < level){
            level = maxLevel;
        }

        if(level < 1){
            returnTree.setImage(tempImage);
            return returnTree;
        }

        // Decomposition
        for (int i = 0; i < level; i++) {
            width = (width + 1) / 2;
            height = (height + 1) / 2;

            coarseImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            horizontalImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            verticalImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            diagonalImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            decomposition(tempImage, coarseImage, horizontalImage, verticalImage, diagonalImage, filterGHList[i].getG(), filterGHList[i].getH(), method);

            tempTree.setCoarse(new ImageTree());
            tempTree.setHorizontal(new ImageTree());
            tempTree.setVertical(new ImageTree());
            tempTree.setDiagonal(new ImageTree());

            tempTree.getCoarse().setLevel(i + 1);
            tempTree.getHorizontal().setLevel(i + 1);
            tempTree.getVertical().setLevel(i + 1);
            tempTree.getDiagonal().setLevel(i + 1);

            tempTree.getHorizontal().setImage(horizontalImage);
            tempTree.getVertical().setImage(verticalImage);
            tempTree.getDiagonal().setImage(diagonalImage);
            tempImage = null;

            if (i != (level - 1)) {
                tempImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                tempImage = coarseImage.copy(Bitmap.Config.ARGB_8888, true);
                coarseImage = null;
            }

            tempTree = tempTree.getCoarse();
        }

        tempTree.setImage(coarseImage);
        return returnTree;
    }

    public static void decomposition(Bitmap inputImg, Bitmap coarseImg, Bitmap horizontalImg, Bitmap verticalImg, Bitmap diagonalImg, Filter filterG,
                                     Filter filterH, int method) {
        Bitmap tempImg = null;

        // Coarse
        tempImg = Bitmap.createBitmap(coarseImg.getWidth(), coarseImg.getHeight(), Bitmap.Config.ARGB_8888);
        convoluteLines(tempImg, inputImg, filterH, method);
        convoluteRows(coarseImg, tempImg, filterH, method);

        // Horizontal
        convoluteRows(horizontalImg, tempImg, filterG, method);

        // Vertical
        tempImg = Bitmap.createBitmap(coarseImg.getWidth(), coarseImg.getHeight(), Bitmap.Config.ARGB_8888);
        convoluteLines(tempImg, inputImg, filterG, method);
        convoluteRows(verticalImg, tempImg, filterH, method);

        // Diagonal
        convoluteRows(diagonalImg, tempImg, filterG, method);
    }

    public static void convoluteLines(Bitmap outputImg, Bitmap inputImg, Filter filter, int method) {
        for (int i = 0; i < inputImg.getHeight(); i++) {
            switch (method) {
                case Filter.METHOD_CUTOFF:
                    filterCutOff(inputImg, inputImg.getWidth() * i, inputImg.getWidth(), 1, outputImg, outputImg.getWidth() * i, outputImg.getWidth(),
                            1, filter);
                    break;

                case Filter.METHOD_INVCUTOFF:
                    filterInvCutOff(inputImg, inputImg.getWidth() * i, inputImg.getWidth(), 1, outputImg, outputImg.getWidth() * i,
                            outputImg.getWidth(), 1, filter);
                    break;

                case Filter.METHOD_PERIODICAL:
                    filterPeriodical(inputImg, inputImg.getWidth() * i, inputImg.getWidth(), 1, outputImg, outputImg.getWidth() * i,
                            outputImg.getWidth(), 1, filter);
                    break;

                case Filter.METHOD_INVPERIODICAL:
                    filterInvPeriodical(inputImg, inputImg.getWidth() * i, inputImg.getWidth(), 1, outputImg, outputImg.getWidth() * i,
                            outputImg.getWidth(), 1, filter);
                    break;

                case Filter.METHOD_MIRROR:
                    filterMirror(inputImg, inputImg.getWidth() * i, inputImg.getWidth(), 1, outputImg, outputImg.getWidth() * i, outputImg.getWidth(),
                            1, filter);
                    break;

                case Filter.METHOD_INVMIRROR:
                    filterInvMirror(inputImg, inputImg.getWidth() * i, inputImg.getWidth(), 1, outputImg, outputImg.getWidth() * i,
                            outputImg.getWidth(), 1, filter);
                    break;
            }
        }
    }

    public static void convoluteRows(Bitmap outputImg, Bitmap inputImg, Filter filter, int method) {
        for (int i = 0; i < inputImg.getWidth(); i++) {
            switch (method) {
                case Filter.METHOD_CUTOFF:
                    filterCutOff(inputImg, i, inputImg.getHeight(), inputImg.getWidth(), outputImg, i, outputImg.getHeight(), outputImg.getWidth(),
                            filter);
                    break;

                case Filter.METHOD_INVCUTOFF:
                    filterInvCutOff(inputImg, i, inputImg.getHeight(), inputImg.getWidth(), outputImg, i, outputImg.getHeight(), outputImg.getWidth(),
                            filter);
                    break;

                case Filter.METHOD_PERIODICAL:
                    filterPeriodical(inputImg, i, inputImg.getHeight(), inputImg.getWidth(), outputImg, i, outputImg.getHeight(),
                            outputImg.getWidth(), filter);
                    break;

                case Filter.METHOD_INVPERIODICAL:
                    filterInvPeriodical(inputImg, i, inputImg.getHeight(), inputImg.getWidth(), outputImg, i, outputImg.getHeight(),
                            outputImg.getWidth(), filter);
                    break;

                case Filter.METHOD_MIRROR:
                    filterMirror(inputImg, i, inputImg.getHeight(), inputImg.getWidth(), outputImg, i, outputImg.getHeight(), outputImg.getWidth(),
                            filter);
                    break;

                case Filter.METHOD_INVMIRROR:
                    filterInvMirror(inputImg, i, inputImg.getHeight(), inputImg.getWidth(), outputImg, i, outputImg.getHeight(), outputImg.getWidth(),
                            filter);
                    break;
            }
        }
    }

    public static void filterCutOff(Bitmap inputImg, int inStart, int inLen, int inStep, Bitmap outputImg, int outStart, int outLen, int outStep,
                                    Filter filter) {
        int fStart = 0;
        int fEnd = 0;
        int pixel = 0;

        for (int i = 0; i < outLen; i++) {
            fStart = max((2 * i) - (inLen - 1), filter.getStart());
            fEnd = min((2 * i), filter.getEnd());
            pixel = outputImg.getPixel(outStart, i * outStep);
            for (int j = fStart; j <= fEnd; j++) {
                pixel += filter.getData()[j - filter.getStart()]
                        * inputImg.getPixel(inStart, ((2 * i) - j) * inStep);
            }

            outputImg.setPixel(outStart, i * outStep, pixel);
        }
    }

    public static void filterInvCutOff(Bitmap inputImg, int inStart, int inLen, int inStep, Bitmap outputImg, int outStart, int outLen, int outStep,
                                       Filter filter) {
        int fStart = 0;
        int fEnd = 0;
        int pixel = 0;

        for (int i = 0; i < outLen; i++) {
            fStart = max(ceilingHalf(filter.getStart() + i), 0);
            fEnd = min(floorHalf(filter.getEnd() + i), inLen - 1);
            pixel = outputImg.getPixel(outStart, i * outStep);
            for (int j = fStart; j <= fEnd; j++) {
                pixel += filter.getData()[(2 * j) - i - filter.getStart()]
                        * inputImg.getPixel(inStart, ((2 * i) - j) * inStep);
            }

            outputImg.setPixel(outStart, i * outStep, pixel);
        }
    }

    public static void filterPeriodical(Bitmap inputImg, int inStart, int inLen, int inStep, Bitmap outputImg, int outStart, int outLen, int outStep,
                                        Filter filter) {
        int fStart = 0;
        int fEnd = 0;
        int iStart = 0;
        int pixel = 0;

        for (int i = 0; i < outLen; i++) {
            fStart = filter.getStart();
            fEnd = filter.getEnd();
            iStart = mod(((2 * i) - fStart), inLen);
            pixel = outputImg.getPixel(outStart, i * outStep);
            for (int j = fStart; j <= fEnd; j++) {
                pixel += filter.getData()[j - fStart]
                        * inputImg.getPixel(inStart,iStart * inStep);
                iStart--;
                if (iStart < 0) {
                    iStart += inLen;
                }
            }

            outputImg.setPixel(outStart, i * outStep, pixel);
        }
    }

    public static void filterInvPeriodical(Bitmap inputImg, int inStart, int inLen, int inStep, Bitmap outputImg, int outStart, int outLen, int outStep,
                                           Filter filter) {
        int fStart = 0;
        int fEnd = 0;
        int iStart = 0;
        int pixel = 0;

        for (int i = 0; i < outLen; i++) {
            fStart = ceilingHalf(filter.getStart() + i);
            fEnd = floorHalf(filter.getEnd() + i);
            iStart = mod(fStart, inLen);
            pixel = outputImg.getPixel(outStart, i * outStep);
            for (int j = fStart; j <= fEnd; j++) {
                pixel += filter.getData()[(2 * j) - i - filter.getStart()]
                        * inputImg.getPixel(inStart, iStart * inStep);
                iStart++;
                if (iStart >= inLen) {
                    iStart -= inLen;
                }
            }
            outputImg.setPixel(outStart, i * outStep, pixel);
        }
    }

    public static void filterMirror(Bitmap inputImg, int inStart, int inLen, int inStep, Bitmap outputImg, int outStart, int outLen, int outStep,
                                    Filter filter) {
        int fStart = 0;
        int fEnd = 0;
        int inPos = 0;
        int pixel = 0;
        for (int i = 0; i < outLen; i++) {
            fStart = filter.getStart();
            fEnd = filter.getEnd();
            pixel = outputImg.getPixel(outStart, i * outStep);

            for (int j = fStart; j <= fEnd; j++) {
                inPos = ((2 * i) - j);
                if (inPos < 0) {
                    inPos = -inPos;
                    if (inPos >= inLen) {
                        continue;
                    }
                }
                if (inPos >= inLen) {
                    inPos = 2 * inLen - 2 - inPos;
                    if (inPos < 0) {
                        continue;
                    }
                }
                pixel += filter.getData()[j - fStart]
                        * inputImg.getPixel(inStart, inPos * inStep);
            }

            outputImg.setPixel(outStart, i * outStep, pixel);
        }
    }

    public static void filterInvMirror(Bitmap inputImg, int inStart, int inLen, int inStep, Bitmap outputImg, int outStart, int outLen, int outStep,
                                       Filter filter) {
        int fStart = 0;
        int fEnd = 0;
        int inPos = 0;
        int pixel = 0;

        for (int i = 0; i < outLen; i++) {
            fStart = ceilingHalf(filter.getStart() + i);
            fEnd = floorHalf(filter.getEnd() + i);

            for (int j = fStart; j <= fEnd; j++) {
                inPos = j;
                if (inPos < 0) {
                    if (filter.isHiPass()) {
                        inPos = -inPos - 1;
                    } else {
                        inPos = -inPos;
                    }
                    if (inPos >= inLen) {
                        continue;
                    }
                }
                if (inPos >= inLen) {
                    if (filter.isHiPass()) {
                        inPos = 2 * inLen - 2 - inPos;
                    } else {
                        inPos = 2 * inLen - 1 - inPos;
                    }
                    if (inPos < 0) {
                        continue;
                    }
                }
                pixel += filter.getData()[2 * j - i - filter.getStart()]
                        * inputImg.getPixel(inStart, inPos * inStep);
            }

            outputImg.setPixel(outStart, i * outStep, pixel);
        }
    }

    public static Bitmap inverseTransform(ImageTree tree, FilterGH[] filterGHList, int method) {
        int width = 0;
        int height = 0;
        Bitmap retImg = null;
        Bitmap coarseImg = null;
        Bitmap verticalImg = null;
        Bitmap horizontalImg = null;
        Bitmap diagonalImg = null;

        if (tree.getImage() == null) {
            coarseImg = inverseTransform(tree.getCoarse(), filterGHList, method);
            horizontalImg = inverseTransform(tree.getHorizontal(), filterGHList, method);
            verticalImg = inverseTransform(tree.getVertical(), filterGHList, method);
            diagonalImg = inverseTransform(tree.getDiagonal(), filterGHList, method);

            width = coarseImg.getWidth() + horizontalImg.getWidth();
            height = coarseImg.getHeight() + verticalImg.getHeight();

            retImg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            if (tree.getFlag() == 0) // If flag is set it is a doubletree tiling
            {
                invDecomposition(retImg, coarseImg, horizontalImg, verticalImg, diagonalImg, filterGHList[tree.getLevel()], method);
            } else {
                copyIntoImage(retImg, coarseImg, 0, 0);
                copyIntoImage(retImg, horizontalImg, coarseImg.getWidth(), 0);
                copyIntoImage(retImg, verticalImg, 0, coarseImg.getHeight());
                copyIntoImage(retImg, diagonalImg, coarseImg.getWidth(), coarseImg.getHeight());
            }

            return retImg;
        }
        return tree.getImage();
    }

    public static int max(int x, int y) {
        return (x > y) ? x : y;
    }

    public static int min(int x, int y) {
        return (x < y) ? x : y;
    }

    public static int ceilingHalf(int num) {
        if ((num & 1) == 1) {
            return (num + 1) / 2;
        } else {
            return num / 2;
        }
    }

    public static int floorHalf(int num) {
        if ((num & 1) == 1) {
            return (num - 1) / 2;
        } else {
            return num / 2;
        }
    }

    public static int mod(int num, int div) {
        if (num < 0) {
            return div - (-num % div);
        } else {
            return num % div;
        }
    }

}
