package com.song.judyaccount.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.song.judyaccount.R;
import com.song.judyaccount.db.RecordDao;
import com.song.judyaccount.model.RecordBean;
import com.song.judyaccount.view.activity.WriteActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.song.judyaccount.R.id.bt_pie_chart;
import static com.song.judyaccount.R.id.bt_title_expense;

/**
 * Created by Judy on 2017/2/15.
 */

public class TableFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private Button mBtPieChart;
    private Button mBtLineChart;
    private Button mBtTitleExpense;
    private Button mBtTitleIncome;
    private PieChart mPieChart;
    private RecordDao mRecordDao;
    private HashMap<String, Double> mExHm;
    private HashMap<String, Double> mInHm;
    int[] colors = new int[4];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = View.inflate(getContext(), R.layout.fragment_table, null);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPieChart = (PieChart) view.findViewById(R.id.pie_chart);
        mBtPieChart = (Button) view.findViewById(bt_pie_chart);
        mBtLineChart = (Button) view.findViewById(R.id.bt_line_chart);
        mBtTitleExpense = (Button) view.findViewById(bt_title_expense);
        mBtTitleIncome = (Button) view.findViewById(R.id.bt_title_income);
        mPieChart = (PieChart) view.findViewById(R.id.pie_chart);

        mBtPieChart.setOnClickListener(this);
        mBtLineChart.setOnClickListener(this);
        mBtTitleExpense.setOnClickListener(this);
        mBtTitleIncome.setOnClickListener(this);

        EventBus.getDefault().register(this);

        mRecordDao = new RecordDao(getContext());

        initColor();

        getBudgetData();

        updatePieChart();
    }

    private void initColor() {
        colors[0] = getResources().getColor(R.color.light_blue);
        colors[1] = getResources().getColor(R.color.light_green);
        colors[2] = getResources().getColor(R.color.light_yellow);
        colors[3] = getResources().getColor(R.color.light_orange);
    }

    private void updatePieChart() {
        HashMap<String, Double> hs;
        if (mBtTitleIncome.isEnabled()) {
            hs = mExHm;
        } else {
            hs = mInHm;
        }
        List<PieEntry> entries = new ArrayList<>();
        Set<String> strings = hs.keySet();
        for (String string : strings) {
            entries.add(new PieEntry(Float.parseFloat(hs.get(string)+""),string));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, "支出饼状图");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setValueTextColor(Color.WHITE);
        PieData pieData = new PieData(pieDataSet);

        mPieChart.setData(pieData);
        mPieChart.invalidate();
    }

    private void getBudgetData() {
        List<RecordBean> recordBeanList = mRecordDao.queryAllRecord();
        mExHm = new HashMap<>();
        mInHm = new HashMap<>();
        for (RecordBean recordBean : recordBeanList) {
            if (!recordBean.isIncome) {
                String key = WriteActivity.expenseTypes[recordBean.type];
                double value;
                if (mExHm.get(key) == null) {
                    value = recordBean.money;
                } else {
                    value = mExHm.get(key).doubleValue() + recordBean.money;
                }
                mExHm.put(key, value);
            } else {
                String key = WriteActivity.incomeTypes[recordBean.type];
                double value;
                if (mInHm.get(key) == null) {
                    value = recordBean.money;
                } else {
                    value = mInHm.get(key).doubleValue() + recordBean.money;
                }
                mInHm.put(key, value);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case bt_pie_chart:
                mBtPieChart.setEnabled(false);
                mBtLineChart.setEnabled(true);
                break;
            case R.id.bt_line_chart:
                mBtPieChart.setEnabled(true);
                mBtLineChart.setEnabled(false);
                break;
            case bt_title_expense:
                mBtTitleExpense.setEnabled(false);
                mBtTitleIncome.setEnabled(true);
                if (mBtLineChart.isEnabled()) {
                    updatePieChart();
                }
                break;
            case R.id.bt_title_income:
                mBtTitleExpense.setEnabled(true);
                mBtTitleIncome.setEnabled(false);
                if (mBtLineChart.isEnabled()) {
                    updatePieChart();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(List<RecordBean> data) {
        getBudgetData();
        updatePieChart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
