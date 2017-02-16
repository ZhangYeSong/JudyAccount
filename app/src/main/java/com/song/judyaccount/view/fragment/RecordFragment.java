package com.song.judyaccount.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.song.judyaccount.R;
import com.song.judyaccount.adapter.RecordAdapter;
import com.song.judyaccount.db.RecordDao;
import com.song.judyaccount.model.RecordBean;
import com.song.judyaccount.view.activity.WriteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Judy on 2017/2/15.
 */

public class RecordFragment extends Fragment {
    private ImageView mIvWrite;
    private TextView mTvIncomeMonth;
    private TextView mTvIncomeNum;
    private TextView mTvExpenseMonth;
    private TextView mTvExpenseNum;
    private RecyclerView mRecyclerViewRecord;
    private List<RecordBean> mData;
    private RecordAdapter mRecordAdapter;
    private RecordDao mRecordDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_record, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIvWrite = (ImageView) view.findViewById(R.id.iv_write);
        mTvIncomeMonth = (TextView) view.findViewById(R.id.tv_income_month);
        mTvIncomeNum = (TextView) view.findViewById(R.id.tv_income_num);
        mTvExpenseMonth = (TextView) view.findViewById(R.id.tv_expense_month);
        mTvExpenseNum = (TextView) view.findViewById(R.id.tv_expense_num);
        mRecyclerViewRecord = (RecyclerView) view.findViewById(R.id.recycler_view_record);

        mData = new ArrayList<>();
        mRecordDao = new RecordDao(getContext());
        List<RecordBean> recordBeanList = mRecordDao.queryAllRecord();
        mData.addAll(recordBeanList);
        mIvWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WriteActivity.class));
            }
        });
        updateView();
    }

    private void updateView() {
        if (mRecordAdapter == null) {
            mRecordAdapter = new RecordAdapter(mData);
            mRecyclerViewRecord.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerViewRecord.setAdapter(mRecordAdapter);
        } else {
            mRecordAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
}
