package com.song.judyaccount.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.song.judyaccount.R;

public class HomeActivity extends AppCompatActivity implements HomeView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
