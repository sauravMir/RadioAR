package com.radioar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ibrar on 3/16/2017.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    String[] text;


    public CustomAdapter(Context context, String[] text) {
        this.context = context;
        this.text = text;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {

        public TextView tvlstCell;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CustomAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new CustomAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.custom_list_cell, null);
            holder.tvlstCell = (TextView) convertView.findViewById(R.id.tvlstCell);
            convertView.setTag(holder);
        } else {
            holder = (CustomAdapter.ViewHolder) convertView.getTag();
        }

        holder.tvlstCell.setText(text[position]);

        return convertView;


    }
}
