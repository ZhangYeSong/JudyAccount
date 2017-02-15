package com.song.judyaccount.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.song.judyaccount.model.HomePagerInfo;

import java.util.List;

/**
 * Created by Judy on 2017/2/15.
 */

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private List<HomePagerInfo> mData;
    public HomePagerAdapter(FragmentManager fm, List<HomePagerInfo> data) {
        super(fm);
        this.mData = data;
    }

    @Override
    public Fragment getItem(int position) {
        return mData.get(position).fragment;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mData.get(position).title;
    }

}
