package com.song.judyaccount.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import java.util.Calendar;
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
    private Button mBtBarChart;
    private Button mBtTitleExpense;
    private Button mBtTitleIncome;
    private PieChart mPieChart;
    private RecordDao mRecordDao;
    private HashMap<String, Double> mExHm;
    private HashMap<String, Double> mInHm;
    int[] colors = new int[4];
    private BarChart mBarChart;
    private HashMap<Integer, Double> mExBarSet;
    private HashMap<Integer, Double> mInBarSet;
    private int mCurrentMonth;

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
        mBtBarChart = (Button) view.findViewById(R.id.bt_bar_chart);
        mBtTitleExpense = (Button) view.findViewById(bt_title_expense);
        mBtTitleIncome = (Button) view.findViewById(R.id.bt_title_income);
        mPieChart = (PieChart) view.findViewById(R.id.pie_chart);
        mBarChart = (BarChart) view.findViewById(R.id.bar_chart);

        mBtPieChart.setOnClickListener(this);
        mBtBarChart.setOnClickListener(this);
        mBtTitleExpense.setOnClickListener(this);
        mBtTitleIncome.setOnClickListener(this);

        EventBus.getDefault().register(this);
        mCurrentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        mRecordDao = new RecordDao(getContext());

        initColor();
        showChart();

    }

    private void showChart() {
        if (mBtBarChart.isEnabled()) {
            mPieChart.setVisibility(View.VISIBLE);
            mBarChart.setVisibility(View.INVISIBLE);
        } else {
            mPieChart.setVisibility(View.INVISIBLE);
            mBarChart.setVisibility(View.VISIBLE);
        }
        updatePieChart();
        updateLineChart();
    }

    private void initColor() {
        colors[0] = getResources().getColor(R.color.light_blue);
        colors[1] = getResources().getColor(R.color.light_green);
        colors[2] = getResources().getColor(R.color.light_yellow);
        colors[3] = getResources().getColor(R.color.light_orange);
    }

    private void updateLineChart() {
        getBarData();
        HashMap<Integer, Double> map;
        String lebal;
        if (mBtTitleIncome.isEnabled()) {
            map = mExBarSet;
            lebal = mCurrentMonth + "月支出柱状图";
        } else {
            map = mInBarSet;
            lebal = mCurrentMonth + "月收入柱状图";
        }
        List<BarEntry> entries = new ArrayList<>();
        Set<Integer> integers = map.keySet();
        for (Integer integer : integers) {
            entries.add(new BarEntry(integer, new float[]{Float.parseFloat(map.get(integer)+"")}, integer+"日"));
        }
        BarDataSet barDataSet = new BarDataSet(entries, lebal);
        barDataSet.setColors(colors);
        barDataSet.setValueTextSize(16f);
        barDataSet.setValueTextColor(Color.BLACK);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(1);
        mBarChart.setData(barData);
        Description description = new Description();
        description.setText(lebal);
        mBarChart.setDescription(description);
        mBarChart.setFitBars(true);
        mBarChart.invalidate();

    }

    private void updatePieChart() {
        getPieData();

        HashMap<String, Double> hs;
        String lebal;
        if (mBtTitleIncome.isEnabled()) {
            hs = mExHm;
            lebal = "支出饼状图";
        } else {
            hs = mInHm;
            lebal = "收入饼状图";
        }
        List<PieEntry> entries = new ArrayList<>();
        Set<String> strings = hs.keySet();
        for (String string : strings) {
            entries.add(new PieEntry(Float.parseFloat(hs.get(string)+""),string));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, lebal);
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setValueTextColor(Color.WHITE);
        PieData pieData = new PieData(pieDataSet);
        Description description = new Description();
        description.setText(lebal);
        mPieChart.setDescription(description);
        mPieChart.setCenterText(lebal);
        mPieChart.setCenterTextSize(25);
        mPieChart.setCenterTextColor(getResources().getColor(R.color.pink));
        mPieChart.setData(pieData);
        mPieChart.invalidate();
    }

    private void getBarData() {
        List<RecordBean> recordBeanList = mRecordDao.queryAllRecord();
        mExBarSet = new HashMap<>();
        mInBarSet = new HashMap<>();
        for (RecordBean recordBean : recordBeanList) {
            int month = recordBean.calendar.get(Calendar.MONTH);
            int key = recordBean.calendar.get(Calendar.DAY_OF_MONTH);
            double value;
            if (month == Calendar.getInstance().get(Calendar.MONTH)) {
                if (!recordBean.isIncome) {
                    if (mExBarSet.get(key) == null) {
                        value = recordBean.money;
                    } else {
                        value = mExBarSet.get(key).doubleValue() + recordBean.money;
                    }
                    mExBarSet.put(key, value);
                } else {
                    if (mInBarSet.get(key) == null) {
                        value = recordBean.money;
                    } else {
                        value = mInBarSet.get(key).doubleValue() + recordBean.money;
                    }
                    mInBarSet.put(key, value);
                }

            }
        }
    }

    private void getPieData() {
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
                mBtBarChart.setEnabled(true);
                showChart();
                break;
            case R.id.bt_bar_chart:
                mBtPieChart.setEnabled(true);
                mBtBarChart.setEnabled(false);
                showChart();
                break;
            case bt_title_expense:
                mBtTitleExpense.setEnabled(false);
                mBtTitleIncome.setEnabled(true);
                showChart();
                break;
            case R.id.bt_title_income:
                mBtTitleExpense.setEnabled(true);
                mBtTitleIncome.setEnabled(false);
                showChart();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(List<RecordBean> data) {
        showChart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
