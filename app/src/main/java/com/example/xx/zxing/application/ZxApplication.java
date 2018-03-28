package com.example.xx.zxing.application;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * 日期：2018/3/26
 * 描述：
 *
 * @author XX
 */

public class ZxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ZXingLibrary.initDisplayOpinion(this);
    }
}
