package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.User;

import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/28.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> pictures;
    public GridViewAdapter(Context context,ArrayList<Bitmap> pictures){
        this.mContext = context;
        this.pictures = pictures;
    }
    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public Object getItem(int position) {
        return pictures.get(position);
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
        Bitmap bitmap = pictures.get(position);
        holder.imageView.setImageBitmap(bitmap);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }
}
