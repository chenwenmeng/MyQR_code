package com.bwie.demo;

import android.app.Application;



import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * 类描述:
 * 作者：陈文梦
 * 时间:2017/2/17 13:43
 * 邮箱:18310832074@163.com
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ZXingLibrary.initDisplayOpinion(this);
    }
}
