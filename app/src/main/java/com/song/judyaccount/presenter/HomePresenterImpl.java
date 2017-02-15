package com.song.judyaccount.presenter;

import com.song.judyaccount.view.activity.HomeView;

/**
 * Created by Judy on 2017/2/15.
 */

public class HomePresenterImpl implements HomePresenter {
    private HomeView mHomeView;
    public HomePresenterImpl(HomeView homeView) {
        this.mHomeView = homeView;
    }
}
