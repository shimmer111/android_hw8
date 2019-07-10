package com.bytedance.camera.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

public class RecordVideoActivity extends AppCompatActivity {
    private static final String TAG = "RecordVideoActivity";
    private VideoView videoView;
    private static final int REQUEST_VIDEO_CAPTURE = 1;

    private static final int REQUEST_EXTERNAL_CAMERA = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        videoView = findViewById(R.id.img);
        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(RecordVideoActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(RecordVideoActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_VIDEO_CAPTURE);
                //todo 在这里申请相机、存储的权限
            } else {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(takeVideoIntent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(takeVideoIntent,REQUEST_EXTERNAL_CAMERA);
                }
                //todo 打开相机拍摄
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            Log.d(TAG, "onActivityResult: videoUri"+videoUri.toString());
            videoView.setVideoURI(videoUri);
            videoView.start();
            //todo 播放刚才录制的视频
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_VIDEO_CAPTURE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(takeVideoIntent.resolveActivity(getPackageManager())!= null){
                        startActivityForResult(takeVideoIntent,REQUEST_EXTERNAL_CAMERA);
                    }
                }
                else{
                    Toast.makeText(RecordVideoActivity.this,"请手动打开相机权限",Toast.LENGTH_SHORT).show();
                }
                //todo 判断权限是否已经授予
                break;
            }
            default:
                break;
        }
    }
}
