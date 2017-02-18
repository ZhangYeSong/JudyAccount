package com.song.judyaccount.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.avos.sns.SNS;
import com.avos.sns.SNSType;
import com.bumptech.glide.Glide;
import com.song.judyaccount.R;
import com.song.judyaccount.adapter.HomePagerAdapter;
import com.song.judyaccount.model.HomePagerInfo;
import com.song.judyaccount.view.fragment.FundFragment;
import com.song.judyaccount.view.fragment.MoreFragment;
import com.song.judyaccount.view.fragment.RecordFragment;
import com.song.judyaccount.view.fragment.TableFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolBar;
    private TextView mTvToolbarTitle;
    private ViewPager mVpHome;
    private TabLayout mTabLayoutHome;
    private NavigationView mNavView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private final String[] TITLES = {"记账", "报表", "资金", "更多"};
    private final int[] ICONS = {R.mipmap.record, R.mipmap.table, R.mipmap.fund, R.mipmap.more};
    private CircleImageView mCivPortrait;
    private TextView mTvNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        mTvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        mVpHome = (ViewPager) findViewById(R.id.vp_home);
        mTabLayoutHome = (TabLayout) findViewById(R.id.tab_layout_home);
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mCivPortrait = (CircleImageView) findViewById(R.id.civ_portrait);
        mTvNickname = (TextView) findViewById(R.id.tv_nickname);
        mVpHome.setOffscreenPageLimit(4);
        mVpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvToolbarTitle.setText(TITLES[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initActionBar();
        initViewPager();
        mNavView.setNavigationItemSelectedListener(this);
        //showUserDataFromNet(mCivPortrait, mTvNickname);
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
        int selectColor = Color.parseColor("#000000");
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

    public void showUserDataFromNet(CircleImageView civPortrait, TextView tvNickname) {
        AVUser currentUser = AVUser.getCurrentUser();
        String nickName = (String) currentUser.get("nickName");
        if (nickName != null) {
            tvNickname.setText(nickName);
        }
        String portraitUrl = (String) currentUser.get("portraitUrl");
        Glide.with(this).load(portraitUrl).into(civPortrait);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SNS.logout(this, SNSType.AVOSCloudSNSQQ);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_setting:
                Toast.makeText(this, "进入设置界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_response:
                Toast.makeText(this, "进入反馈界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_help:
                Toast.makeText(this, "进入帮助界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "进入分享界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_resume:
                Toast.makeText(this, "进入作者界面", Toast.LENGTH_SHORT).show();
                break;

        }
        return false;
    }
}
