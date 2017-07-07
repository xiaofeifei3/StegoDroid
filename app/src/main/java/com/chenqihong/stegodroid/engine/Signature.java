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
    byte[] mSig = "MUCFC".getBytes();
    int mWatermarkLength = 1000;
    int mWaveletFilterMethod = 0;
    int mFileterId = 0;
    int mDecompositionLevel = 0;
    double mAlpha = 0;
    double mCastingThreshold = 0;
    double mDetectionThreshold = 0;
    double[] mWatermark = null;

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
}
