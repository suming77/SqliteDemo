package com.suming.sqlitedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @创建者 mingyan.su
 * @创建时间 2018/9/29 14:03
 * @类描述 ${TODO}
 */
public class BleListAdapter extends BaseAdapter {
    private Context context;
    private List<OpenBleInfo> data;

    public BleListAdapter(Context context, List<OpenBleInfo> orderList) {
        this.context = context;
        this.data = orderList;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        OpenBleInfo address = data.get(position);
        if (address == null) {
            return null;
        }

        ViewHolder holder = null;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.show_sql_item, null);

            holder = new ViewHolder();
            holder.dateIdTextView = (TextView) view.findViewById(R.id.dateIdTextView);
            holder.dateCustomTextView = (TextView) view.findViewById(R.id.dateCustomTextView);
            holder.dateOrderPriceTextView = (TextView) view.findViewById(R.id.dateOrderPriceTextView);
            holder.dateCountoryTextView = (TextView) view.findViewById(R.id.dateCountoryTextView);

            view.setTag(holder);
        }

        holder.dateIdTextView.setText(address.getUser_id() + "");
        holder.dateCustomTextView.setText(address.getName());
        holder.dateOrderPriceTextView.setText(address.getAddress());
        holder.dateCountoryTextView.setText(address.getTime());

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public static class ViewHolder {
        public TextView dateIdTextView;
        public TextView dateCustomTextView;
        public TextView dateOrderPriceTextView;
        public TextView dateCountoryTextView;
    }
}
