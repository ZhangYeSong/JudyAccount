package com.song.judyaccount;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by Judy on 2017/2/15.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this,"72AyqVcig8GHil1VmhJlGk4e-gzGzoHsz","AntvQCqjQhzIfLSvfgHIxV8o");
    }
}
