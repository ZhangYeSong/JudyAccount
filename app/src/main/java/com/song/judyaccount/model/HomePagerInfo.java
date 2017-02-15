package com.song.judyaccount.model;

import android.support.v4.app.Fragment;

/**
 * Created by Judy on 2017/2/15.
 */

public class HomePagerInfo {
    public String title;
    public Fragment fragment;

    public HomePagerInfo(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }
}
