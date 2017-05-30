package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zjlyyq.demo.R;

import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/5/7.
 */

public class GalikGridViewAdapter extends BaseAdapter {
    private ArrayList<String> imageUrls;
    private Context mContext;
    public GalikGridViewAdapter(Context context,ArrayList<String> list){
        this.mContext = context;
        this.imageUrls = list;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.gridview_item,null,false);
            holder.imageView=(ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        String url = null;
        url = imageUrls.get(position);
        Glide.with(mContext).load(url).into(holder.imageView);
        return  convertView;
    }

    class ViewHolder{
        ImageView imageView;
    }
}
