package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.zjlyyq.demo.R;

import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/16.
 */

public class PhotoAdapter extends BaseAdapter {
    private ArrayList<Bitmap> mList;
    private Context mAppContext;
    public PhotoAdapter(Context context,ArrayList<Bitmap> list){
        this.mList = list;
        mAppContext = context;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            view = LayoutInflater.from(this.mAppContext).inflate(R.layout.photo_view,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)view.findViewById(R.id.photo_view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.imageView.setImageBitmap(mList.get(i));
        return view;
    }
    private class ViewHolder {
        ImageView imageView;
    }
}
