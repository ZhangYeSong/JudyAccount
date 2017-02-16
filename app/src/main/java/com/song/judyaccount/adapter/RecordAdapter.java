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
    private OnIconClickListener mOnIconClickListener;
    public RecordAdapter(List<RecordBean> data) {
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        RecordBean recordBean = mData.get(position);
        int month = recordBean.calendar.get(Calendar.MONTH) + 1;
        int day = recordBean.calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfYear = recordBean.calendar.get(Calendar.DAY_OF_YEAR);
        if (position == 0) {
            holder.mTvDate.setText(month+"月"+day+"日"+"  收支:"+getBudget(dayOfYear,position));
            holder.mTvDate.setVisibility(View.VISIBLE);
        } else {
            if (mData.get(position - 1).calendar.get(Calendar.DAY_OF_MONTH) == day) {
                holder.mTvDate.setText(month+"月"+day+"日"+"  收支:"+getBudget(dayOfYear,position));
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

        holder.mIvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnIconClickListener.onIconClick(position);
            }
        });
        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnIconClickListener.onDeleteClick(position);
            }
        });
        holder.mIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnIconClickListener.onEditClick(position);
            }
        });
    }

    private double getBudget(int dayOfYear, int position) {
        double budget = 0;
        for (int i = 0; i < mData.size(); i++) {
            RecordBean bean = mData.get(i);
            if (bean.calendar.get(Calendar.DAY_OF_YEAR) == dayOfYear) {
                if (bean.isIncome) {
                    budget += bean.money;
                } else {
                    budget -= bean.money;
                }
            }
        }
        return budget;
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
        private final ImageView mIvDelete;
        private final ImageView mIvEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_date);
            mTv_des_incom = (TextView) itemView.findViewById(R.id.tv_des_incom);
            mTv_des_expense = (TextView) itemView.findViewById(R.id.tv_des_expense);
            mIvType = (ImageView) itemView.findViewById(R.id.iv_type);
            mIvDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            mIvEdit = (ImageView) itemView.findViewById(R.id.iv_edit);
        }
    }

    public interface OnIconClickListener {
        void onIconClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnIconClickListener(OnIconClickListener onIconClickListener) {
        this.mOnIconClickListener = onIconClickListener;
    }
}
