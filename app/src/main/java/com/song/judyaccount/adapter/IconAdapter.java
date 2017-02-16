package com.song.judyaccount.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.song.judyaccount.R;
import com.song.judyaccount.model.IconBean;

import java.util.List;

/**
 * Created by Judy on 2017/2/16.
 */

public class IconAdapter extends BaseAdapter {
    private List<IconBean> mData;

    public IconAdapter(List<IconBean> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public IconBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.list_item_icon, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        IconBean bean = getItem(position);
        holder.iv.setImageResource(bean.resId);
        holder.tv.setText(bean.type);
        if (bean.selected) {
            convertView.setBackgroundResource(R.drawable.shape_quan_pink);
        } else {
            convertView.setBackgroundResource(0);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView iv;
        TextView tv;
    }

}
