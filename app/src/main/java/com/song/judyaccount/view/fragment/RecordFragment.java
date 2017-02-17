package com.song.judyaccount.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.song.judyaccount.R;
import com.song.judyaccount.adapter.RecordAdapter;
import com.song.judyaccount.db.RecordDao;
import com.song.judyaccount.model.RecordBean;
import com.song.judyaccount.view.activity.WriteActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Judy on 2017/2/15.
 */

public class RecordFragment extends Fragment {
    private FloatingActionButton mIvWrite;
    private TextView mTvIncomeMonth;
    private TextView mTvIncomeNum;
    private TextView mTvExpenseMonth;
    private TextView mTvExpenseNum;
    private RecyclerView mRecyclerViewRecord;
    private List<RecordBean> mData;
    private RecordAdapter mRecordAdapter;
    private RecordDao mRecordDao;
    private LinearLayoutManager mLayoutManager;
    private ImageView mIvDelete;
    private ImageView mIvEdit;
    private TextView mTv_des_incom;
    private TextView mTv_des_expense;
    private Integer prePosition;
    private View mRootView;
    private AnimatorSet mOpenAnimatorSet;
    private AnimatorSet mCloseAnimatorSet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = View.inflate(getContext(), R.layout.fragment_record, null);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIvWrite = (FloatingActionButton) view.findViewById(R.id.iv_write);
        mTvIncomeMonth = (TextView) view.findViewById(R.id.tv_income_month);
        mTvIncomeNum = (TextView) view.findViewById(R.id.tv_income_num);
        mTvExpenseMonth = (TextView) view.findViewById(R.id.tv_expense_month);
        mTvExpenseNum = (TextView) view.findViewById(R.id.tv_expense_num);
        mRecyclerViewRecord = (RecyclerView) view.findViewById(R.id.recycler_view_record);

        initAnim();

        mData = new ArrayList<>();
        mRecordDao = new RecordDao(getContext());
        mIvWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WriteActivity.class);
                intent.putExtra("time", 0);
                startActivity(intent);
            }
        });
        updateView();

        mRecordAdapter.setOnIconClickListener(new RecordAdapter.OnIconClickListener() {
            @Override
            public void onIconClick(int position) {
                if (prePosition != null && prePosition == position) {
                    closeAnim(position);
                } else {
                    if (prePosition != null) {
                        closeAnim(prePosition);
                    }
                    openAnim(position);
                }
            }

            @Override
            public void onDeleteClick(int position) {
                closeWithoutAnim(position);
                long time = mData.get(position).calendar.getTime().getTime();
                mRecordDao.deleteRecord(time);
                updateView();
            }

            @Override
            public void onEditClick(int position) {
                Intent intent = new Intent(getContext(), WriteActivity.class);
                intent.putExtra("time", mData.get(position).calendar.getTime().getTime());
                startActivity(intent);
            }
        });
        mRecyclerViewRecord.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (prePosition != null) {
                    closeAnim(prePosition);
                }
            }

        });

    }

    private void closeWithoutAnim(int position) {
        getViewByPosition(position);
        mTv_des_incom.setAlpha(1f);
        mTv_des_expense.setAlpha(1f);
        mIvDelete.setAlpha(0f);
        mIvEdit.setAlpha(0f);
        mIvDelete.setTranslationX(mRecyclerViewRecord.getWidth()*0.35f);
        mIvEdit.setTranslationX(-mRecyclerViewRecord.getWidth()*0.35f);
        prePosition = null;
    }

    private void initMonthBudget() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        mTvExpenseMonth.setText(month+"月支出");
        mTvIncomeMonth.setText(month+"月收入");
        double income = 0;
        double expense = 0;
        for (int i = 0; i < mData.size(); i++) {
            RecordBean bean = mData.get(i);
            if (bean.isIncome) {
                income += bean.money;
            } else {
                expense += bean.money;
            }
        }
        mTvIncomeNum.setText("￥"+income);
        mTvExpenseNum.setText("￥"+expense);
    }

    private void updateView() {
        mData.clear();
        List<RecordBean> recordBeanList = mRecordDao.queryAllRecord();
        mData.addAll(recordBeanList);
        if (mRecordAdapter == null) {
            mRecordAdapter = new RecordAdapter(mData);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerViewRecord.setLayoutManager(mLayoutManager);
            mRecyclerViewRecord.setAdapter(mRecordAdapter);
        } else {
            mRecordAdapter.notifyDataSetChanged();
        }

        initMonthBudget();
        EventBus.getDefault().post(mData);
    }

    private void initAnim() {
        mCloseAnimatorSet = new AnimatorSet();
    }

    private void closeAnim(int position) {
        getViewByPosition(position);
        mCloseAnimatorSet = new AnimatorSet();
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(mTv_des_incom, "alpha", 0f, 1f);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(mTv_des_expense, "alpha", 0f, 1f);
        ObjectAnimator alpha3 = ObjectAnimator.ofFloat(mIvDelete, "alpha", 1f, 0f);
        ObjectAnimator alpha4 = ObjectAnimator.ofFloat(mIvEdit, "alpha", 1f, 0f);
        ObjectAnimator translationX1 = ObjectAnimator.ofFloat(mIvDelete, "translationX", -mRecyclerViewRecord.getWidth()*0.35f, 0f);
        ObjectAnimator translationX2 = ObjectAnimator.ofFloat(mIvEdit, "translationX", mRecyclerViewRecord.getWidth()*0.35f, 0f);
        mCloseAnimatorSet.setDuration(100);
        mCloseAnimatorSet.playTogether(alpha1,alpha2,alpha3,alpha4,translationX1,translationX2);
        if (!mOpenAnimatorSet.isRunning() && !mCloseAnimatorSet.isRunning()) {
            mCloseAnimatorSet.start();
            prePosition = null;
        }
    }

    private void openAnim(int position) {
        getViewByPosition(position);
        mOpenAnimatorSet = new AnimatorSet();
        ObjectAnimator alpha11 = ObjectAnimator.ofFloat(mTv_des_incom, "alpha", 1f, 0f);
        ObjectAnimator alpha22 = ObjectAnimator.ofFloat(mTv_des_expense, "alpha", 1f, 0f);
        ObjectAnimator alpha33 = ObjectAnimator.ofFloat(mIvDelete, "alpha", 0f, 1f);
        ObjectAnimator alpha44 = ObjectAnimator.ofFloat(mIvEdit, "alpha", 0f, 1f);
        ObjectAnimator translationX11 = ObjectAnimator.ofFloat(mIvDelete, "translationX", 0f, -mRecyclerViewRecord.getWidth()*0.35f);
        ObjectAnimator translationX22 = ObjectAnimator.ofFloat(mIvEdit, "translationX", 0f, mRecyclerViewRecord.getWidth()*0.35f);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        translationX11.setInterpolator(bounceInterpolator);
        translationX22.setInterpolator(bounceInterpolator);
        mOpenAnimatorSet.setDuration(500);
        mOpenAnimatorSet.playTogether(alpha11,alpha22,alpha33,alpha44,translationX11,translationX22);
        if (!mOpenAnimatorSet.isRunning() && !mCloseAnimatorSet.isRunning()) {
            mOpenAnimatorSet.start();
            prePosition = position;

        }
    }

    private void getViewByPosition(int position) {
        int i = position - mLayoutManager.findFirstVisibleItemPosition();
        if (i >= 0) {
            View view = mRecyclerViewRecord.getChildAt(i);
            if (view != null) {
                mIvDelete = (ImageView) view.findViewById(R.id.iv_delete);
                mIvEdit = (ImageView) view.findViewById(R.id.iv_edit);
                mTv_des_incom = (TextView) view.findViewById(R.id.tv_des_incom);
                mTv_des_expense = (TextView) view.findViewById(R.id.tv_des_expense);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (prePosition != null) {
            closeWithoutAnim(prePosition);
        }
    }
}
