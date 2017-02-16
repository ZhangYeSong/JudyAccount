package com.song.judyaccount.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.song.judyaccount.R;
import com.song.judyaccount.adapter.IconAdapter;
import com.song.judyaccount.db.RecordDao;
import com.song.judyaccount.model.IconBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextView.OnEditorActionListener {
    private TextView mTvAccountBook;
    private Button mBtTitleExpense;
    private Button mBtTitleIncome;
    private TextView mTvIsExpense;
    private EditText mEtMoney;
    private GridView mGlWrite;
    private List<IconBean> mData;
    private List<IconBean> mExpenseData;
    private List<IconBean> mIncomeData;
    private IconAdapter mIconAdapter;
    private int prePostion;
    public static final int[] expenseIds = {R.mipmap.expense_common,R.mipmap.expense_dinner,R.mipmap.expense_traffic,R.mipmap.expense_shopping,R.mipmap.expense_gift_money,
            R.mipmap.expense_day_use,R.mipmap.expense_food,R.mipmap.expense_fruit,R.mipmap.expense_eat,R.mipmap.expense_beauty,
            R.mipmap.expense_closes,R.mipmap.expense_drink,R.mipmap.expense_baby,R.mipmap.expense_music,R.mipmap.expense_film,
            R.mipmap.expense_house,R.mipmap.expense_elec,R.mipmap.expense_phone,R.mipmap.expense_invest,R.mipmap.expense_sex};
    public static final String[] expenseTypes = {"花钱", "餐饮", "交通","购物","红包",
    "日用","买菜","水果","零食","护肤","服饰","烟酒","育婴","娱乐","电影","住房","水电","话费","投资","情趣"};
    public static final int[] incomeIds = {R.mipmap.income_earn,R.mipmap.income_salary,R.mipmap.income_bonus,R.mipmap.income_fetch,R.mipmap.income_invest};
    public static final String[] incomeTypes = {"赚钱","工资","奖金","报销","投资"};
    private LinearLayout mLlInput;
    private RecordDao mRecordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        mTvAccountBook = (TextView) findViewById(R.id.tv_account_book);
        mBtTitleExpense = (Button) findViewById(R.id.bt_title_expense);
        mBtTitleIncome = (Button) findViewById(R.id.bt_title_income);
        mTvIsExpense = (TextView) findViewById(R.id.tv_is_expense);
        mEtMoney = (EditText) findViewById(R.id.et_money);
        mGlWrite = (GridView) findViewById(R.id.gl_write);
        mLlInput = (LinearLayout) findViewById(R.id.ll_input);

        mBtTitleExpense.setOnClickListener(this);
        mBtTitleIncome.setOnClickListener(this);
        initList();
        mIconAdapter = new IconAdapter(mData);
        mGlWrite.setAdapter(mIconAdapter);
        mGlWrite.setOnItemClickListener(this);

        mEtMoney.setOnEditorActionListener(this);
    }

    private void initList() {
        mData = new ArrayList<>();
        mExpenseData = new ArrayList<>();
        mIncomeData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mExpenseData.add(new IconBean(expenseIds[i], expenseTypes[i], false));
        }
        for (int i = 0; i < 5; i++) {
            mIncomeData.add(new IconBean(incomeIds[i], incomeTypes[i], false));
        }
        mData.addAll(mExpenseData);
        mData.get(prePostion).selected = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_title_expense:
                mData.get(prePostion).selected = false;
                mBtTitleExpense.setEnabled(false);
                mBtTitleIncome.setEnabled(true);
                mTvIsExpense.setText("花钱");
                mData.clear();
                mData.addAll(mExpenseData);
                mIconAdapter.notifyDataSetChanged();
                mLlInput.setBackgroundResource(R.color.pink);
                mEtMoney.setBackgroundResource(R.color.pink);
                break;
            case R.id.bt_title_income:
                mData.get(prePostion).selected = false;
                mBtTitleExpense.setEnabled(true);
                mBtTitleIncome.setEnabled(false);
                mTvIsExpense.setText("赚钱");
                mData.clear();
                mData.addAll(mIncomeData);
                mIconAdapter.notifyDataSetChanged();
                mLlInput.setBackgroundResource(R.color.orange);
                mEtMoney.setBackgroundResource(R.color.orange);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mData.get(prePostion).selected = false;
        mData.get(position).selected = true;
        prePostion = position;
        mIconAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == mEtMoney.getId()) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                insertRecord();
                finish();
            }
        }
        return false;
    }

    private void insertRecord() {
        mRecordDao = new RecordDao(this);
        boolean isIncome = mBtTitleExpense.isEnabled();
        int type = prePostion;
        double money = Double.parseDouble(mEtMoney.getText().toString());
        mRecordDao.insertRecord(isIncome, type, money, "", new Date().getTime());
    }
}
