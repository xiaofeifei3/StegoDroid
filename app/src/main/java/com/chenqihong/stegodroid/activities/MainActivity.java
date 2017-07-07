package com.chenqihong.stegodroid.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenqihong.stegodroid.R;
import com.chenqihong.stegodroid.engine.Steganography;
import com.chenqihong.stegodroid.tasks.CreateMessageTask;
import com.chenqihong.stegodroid.tasks.CreateTask;
import com.chenqihong.stegodroid.tasks.FindTask;

import net.vrallev.android.task.Task;
import net.vrallev.android.task.TaskExecutor;

import java.io.File;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    public static int REQUEST_PICK_IMAGE = 200;
    private EditText mInfoEdit;
    private Button mHideInfoButton;
    private Button mShowInfoButton;
    private ImageView mProcessedImage;
    private TextView mExtractedInfoText;
    private String mSelectedImagePath;
    private Bitmap mStegoBitmap;
    private Handler mCreateHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                Bitmap bitmap = msg.getData().getParcelable("image");
                mExtractedInfoText.setVisibility(View.GONE);
                mProcessedImage.setVisibility(View.VISIBLE);
                mProcessedImage.setImageBitmap(bitmap);
                mStegoBitmap = bitmap;
            }
            return false;
        }
    });

    private Handler mFindHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                String message = msg.getData().getString("message");
                mExtractedInfoText.setText("从图片提取出来的内容是：" + message);
            }

            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        mInfoEdit = (EditText)findViewById(R.id.editText);
        mHideInfoButton = (Button)findViewById(R.id.pickPhoto);
        mShowInfoButton = (Button)findViewById(R.id.pickInfo);
        mProcessedImage = (ImageView)findViewById(R.id.showImage);
        mExtractedInfoText = (TextView)findViewById(R.id.showInfo);
        mHideInfoButton.setOnClickListener(this);
        mShowInfoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mHideInfoButton == v){
            if(TextUtils.isEmpty(mInfoEdit.getText())){
                Toast.makeText(this, "隐藏信息不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            pickPhoto();

        }else if(mShowInfoButton == v){
            mExtractedInfoText.setVisibility(View.VISIBLE);
            mProcessedImage.setVisibility(View.GONE);
            extractData();
        }
    }

    public void pickPhoto(){
        MultiImageSelector.create().single().start(this, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK){
            List<String> path = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            mSelectedImagePath = path.get(0);
            if (null == mSelectedImagePath){
                Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Message message = mCreateHandler.obtainMessage();
                        message.what = 1;
                        Bitmap image = Steganography.withInput(mSelectedImagePath)
                                .withPassword("Chenqihong3")
                                .encodeMessage(mInfoEdit.getText().toString()).getBitmap();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("image", image);
                        message.setData(bundle);
                        message.sendToTarget();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void hideData(){
        Task task = new CreateMessageTask(mSelectedImagePath, mInfoEdit.getText().toString(),
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/encode.jpg", "Chenqihong3");
        TaskExecutor.getInstance().execute(task, this);

    }

    public void extractData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String message = Steganography.withInput(mStegoBitmap)
                            .withPassword("Chenqihong3")
                            .decode().getMessage();
                    mExtractedInfoText.setText("从图片提取出来的内容是：" + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
