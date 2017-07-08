package com.chenqihong.stegodroid.engine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by chenqihong on 2017/7/7.
 */

public class Signature {
    private byte[] mSig = "MUCFC".getBytes();
    private int mWatermarkLength = 1000;
    private int mWaveletFilterMethod = 0;
    private int mFileterId = 0;
    private int mDecompositionLevel = 0;
    private double mAlpha = 0;
    private double mCastingThreshold = 0;
    private double mDetectionThreshold = 0;
    private double[] mWatermark = null;

    public Signature(byte[] sigData){
        ObjectInputStream ois = null;
        byte[] inputSig = new byte[mSig.length];
        try{
            ois = new ObjectInputStream(new ByteArrayInputStream(sigData));
            ois.read(inputSig, 0, this.mSig.length);
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
