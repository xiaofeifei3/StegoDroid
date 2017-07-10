package com.chenqihong.stegodroid.engine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

/**
 * Created by chenqihong on 2017/7/7.
 */

public class Signature {
    private byte[] mSig = "MU".getBytes();
    private int mWatermarkLength = 1000;
    private int mWaveletFilterMethod = 2;
    private int mFileterId = 1;
    private int mDecompositionLevel = 3;
    private double mAlpha = 0.2;
    private double mCastingThreshold = 40.0;
    private double mDetectionThreshold = 50.0;
    private double[] mWatermark = null;
    public Signature(Random rand) {
        double x, x1, x2;
        mWatermark = new double[mWatermarkLength];

        for (int i = 0; i < mWatermarkLength; i += 2) {
            do {
                x1 = 2.0 * rand.nextDouble() - 1.0;
                x2 = 2.0 * rand.nextDouble() - 1.0;
                x = x1 * x1 + x2 * x2;
            } while (x >= 1.0);
            x1 *= Math.sqrt((-2.0) * Math.log(x) / x);
            x2 *= Math.sqrt((-2.0) * Math.log(x) / x);

            this.mWatermark[i] = x1;
            this.mWatermark[i + 1] = x2;
        }
    }

    public Signature(byte[] sigData){
        ObjectInputStream ois = null;
        byte[] inputSig = new byte[mSig.length];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            ois = new ObjectInputStream(new ByteArrayInputStream(sigData));
            ois.read(inputSig, 0, mSig.length);
            mWatermarkLength = ois.readInt();
            mWaveletFilterMethod = ois.readInt();
            mFileterId = ois.readInt();
            mDecompositionLevel = ois.readInt();
            mAlpha = ois.readDouble();
            mCastingThreshold = ois.readDouble();
            mDetectionThreshold = ois.readDouble();
            mWatermark = new double[mWatermarkLength];
            for(int i = 0; i < mWatermarkLength; i ++){
                mWatermark[i] = ois.readDouble();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getSigData(){
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try{
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.write(mSig);
            oos.writeInt(mWatermarkLength);
            oos.writeInt(mWaveletFilterMethod);
            oos.writeInt(mFileterId);
            oos.writeInt(mDecompositionLevel);
            oos.writeDouble(mAlpha);
            oos.writeDouble(mCastingThreshold);
            oos.writeDouble(mDetectionThreshold);

            for(int i = 0; i < mWatermarkLength; i++){
                oos.writeDouble(mWatermark[i]);
            }

            oos.flush();
            oos.close();
            return baos.toByteArray();
        } catch (IOException e) {

        }

        return null;
    }

    public byte[] getStreamBytes(InputStream is) {
        final int BUF_SIZE = 512;
        ByteArrayOutputStream bos = null;
        int bytesRead = 0;
        byte[] data = null;

        try {
            data = new byte[BUF_SIZE];
            bos = new ByteArrayOutputStream();

            while ((bytesRead = is.read(data, 0, BUF_SIZE)) >= 0) {
                bos.write(data, 0, bytesRead);
            }

            is.close();
            bos.close();

            return bos.toByteArray();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            return null;
        }
    }

    public byte[] getmSig() {
        return mSig;
    }

    public void setmSig(byte[] mSig) {
        this.mSig = mSig;
    }

    public int getmWatermarkLength() {
        return mWatermarkLength;
    }

    public void setmWatermarkLength(int mWatermarkLength) {
        this.mWatermarkLength = mWatermarkLength;
    }

    public int getmWaveletFilterMethod() {
        return mWaveletFilterMethod;
    }

    public void setmWaveletFilterMethod(int mWaveletFilterMethod) {
        this.mWaveletFilterMethod = mWaveletFilterMethod;
    }

    public int getmFileterId() {
        return mFileterId;
    }

    public void setmFileterId(int mFileterId) {
        this.mFileterId = mFileterId;
    }

    public int getmDecompositionLevel() {
        return mDecompositionLevel;
    }

    public void setmDecompositionLevel(int mDecompositionLevel) {
        this.mDecompositionLevel = mDecompositionLevel;
    }

    public double getmAlpha() {
        return mAlpha;
    }

    public void setmAlpha(double mAlpha) {
        this.mAlpha = mAlpha;
    }

    public double getmCastingThreshold() {
        return mCastingThreshold;
    }

    public void setmCastingThreshold(double mCastingThreshold) {
        this.mCastingThreshold = mCastingThreshold;
    }

    public double getmDetectionThreshold() {
        return mDetectionThreshold;
    }

    public void setmDetectionThreshold(double mDetectionThreshold) {
        this.mDetectionThreshold = mDetectionThreshold;
    }

    public double[] getmWatermark() {
        return mWatermark;
    }

    public void setmWatermark(double[] mWatermark) {
        this.mWatermark = mWatermark;
    }
}
