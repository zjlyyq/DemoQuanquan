package com.example.zjlyyq.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zjlyyq on 2016/11/7.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, Object>> mList;

    public GridViewAdapter(Context mContext,ArrayList<HashMap<String, Object>> mList) {
        super();
        this.mContext = mContext;
        this.mList = mList;
    }
    @Override
    public int getCount() {
        if (mList == null) {
            return 5;
        } else {
            return this.mList.size()-3;
        }
    }
    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            return this.mList.get(position+3);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from
                    (this.mContext).inflate(R.layout.gridview_item, null, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (this.mList != null) {
            HashMap<String, Object> hashMap = this.mList.get(position);
            if (holder.imageView != null) {
                holder.imageView.setImageResource(R.drawable.touxiang6);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "第"+(position+1)+"个", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }
}