package com.song.judyaccount.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.song.judyaccount.R;
import com.song.judyaccount.adapter.HomePagerAdapter;
import com.song.judyaccount.model.HomePagerInfo;
import com.song.judyaccount.presenter.HomePresenterImpl;
import com.song.judyaccount.view.fragment.FundFragment;
import com.song.judyaccount.view.fragment.MoreFragment;
import com.song.judyaccount.view.fragment.RecordFragment;
import com.song.judyaccount.view.fragment.TableFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView{
    private Toolbar mToolBar;
    private TextView mTvToolbarTitle;
    private ViewPager mVpHome;
    private TabLayout mTabLayoutHome;
    private NavigationView mNavView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private HomePresenterImpl mHomePresenter;
    private final String[] TITLES = {"记账", "报表", "资金", "更多"};
    private final int[] ICONS = {R.mipmap.record, R.mipmap.table, R.mipmap.fund, R.mipmap.more};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mHomePresenter = new HomePresenterImpl(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        mTvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        mVpHome = (ViewPager) findViewById(R.id.vp_home);
        mTabLayoutHome = (TabLayout) findViewById(R.id.tab_layout_home);
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        initActionBar();
        initViewPager();
    }

    private void initViewPager() {
        final List<HomePagerInfo> data = new ArrayList<>();
        data.add(new HomePagerInfo(TITLES[0], new RecordFragment()));
        data.add(new HomePagerInfo(TITLES[1], new TableFragment()));
        data.add(new HomePagerInfo(TITLES[2], new FundFragment()));
        data.add(new HomePagerInfo(TITLES[3], new MoreFragment()));
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), data);
        mVpHome.setAdapter(homePagerAdapter);
        mTabLayoutHome.setupWithViewPager(mVpHome);
        int normalColor = Color.parseColor("#8C8C8C");
        int selectColor = Color.parseColor("#3F51B5");
        mTabLayoutHome.setTabTextColors(normalColor, selectColor);
        mTabLayoutHome.setSelectedTabIndicatorColor(selectColor);

        for (int i = 0; i < 4; i++) {
            mTabLayoutHome.getTabAt(i).setIcon(ICONS[i]);
        }

    }

    private void initActionBar() {
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerToggle.onOptionsItemSelected(item);

                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
