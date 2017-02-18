package com.song.judyaccount.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.song.judyaccount.R;
import com.song.judyaccount.db.RecordDao;
import com.song.judyaccount.model.RecordBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Judy on 2017/2/15.
 */

public class MoreFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private CircleImageView mCivPortrait;
    private TextView mTvNickname;
    private TextView mTvSetting;
    private TextView mTvSync;
    private TextView mTvHelp;
    private TextView mTvTheme;
    private TextView mTvResponse;
    private TextView mTvResume;
    private AVUser mCurrentUser;
    private Uri mUri;
    private static String path = "/sdcard/JudyAccount/myHead/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = View.inflate(getContext(), R.layout.fragment_more, null);
        }
        assignViews();
        initEvent();
        return mRootView;
    }

    private void initEvent() {
        mCivPortrait.setOnClickListener(this);
        mTvSetting.setOnClickListener(this);
        mTvSync.setOnClickListener(this);
        mTvHelp.setOnClickListener(this);
        mTvTheme.setOnClickListener(this);
        mTvResponse.setOnClickListener(this);
        mTvResume.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCurrentUser = AVUser.getCurrentUser();
        showUserDataFromNet();
    }

    private void showUserDataFromNet() {
        String nickName = (String) mCurrentUser.get("nickName");
        if (nickName != null) {
            mTvNickname.setText(nickName);
        }
        String portraitUrl = (String) mCurrentUser.get("portraitUrl");
        Glide.with(this).load(portraitUrl).into(mCivPortrait);

    }

    private void assignViews() {
        mCivPortrait = (CircleImageView) mRootView.findViewById(R.id.civ_portrait);
        mTvNickname = (TextView) mRootView.findViewById(R.id.tv_nickname);
        mTvSetting = (TextView) mRootView.findViewById(R.id.tv_setting);
        mTvSync = (TextView) mRootView.findViewById(R.id.tv_sync);
        mTvHelp = (TextView) mRootView.findViewById(R.id.tv_help);
        mTvTheme = (TextView) mRootView.findViewById(R.id.tv_theme);
        mTvResponse = (TextView) mRootView.findViewById(R.id.tv_response);
        mTvResume = (TextView) mRootView.findViewById(R.id.tv_resume);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_portrait:
                showTypeDialog();
                break;
            case R.id.tv_setting:

                break;
            case R.id.tv_sync:
                syncRecordDb();
                break;
            case R.id.tv_help:

                break;
            case R.id.tv_theme:

                break;
            case R.id.tv_response:

                break;
            case R.id.tv_resume:

                break;
        }
    }

    private void syncRecordDb() {
        compareTimeAndSync();
    }

    private void compareTimeAndSync() {
        RecordDao recordDao = new RecordDao(getContext());
        List<RecordBean> recordBeanList = recordDao.queryAllRecord();
        if (recordBeanList == null || recordBeanList.size() < 1) {
            downloadDb();
            return;
        }
        Date localTime = recordBeanList.get(0).calendar.getTime();
        Date netTime = (Date) mCurrentUser.get("netRecordDbDate");
        if (netTime == null) {
            uploadDb();
            return;
        }

        if (localTime.after(netTime)) {
            uploadDb();
        } else {
            downloadDb();
        }

    }

    private void uploadDb() {
        File dbFile = new File(getActivity().getApplication().getDatabasePath("Records")+".db");
        if (!dbFile.exists()) {
            Toast.makeText(getContext(), "本地和云存储均没有数据！", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            final String recordDbObjectId = (String) mCurrentUser.get("recordDbObjectId");
            if (recordDbObjectId != null && !recordDbObjectId.equals("")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final AVFile avFileDel;
                        try {
                            avFileDel = AVFile.withObjectId(recordDbObjectId);
                            avFileDel.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(AVException e) {

                                }
                            });
                        } catch (AVException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }


            final AVFile avFile = AVFile.withAbsoluteLocalPath("Records.db", dbFile.getAbsolutePath());
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    mCurrentUser.put("recordDbObjectId", avFile.getObjectId());
                    mCurrentUser.put("netRecordDbDate", new Date());
                    mCurrentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {

                        }
                    });
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void downloadDb() {

    }

    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getActivity(), R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // 裁剪图片
                    mUri = data.getData();
                    cropPhoto(mUri);
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    // 裁剪图片
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    mUri = Uri.fromFile(temp);
                    cropPhoto(mUri);
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap head = extras.getParcelable("data");
                    if (head != null) {
                        setPicToView(head);
                        mCivPortrait.setImageBitmap(head);// 用ImageView显示出来
                        /**
                         * 上传服务器代码
                         */
                        try {
                            final String headObjectId = (String) mCurrentUser.get("headObjectId");
                            if (headObjectId != null && !headObjectId.equals("")) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final AVFile avFileDel;
                                        try {
                                            avFileDel = AVFile.withObjectId(headObjectId);
                                            avFileDel.deleteInBackground(new DeleteCallback() {
                                                @Override
                                                public void done(AVException e) {

                                                }
                                            });
                                        } catch (AVException e) {
                                            e.printStackTrace();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                            }
                            final AVFile avFile = AVFile.withAbsoluteLocalPath("head.png", Environment.getExternalStorageDirectory()+"/JudyAccount/myHead/head.jpg" );
                            avFile.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    final String url = avFile.getUrl();
                                    if (url != null && !url.equals("")) {
                                        mCurrentUser.put("portraitUrl", url);
                                        mCurrentUser.put("headObjectId", avFile.getObjectId());
                                        mCurrentUser.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {

                                            }
                                        });
                                    }
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
                break;

        }
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
