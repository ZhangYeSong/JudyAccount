package com.song.judyaccount.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.song.judyaccount.R;
import com.song.judyaccount.model.RecordBean;
import com.song.judyaccount.view.activity.WriteActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Judy on 2017/2/16.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<RecordBean> mData;
    public RecordAdapter(List<RecordBean> data) {
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecordBean recordBean = mData.get(position);
        int month = recordBean.calendar.get(Calendar.MONTH) + 1;
        int day = recordBean.calendar.get(Calendar.DAY_OF_MONTH);
        holder.mTvDate.setText(month+"月"+day+"日");
        if (position == 0) {
            holder.mTvDate.setVisibility(View.VISIBLE);
        } else {
            if (mData.get(position - 1).calendar.get(Calendar.DAY_OF_MONTH) == day) {
                holder.mTvDate.setVisibility(View.GONE);
            } else {
                holder.mTvDate.setVisibility(View.VISIBLE);
            }
        }
        int resId;
        if (recordBean.isIncome) {
            holder.mTv_des_incom.setText(WriteActivity.incomeTypes[recordBean.type] + "收入￥" + recordBean.money);
            holder.mTv_des_expense.setText("");
            resId = WriteActivity.incomeIds[recordBean.type];
        } else {
            holder.mTv_des_incom.setText("");
            holder.mTv_des_expense.setText((WriteActivity.expenseTypes[recordBean.type] + "支出￥" + recordBean.money));
            resId = WriteActivity.expenseIds[recordBean.type];
        }
        holder.mIvType.setImageResource(resId);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvDate;
        private final TextView mTv_des_incom;
        private final TextView mTv_des_expense;
        private final ImageView mIvType;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_date);
            mTv_des_incom = (TextView) itemView.findViewById(R.id.tv_des_incom);
            mTv_des_expense = (TextView) itemView.findViewById(R.id.tv_des_expense);
            mIvType = (ImageView) itemView.findViewById(R.id.iv_type);
        }
    }
}
