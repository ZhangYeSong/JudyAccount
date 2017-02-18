package com.song.judyaccount.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.sns.SNS;
import com.avos.sns.SNSBase;
import com.avos.sns.SNSCallback;
import com.avos.sns.SNSException;
import com.avos.sns.SNSType;
import com.song.judyaccount.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;



public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private CircleImageView mCivPortrait;
    private TextInputLayout mTextinputlayoutUserLogin;
    private TextInputLayout mTextinputlayouPassLogin;
    private TextInputLayout mTextinputlayouPassConfirm;
    private Button mBtLogin;
    private CheckBox mCbRbPaw;
    private CheckBox mCbAotuLogin;
    private ImageView mIvQqLog;
    private ImageView mIvWxLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        assignViews();

        mBtLogin.setOnClickListener(this);
        mIvQqLog.setOnClickListener(this);
        mIvWxLog.setOnClickListener(this);

    }

    private void initQQSSO() {
        final SNSCallback myCallback = new SNSCallback() {
            @Override
            public void done(final SNSBase object, SNSException e) {
                if (e == null) {
                    final Map<String, Object> qqUserInfo = SNS.userInfo(SNSType.AVOSCloudSNSQQ);
                    SNS.loginWithAuthData(qqUserInfo, new LogInCallback() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if (e == null) {
                                JSONObject authorizedData = object.authorizedData();
                                if (authorizedData != null && avUser.isNew()) {
                                    updateUserData(authorizedData, avUser);
                                }
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, ("create new user with auth data error: " + e.getMessage()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        };

        try {
            SNS.setupPlatform(this, SNSType.AVOSCloudSNSQQ, "1105919929", "", "https://leancloud.cn/1.1/sns/callback/vofwg9xy5yzkkaq5");
        } catch (AVException e) {
            e.printStackTrace();
            Toast.makeText(this, "e:" + e, Toast.LENGTH_SHORT).show();
        }
        SNS.loginWithCallback(this, SNSType.AVOSCloudSNSQQ, myCallback);

    }

    private void updateUserData(JSONObject authorizedData, AVUser avUser) {
        try {
            avUser.put("nickName", authorizedData.get("nickname"));
            avUser.put("portraitUrl", authorizedData.get("figureurl_qq_2"));
            avUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void assignViews() {
        mCivPortrait = (CircleImageView) findViewById(R.id.civ_portrait);
        mTextinputlayoutUserLogin = (TextInputLayout) findViewById(R.id.textinputlayout_user_login);
        mTextinputlayouPassLogin = (TextInputLayout) findViewById(R.id.textinputlayou_pass_login);
        mTextinputlayouPassConfirm = (TextInputLayout) findViewById(R.id.textinputlayou_pass_confirm);
        mBtLogin = (Button) findViewById(R.id.bt_login);
        mCbRbPaw = (CheckBox) findViewById(R.id.cb_rb_paw);
        mCbAotuLogin = (CheckBox) findViewById(R.id.cb_aotu_login);
        mIvQqLog = (ImageView) findViewById(R.id.iv_qq_log);
        mIvWxLog = (ImageView) findViewById(R.id.iv_wx_log);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:

                break;
            case R.id.iv_qq_log:
                initQQSSO();

                break;
            case R.id.iv_wx_log:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SNS.onActivityResult(requestCode, resultCode, data, SNSType.AVOSCloudSNSQQ);
    }


}
