package com.chenqihong.stegodroid.tasks;

import com.chenqihong.stegodroid.engine.Steganography;

import net.vrallev.android.task.Task;

public class FindTask extends Task<Boolean> {

    private String mPathToImage;
    private String mPathToSave;
    private String mPassword;

    public FindTask(String pathToImage, String pathToSave, String password) {
        super();
        mPathToImage = pathToImage;
        mPathToSave = pathToSave;
        mPassword = password;
    }

    @Override
    protected Boolean execute() {
        try {
            Steganography.withInput(mPathToImage)
                    .withPassword(mPassword)
                    .decode()
                    .intoFile(mPathToSave);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}