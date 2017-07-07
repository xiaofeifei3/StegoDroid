package com.chenqihong.stegodroid.tasks;

import com.chenqihong.stegodroid.engine.Steganography;

import net.vrallev.android.task.Task;

public class CreateTask extends Task<Boolean> {

    private String mPathToImage;
    private String mPathToFile;
    private String mPathToSave;
    private String mPassword;

    public CreateTask(String pathToImage, String pathToFile, String pathToSave, String password) {
        super();
        mPathToImage = pathToImage;
        mPathToFile = pathToFile;
        mPathToSave = pathToSave;
        mPassword = password;
    }

    @Override
    protected Boolean execute() {
        try {
            Steganography.withInput(mPathToImage)
                    .withPassword(mPassword)
                    .encode(mPathToFile)
                    .intoFile(mPathToSave);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}