package com.bwie.demo;

import android.Manifest;

import android.app.Activity;


import android.content.ContentResolver;
import android.content.Intent;


import android.content.pm.PackageManager;


import android.graphics.Bitmap;

import android.graphics.BitmapFactory;

import android.net.Uri;


import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;


import android.view.View;


import android.widget.Button;


import android.widget.ImageView;
import android.widget.Toast;


import com.uuzuche.lib_zxing.activity.CaptureActivity;

import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 扫描，生成二维码
 */
public
class
MainActivity extends Activity {


    private Button button, button1, button2, button3;
    private int REQUEST_CODE = 1;
    private ImageView imageView;
    private String string1 = "无图";
    private String string2 = "https://github.com/yipianfengye/android-zxingLibrary";


    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btn);
        button1 = (Button) findViewById(R.id.btn2);
        button2 = (Button) findViewById(R.id.btn3);
        button3 = (Button) findViewById(R.id.btn4);
        imageView = (ImageView) findViewById(R.id.image1);
        /**
         * 打开默认二维码扫描界面
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 判断是否获取到相机权限
                 */
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //是否请求过该权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {

                    } else {//没有则请求获取权限
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                    }
                } else {        //如果已经获取到了权限则直接进行下一步操作

                    //打开闪光灯
                    //CodeUtils.isLightEnable(true);
                    //关闭闪光灯
                    //CodeUtils.isLightEnable(false);
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
        //生成无图二维码
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                bitmap = CodeUtils.createImage(string1, 400, 400, null);
                imageView.setImageBitmap(bitmap);
            }
        });
        //生成有图二维码
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                bitmap = CodeUtils.createImage(string2, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.zzz));
                imageView.setImageBitmap(bitmap);
            }
        });
        //读取图库中的二维码图片
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 判断是否获取到相册权限
                 */
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //是否请求过该权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    } else {//没有则请求获取权限
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
                    }
                } else {        //如果已经获取到了权限则直接进行下一步操作

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, 3);
                }
            }
        });
    }

    //接收权限回传的值
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                //判断是否拿到相机权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Toast.makeText(MainActivity.this, "您拒绝了获取到相机权限,请手动获取或重装软件", Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                //判断是否拿到相册权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, 3);
                } else {
                    Toast.makeText(MainActivity.this, "您拒绝了获取到读取相册权限,请手动获取或重装软件", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //接收二维码扫描结果
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        //处理从图库读取的二维码图片扫描结果
        if (requestCode == 3) {
            if (data != null) {
                try {
                    String path = ImageUtil.getImageAbsolutePath(MainActivity.this, data.getData());
                    CodeUtils.analyzeBitmap(path, new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
