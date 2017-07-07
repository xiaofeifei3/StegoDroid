package com.chenqihong.stegodroid.tasks;

import com.chenqihong.stegodroid.engine.Steganography;

import net.vrallev.android.task.Task;

/**
 * Created by chenqihong on 2017/7/4.
 */

public class CreateMessageTask extends Task<Boolean>{
    private String mPathToImage;
    private String mMassage;
    private String mPathToSave;
    private String mPassword;

    public CreateMessageTask(String pathToImage, String message, String pathToSave, String password) {
        super();
        mPathToImage = pathToImage;
        mMassage = message;
        mPathToSave = pathToSave;
        mPassword = password;
    }

    @Override
    protected Boolean execute() {
        try {
            Steganography.withInput(mPathToImage)
                    .withPassword(mPassword)
                    .encodeMessage(mMassage)
                    .intoFile(mPathToSave);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
